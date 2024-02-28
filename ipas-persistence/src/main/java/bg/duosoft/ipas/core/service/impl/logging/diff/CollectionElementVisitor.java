package bg.duosoft.ipas.core.service.impl.logging.diff;

import de.danielbechler.diff.introspection.ObjectDiffProperty;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;
import org.apache.commons.lang.StringUtils;

import java.util.*;

import static bg.duosoft.ipas.core.service.impl.logging.diff.LogVisitor.printField;

/**
 * User: ggeorgiev
 * Date: 23.11.2021
 * Time: 13:49
 */
public class CollectionElementVisitor  implements DiffNode.Visitor{
    private StringBuilder result = new StringBuilder();
    private Object object;
    private List<DiffNode> openedNodes = new ArrayList<>();


    public CollectionElementVisitor(Object object) {
        this.object = object;
    }
    @Override
    public void node(DiffNode node, Visit visit) {

        Object _object = node.canonicalGet(object);
        closeOpenedNodes(node);
        if (Collection.class.isAssignableFrom(node.getValueType())) {
            //process collection
            CollectionElementVisitor collectionVisitor = new CollectionElementVisitor(object);
            node.visitChildren(collectionVisitor);
            visit.dontGoDeeper();//nqma da se hodi navytre, zashtoto CollectionVisitor-a shte svyrshi taq rabota!
            result.append(collectionVisitor.getResult());
        } else {
            if (node.hasChildren()) {
                openNode(node);
                logNodeIfEqualsOnlyValueProviderMethodDefined(node, visit, _object);
            } else {
                result.append(printField(node, _object, node.getValueType()));
            }
        }


    }

    public String getResult() {
        //closing the non-closed nodes!
        closeAllOpenedNodes();
        return result.toString();
    }

    private void openNode(DiffNode node) {
        openedNodes.add(node);
        result.append("<ELEMENT NAME=\"{" + node.getParentNode().getValueType().getSimpleName() + "." + node.getElementSelector().toString() + "}\">\n");
    }

    private void closeOpenedNodes(DiffNode node) {
        DiffNode currentParent = node.getParentNode();
        DiffNode lastOpenedNode = openedNodes.size() == 0 ? null : openedNodes.get(openedNodes.size() - 1);
        if (lastOpenedNode == null) {
            return;
        }
        if (currentParent.equals(lastOpenedNode)) {
            return;
        }
        List<DiffNode> temp = new ArrayList<>(openedNodes);
        for (int i = openedNodes.size() - 1; i >= 0; i--) {
            DiffNode current = openedNodes.get(i);
            if (current.equals(currentParent)) {
                break;
            }
            result.append("</ELEMENT>\n");
            temp.remove(temp.size() - 1);
        }
        openedNodes = temp;
    }
    private void closeAllOpenedNodes() {
        for (DiffNode n : openedNodes) {
            result.append("</ELEMENT>\n");
        }
    }

    /**
     * ako node-a ima definirana anotaciq ObjectDiffProperty s equalsOnlyValueProviderMethod, to se vika tozi method na node-a, vmesto da se generirat vsichki child elementi na node-a
     * primerno v CUserdocExtraData ima definiran CUserdocExtraDataType getType(), na koito ima slojena anotaciq @ObjectDiffProperty(equalsOnlyValueProviderMethod = "getCode")
     * taka shte se napravi element samo s edin tag
     * <ELEMENT>
     *                          <FIELD NAME="{CUserdocExtraData.type}">SERVICE_SCOPE</FIELD> vmesto
     * </ELEMENT>
     * vmesto
     * <ELEMENT>
     * 							<FIELD NAME="{CUserdocExtraDataType.booleanTextFalse}">Част</FIELD>
     * 							<FIELD NAME="{CUserdocExtraDataType.booleanTextTrue}">Всички</FIELD>
     * 							<FIELD NAME="{CUserdocExtraDataType.code}">SERVICE_SCOPE</FIELD>
     * 							<FIELD NAME="{CUserdocExtraDataType.isBoolean}">true</FIELD>
     * 							<FIELD NAME="{CUserdocExtraDataType.isDate}">false</FIELD>
     * 							<FIELD NAME="{CUserdocExtraDataType.isNumber}">false</FIELD>
     * 							<FIELD NAME="{CUserdocExtraDataType.isText}">false</FIELD>
     * 							<FIELD NAME="{CUserdocExtraDataType.title}">Обхват на услугата</FIELD>
     * 						</ELEMENT>
     * @param node
     * @param visit
     * @param leftObject
     */
    private void logNodeIfEqualsOnlyValueProviderMethodDefined(DiffNode node, Visit visit, Object leftObject) {
        ObjectDiffProperty objectDiffProperty = node.getPropertyAnnotation(ObjectDiffProperty.class);
        if (objectDiffProperty != null && !StringUtils.isEmpty(objectDiffProperty.equalsOnlyValueProviderMethod())) {
            String method = objectDiffProperty.equalsOnlyValueProviderMethod();
            try {

                Object _old = leftObject == null ? null : leftObject.getClass().getMethod(method).invoke(leftObject);
                result.append(printField(node, _old == null ? null : _old.toString(), String.class));
                visit.dontGoDeeper();
            } catch (Exception e) {
                throw new RuntimeException("Logging - cannot invoke method " + method + " of object " + leftObject.getClass().toString(), e);
            }

        }

    }
}
