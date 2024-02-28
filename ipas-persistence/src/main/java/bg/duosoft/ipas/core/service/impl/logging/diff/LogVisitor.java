package bg.duosoft.ipas.core.service.impl.logging.diff;

import bg.duosoft.ipas.util.date.DateUtils;
import de.danielbechler.diff.introspection.ObjectDiffProperty;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;
import de.danielbechler.diff.selector.ElementSelector;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 29.1.2019 г.
 * Time: 15:34
 */
public class LogVisitor implements DiffNode.Visitor {
    private Object left;
    private Object right;
    private StringBuilder result = new StringBuilder();
    private List<ElementSelector> openedNodes = new ArrayList<>();
    private static Logger log = LoggerFactory.getLogger(LogVisitor.class);
//    private static Set<DiffNode.State> STATES_TOBE_PROCESSED = new HashSet<>(Arrays.asList(DiffNode.State.CHANGED, DiffNode.State.ADDED, DiffNode.State.REMOVED));//TODO:Processing CIRCULAR/INACCESSIBLE states???
    public LogVisitor(Object left, Object right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void node(DiffNode node, Visit visit) {
        if (/*node.isRootNode() ||*/ node.getState() == DiffNode.State.UNTOUCHED) {//the compared elements have no differences
            return;
        }

        Object leftObject = node.canonicalGet(left);
        Object rightObject = node.canonicalGet(right);
        openNode(node);



        if (Collection.class.isAssignableFrom(node.getValueType())) {
            //process collection
            CollectionVisitor collectionVisitor = new CollectionVisitor(left, right);
            node.visitChildren(collectionVisitor);
            visit.dontGoDeeper();//nqma da se hodi navytre, zashtoto CollectionVisitor-a shte svyrshi taq rabota!
            result.append(collectionVisitor.getResult());
        } else if (!node.hasChildren()) {
            result.append("<ITEM>\n\t<BEFORE>\n\t\t" + printValue(leftObject, node.getValueType()) + "\n\t</BEFORE>\n\t<AFTER>\n\t\t" + printValue(rightObject, node.getValueType()) + "\n\t</AFTER>\n</ITEM>\n");
        } else {
            ObjectDiffProperty objectDiffProperty = node.getPropertyAnnotation(ObjectDiffProperty.class);
            if (objectDiffProperty != null && !StringUtils.isEmpty(objectDiffProperty.equalsOnlyValueProviderMethod())) {
                String method = objectDiffProperty.equalsOnlyValueProviderMethod();

                try {
                    Object _old = leftObject == null ? null : leftObject.getClass().getMethod(method).invoke(leftObject);
                    Object _new = rightObject == null ? null : rightObject.getClass().getMethod(method).invoke(rightObject);
                    result.append("<ITEM>\n\t<BEFORE>\n\t\t" + printValue(_old, String.class) + "\n\t</BEFORE>\n\t<AFTER>\n\t\t" + printValue(_new, String.class) + "\n\t</AFTER>\n</ITEM>\n");
                    visit.dontGoDeeper();
                } catch (Exception e) {
                    throw new RuntimeException("Logging - cannot invoke method " + method + " of object " + (leftObject == null ? rightObject.getClass().toString() : left.getClass().toString()), e);
                }
            }
        }

//        String path = node.getPath().getElementSelectors().stream().map(s -> s.toHumanReadableString()).collect(Collectors.joining("/"));
//        log.debug(String.format("Path: >%s<, State: %s, ValueType:%s, PropertyName:%s, IsRoot:%s, HasChildren:%s\n >%s<\n >%s<", path, node.getState(), node.getValueType(), node.getPropertyName(), node.hasChildren(), node.isRootNode(), leftObject, rightObject));
    }

    static String printField(DiffNode node, Object object, Class<?> valueType) {
        StringBuilder res = new StringBuilder("<FIELD NAME=\"{" + node.getParentNode().getValueType().getSimpleName() + "." + node.getElementSelector().toString() + "}\"");
        if (object == null) {
            res.append(" />");
        } else {
            res.append(">" + printObject(object, valueType) + "</FIELD>");
        }
        res.append("\n");
        return res.toString();
    }
    static String printValue(Object object, Class<?> valueType) {
        StringBuilder res = new StringBuilder("<VALUE");
        if (object == null) {
            res.append(" />");
        } else {
            res.append(">" + printObject(object, valueType) + "</VALUE>");
        }
        return res.toString();
    }

    //if the object toBePrinted is byte array, then the length of the byteArray is getting printed!
    private static String printObject(Object object, Class<?> valueType) {
        if (byte[].class.isAssignableFrom(valueType)) {
            int length;
            if (object == null) {
                length = 0;
            } else {
                length = ((byte[])object).length;
            }
            return length + " bytes";
        } else if (java.util.Date.class.isAssignableFrom(valueType)) {
            return DateUtils.formatIpasDateTime((java.util.Date)object);
        } else if (java.util.Calendar.class.isAssignableFrom(valueType)) {
            return DateUtils.formatIpasDateTime(object == null ? null : ((java.util.Calendar)object).getTime());
        } else if (LocalDateTime.class.isAssignableFrom(valueType)) {
            return DateUtils.formatIpasDateTime((LocalDateTime)object);
        } else if (LocalDate.class.isAssignableFrom(valueType)) {
            return DateUtils.formatIpasDate((LocalDate)object);
        } else {
            return object == null ? null : object.toString();
        }
    }

    private void openNode(DiffNode node) {
        int cnt = 0;
        List<ElementSelector> selectors = node.getPath().getElementSelectors().stream().collect(Collectors.toList());
        while (openedNodes.size() > cnt && selectors.get(cnt).equals(openedNodes.get(cnt))) {
            cnt++;
        }

        if (openedNodes.size() > cnt) {//these nodes should be closed, because they do not have the same root
            closeOpenedNodes(cnt);
        }


        for (int i = cnt; i < selectors.size(); i++) {//these nodes should be opened!
            openedNodes.add(selectors.get(i));
        }
//        log.debug(node + "..." + node.isRootNode());

        result.append("<DIFF FIELD=\"" + (node.isRootNode() ? node.getValueType().getSimpleName() : "{" + node.getParentNode().getValueType().getSimpleName() + "." + node.getElementSelector().toString()+ "}") + "\">\n");
    }
    public String getResult() {
        //closing the non-closed nodes!
        closeOpenedNodes(0);
        return result.toString();
    }

    /**
     * ako v opened nodes ima first,  second,  last
     *  - ako lastNodeNbrToClose == 0, togava shte se zatvorqt last, second, first
     *  - ako lastNodeNbrToClose == 1, shte se zatvorqt samo last, second
     *  - ako lastNodeNbrToClose == 2, shte se zatvori samo last!
     * @param lastNodeNbrToClose
     */
    private void closeOpenedNodes(int lastNodeNbrToClose) {
        List<ElementSelector> temp = new ArrayList<>(openedNodes);
        for (int i = openedNodes.size() - 1; i >= lastNodeNbrToClose; i--) {
            result.append("</DIFF>\n");
            temp.remove(temp.size() - 1);
        }
        openedNodes = temp;

    }
}
