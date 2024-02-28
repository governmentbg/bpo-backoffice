package bg.duosoft.ipas.core.mapper.logging;

import bg.duosoft.ipas.core.model.logging.CLogChangeElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.text.Element;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 02.12.2021
 * Time: 16:04
 */
@Slf4j
public class LogParseHandler extends DefaultHandler {

    private List<CLogChangeElement> before = new ArrayList<>();
    private List<CLogChangeElement> after = new ArrayList<>();
    private CLogChangeElement current = null;

    private boolean beforeStarted;
    private boolean afterStarted;
    private StringBuilder textBuffer = null;


    private ElementHierarchy elementHierarchy;
    private static class ElementHierarchy {
        private String nodeName;
        private String nodeKey;
        private List<ElementHierarchy> children = new ArrayList<>();
        private ElementHierarchy parent;

        public ElementHierarchy(String nodeName, String nodeKey, ElementHierarchy parent) {
            this.nodeName = nodeName;
            this.nodeKey = nodeKey;
            this.parent = parent;
        }
    }

    public void startDocument() throws SAXException {
        log.debug("Started parsing document");
        elementHierarchy = new ElementHierarchy(null, null, null);
    }

    public void startElement(String namespaceURI, String simpleName, String qualifiedName, Attributes attributes) throws SAXException {
        String elementName = simpleName; // element name
        if ("".equals(elementName)) {
            elementName = qualifiedName; // not namespace-aware
        }


        String currentElementKey = attributes.getValue("FIELD");
        currentElementKey = currentElementKey == null ? attributes.getValue("NAME") : currentElementKey;
        currentElementKey = currentElementKey == null ? "UNKNOWN" : currentElementKey;
        currentElementKey = currentElementKey.replace("{", "").replace("}", "");

        ElementHierarchy h = new ElementHierarchy(elementName, currentElementKey, elementHierarchy);
        elementHierarchy.children.add(h);
        elementHierarchy = h;


        if ("BEFORE".equals(elementName)) {
            if (beforeStarted || afterStarted) {
                throw new RuntimeException("Should not happen");
            }
            current = new CLogChangeElement(null);
            beforeStarted  = true;
        } else if ("AFTER".equals(elementName)) {
            if (beforeStarted || afterStarted) {
                throw new RuntimeException("Should not happen");
            }
            current = new CLogChangeElement(null);
            afterStarted = true;
        } else if ("ELEMENT".equals(elementName)) {
            CLogChangeElement parent = current;
            current = new CLogChangeElement(current);
            current.setKey(getCurrentElementKey());
            parent.addChild(current);
        }
        textBuffer = null;
    }

    public void endElement(String namespaceURI, String simpleName, String qualifiedName) throws SAXException {
        String elementName = simpleName; // element name
        if ("".equals(elementName)) {
            elementName = qualifiedName; // not namespace-aware
        }

        if ("AFTER".equals(elementName)) {
            after.add(current);
            afterStarted = false;
            current = null;
        }
        if ("BEFORE".equals(elementName)) {
            before.add(current);
            beforeStarted = false;
            current = null;
        }
        if ("FIELD".equals(elementName)) {
            /**
             * samo ako tekushtiq field nqma children. V starite logove (ot originalniq ipas) i complex elementite sa vyv field. Togava trqbva da se ignorira field tag-a. Primer
             * <FIELD NAME="{CRepresentative.person}">
             *  <ELEMENT>
             * 	    <FIELD NAME="{CPerson.personName}">Милена Живкова Модева</FIELD>
             * 	    <FIELD NAME="{CPerson.indCompany}">false</FIELD>
             * 	    ....
             * 	</ELEMENT>
             * </FIELD>
             *
             * dokato v novite logove gorniq element izglejda taka
             * <ELEMENT NAME="{CRepresentative.person}">
             * 	    <FIELD NAME="{CPerson.personName}">Милена Живкова Модева</FIELD>
             * 	    <FIELD NAME="{CPerson.indCompany}">false</FIELD>
             *  	....
             * </ELEMENT>
             *
             */
            if (elementHierarchy.children.size() == 0) {
                CLogChangeElement currentCLogChangeElement = new CLogChangeElement(current);
                currentCLogChangeElement.setStringValue(textBuffer == null ? null : textBuffer.toString());
                currentCLogChangeElement.setKey(getCurrentElementKey());
                current.addChild(currentCLogChangeElement);
            }

        } else if ("VALUE".equals(elementName)) {
            current.setKey(getCurrentElementKey());
            current.setStringValue(textBuffer == null ? null : textBuffer.toString());
        } else if ("ELEMENT".equals(elementName)) {
            current = current.getParent();
        }

        elementHierarchy = elementHierarchy.parent == null ? elementHierarchy : elementHierarchy.parent;

    }

    @Override
    public void endDocument() {
        //maha elementite koito imat edin i sy6t child i parent fullKey!!!
        //taka se poluchava che ako imame list, ot elementi, pyrvo se syzdava edin element (primerno s key CPatent>CPatent.technicalData>CTechnicalData.drawingList) i v nego ima child elements
        //sys sy6tiq key. Tozi kod premestva  child elementite da se vyrjta kym parent-a na tekushtiq dokument!

        before.stream().forEach(e -> {
            while (removeDuplicateNodes(e)) {
                log.debug("Before node removed");
            }
        });
        after.stream().forEach(e -> {
            while (removeDuplicateNodes(e)) {
                log.debug("After node removed");
            }
        });
    }
    private boolean removeDuplicateNodes(CLogChangeElement element) {
        if (element == null) {
            return false;
        }
        if (element.hasChildren()) {
            List<CLogChangeElement> children = element.getAllChildren();
            CLogChangeElement parent = element.getParent();
            if (children.stream().allMatch(c -> Objects.equals(element.getFullKey(), c.getFullKey()))) {
                parent.removeChild(element);
                children.forEach(c -> {
                    c.setParent(parent);
                    parent.addChild(c);
                    log.debug("Removed node " + element.getFullKey());

                });
                return true;
            }

            for (CLogChangeElement e : children) {
                if (removeDuplicateNodes(e)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<String> getCurrentElementKey() {
        ElementHierarchy h = elementHierarchy;
        List<String> res = new ArrayList<>();
        while (h != null) {
            if (h.nodeKey != null && !"UNKNOWN".equals(h.nodeKey)) {
                res.add(h.nodeKey);
            }
            h = h.parent;
        }
        Collections.reverse(res);
        return res;
    }
    public List<CLogChangeElement> getBefore() {
        return before;
    }

    public List<CLogChangeElement> getAfter() {
        return after;
    }

    public void characters(char buf[], int offset, int len) throws SAXException {
        String s = new String(buf, offset, len);
        if (textBuffer == null) {
            textBuffer = new StringBuilder(s);
        } else {
            textBuffer.append(s);
        }
    }
}
