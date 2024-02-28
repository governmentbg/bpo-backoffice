package bg.duosoft.ipas.core.service.impl.logging.diff;

import bg.duosoft.ipas.util.date.DateUtils;
import de.danielbechler.diff.NodeQueryService;
import de.danielbechler.diff.access.Instances;
import de.danielbechler.diff.differ.Differ;
import de.danielbechler.diff.differ.DifferDispatcher;
import de.danielbechler.diff.differ.DifferFactory;
import de.danielbechler.diff.node.DiffNode;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 27.06.2023
 * Time: 14:53
 */
public class DateDiffer implements Differ {
    @Override
    public boolean accepts(Class<?> type) {
        return type == Date.class;
    }

    @Override
    public DiffNode compare(DiffNode parentNode, Instances instances) {
        final DiffNode node = new DiffNode(parentNode, instances.getSourceAccessor());
        if (instances.hasBeenAdded())
        {
            node.setState(DiffNode.State.ADDED);
        }
        else if (instances.hasBeenRemoved())
        {
            node.setState(DiffNode.State.REMOVED);
        }
        else
        {
            Date baseValue = instances.getBase(Date.class);
            Date workingValue = instances.getWorking(Date.class);
            if (!equals(baseValue, workingValue))
            {
                node.setState(DiffNode.State.CHANGED);
            }
        }
        return node;
    }

    private boolean equals(Date d1, Date d2) {
        LocalDateTime ldt1 = DateUtils.convertToLocalDatTime(d1);
        LocalDateTime ldt2 = DateUtils.convertToLocalDatTime(d2);
        return ldt1.withNano(0).equals(ldt2.withNano(0));
    }
    public static class Factory implements DifferFactory
    {
        public Differ createDiffer(final DifferDispatcher differDispatcher,
                                   final NodeQueryService nodeQueryService)
        {
            return new DateDiffer();
        }
    }
}
