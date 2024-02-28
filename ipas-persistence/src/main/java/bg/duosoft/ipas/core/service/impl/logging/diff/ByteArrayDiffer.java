package bg.duosoft.ipas.core.service.impl.logging.diff;

import de.danielbechler.diff.NodeQueryService;
import de.danielbechler.diff.access.Instances;
import de.danielbechler.diff.differ.Differ;
import de.danielbechler.diff.differ.DifferDispatcher;
import de.danielbechler.diff.differ.DifferFactory;
import de.danielbechler.diff.node.DiffNode;

import java.util.Arrays;

/**
 * User: ggeorgiev
 * Date: 30.1.2019 г.
 * Time: 13:04
 *
 * https://gist.github.com/SQiShER/dc5d456a9d39dd1c8e60
 */
public class ByteArrayDiffer implements Differ
{
    public boolean accepts(final Class<?> type)
    {
        return type == byte[].class;
    }

    public DiffNode compare(final DiffNode parentNode, final Instances instances)
    {
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
            final byte[] baseValue = instances.getBase(byte[].class);
            final byte[] workingValue = instances.getWorking(byte[].class);
            if (!Arrays.equals(baseValue, workingValue))
            {
                node.setState(DiffNode.State.CHANGED);
            }
        }
        return node;
    }

    public static class Factory implements DifferFactory
    {
        public Differ createDiffer(final DifferDispatcher differDispatcher,
                                   final NodeQueryService nodeQueryService)
        {
            return new ByteArrayDiffer();
        }
    }
}
