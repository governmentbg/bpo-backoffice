package bg.duosoft.ipas.core.service.impl.logging.diff;

import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;

import java.util.Arrays;


/**
 * User: ggeorgiev
 * Date: 29.1.2019 г.
 * Time: 17:42
 */
class CollectionVisitor implements DiffNode.Visitor {
    boolean beforeElementOpened = false;
    boolean afterElementOpened = false;
    private Object left;
    private Object right;
    private StringBuilder before = new StringBuilder();
    private StringBuilder after = new StringBuilder();

    public CollectionVisitor(Object left, Object right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void node(DiffNode node, Visit visit) {
        
        closeBeforeElementIfOpened();
        closeAfterElementIfOpened();

        //ako e osnoven element (primerno NiceClass, a ne fieldovete na NiceClass-a), togava ako statusa e changed/removed, tova oznachava che imame star (before) element, ako e changed/added, togava imame nov (after) element....
        if (Arrays.asList(DiffNode.State.CHANGED, DiffNode.State.UNTOUCHED, DiffNode.State.REMOVED).contains(node.getState())) {
            openBeforeElement(node);

            CollectionElementVisitor leftVisitor = new CollectionElementVisitor(left);
            node.visitChildren(leftVisitor);
            visit.dontGoDeeper();//nqma da se hodi navytre
            before.append(leftVisitor.getResult());

        }
        if (Arrays.asList(DiffNode.State.CHANGED, DiffNode.State.UNTOUCHED, DiffNode.State.ADDED).contains(node.getState())) {
            openAfterElement(node);

            CollectionElementVisitor rightVisitor = new CollectionElementVisitor(right);
            node.visitChildren(rightVisitor);
            visit.dontGoDeeper();//nqma da se hodi navytre
            after.append(rightVisitor.getResult());
        }

    }

    private StringBuilder getBefore() {
        closeBeforeElementIfOpened();
        return before;
    }

    private StringBuilder getAfter() {
        closeAfterElementIfOpened();
        return after;
    }

    private void openBeforeElement(DiffNode node) {
        before.append("<ELEMENT>\n");
        beforeElementOpened = true;
    }

    private void openAfterElement(DiffNode node) {
        after.append("<ELEMENT>\n");
        afterElementOpened = true;
    }

    private void closeBeforeElementIfOpened() {
        if (beforeElementOpened) {
            beforeElementOpened = false;
            before.append("</ELEMENT>\n");
        }
    }

    private void closeAfterElementIfOpened() {
        if (afterElementOpened) {
            afterElementOpened = false;
            after.append("</ELEMENT>\n");
        }
    }


    public String getResult() {
        StringBuilder result = new StringBuilder();
        result.append("<ITEM>\n<BEFORE>\n<ELEMENT>\n<LIST>");
        result.append(getBefore());
        result.append("</LIST>\n</ELEMENT>\n</BEFORE>\n");
        result.append("<AFTER>\n<ELEMENT>\n<LIST>");
        result.append(getAfter());
        result.append("</LIST>\n</ELEMENT>\n</AFTER>\n</ITEM>\n");
        return result.toString();
    }
}
