package bg.duosoft.ipas.core.service.impl.logging.diff;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import org.springframework.util.StringUtils;

/**
 * User: ggeorgiev
 * Date: 30.1.2019 г.
 * Time: 12:29
 */
public class DiffGenerator {
    private String result;

    private DiffGenerator(Object original, Object changed) {
        DiffNode diff = ObjectDifferBuilder.startBuilding()
                .differs().register(new ByteArrayDiffer.Factory())
                .differs().register(new DateDiffer.Factory())
                .filtering().returnNodesWithState(DiffNode.State.UNTOUCHED).returnSubNodesWithState(DiffNode.State.UNTOUCHED)
                .and()
                .build()
                .compare(changed, original);
        LogVisitor visitor = new LogVisitor(original, changed);
        diff.visit(visitor);
        result = visitor.getResult();
    }

    public static DiffGenerator create(Object original, Object changed) {
        return new DiffGenerator(original, changed);
    }

    public String getResult() {
        return result;
    }

    public boolean isChanged() {
        return !StringUtils.isEmpty(result);
    }
}
