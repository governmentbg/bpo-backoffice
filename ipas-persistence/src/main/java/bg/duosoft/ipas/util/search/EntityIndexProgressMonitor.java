package bg.duosoft.ipas.util.search;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.batchindexing.MassIndexerProgressMonitor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * User: ggeorgiev
 * Date: 12.03.2021
 * Time: 12:37
 */
@Getter
@Slf4j
public class EntityIndexProgressMonitor implements MassIndexerProgressMonitor {
//    private static final Log log = LoggerFactory.make(MethodHandles.lookup());
    private final Class<?> entityType;
    private boolean running;
    private final AtomicLong documentsDoneCounter;
    private final LongAdder totalCounter;
    private volatile long startTime;
    private String error;
    private final int logAfterNumberOfDocuments;

    public EntityIndexProgressMonitor(Class<?> entityType) {
        this.entityType = entityType;
        this.running = true;
        this.documentsDoneCounter = new AtomicLong();
        this.totalCounter = new LongAdder();
        this.logAfterNumberOfDocuments = 10_000;
    }

    @Override
    public void documentsAdded(long increment) {
        long current = this.documentsDoneCounter.addAndGet(increment);
        if (current == increment) {
            this.startTime = System.nanoTime();
        }

        if (current % (long)this.getStatusMessagePeriod() == 0L) {
            this.printStatusMessage(this.startTime, this.totalCounter.longValue(), current);
        }
    }
    protected int getStatusMessagePeriod() {
        return this.logAfterNumberOfDocuments;
    }

    protected void printStatusMessage(long startTime, long totalTodoCount, long doneCount) {
        long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        log.info(String.format("%s - %d documents indexed in %d ms", entityType.getName(), doneCount, elapsedMs));
        float estimateSpeed = (float)doneCount * 1000.0F / (float)elapsedMs;
        float estimatePercentileComplete = (float)doneCount * 100.0F / (float)totalTodoCount;
        log.info(String.format("%s - Indexing speed: %f documents/second; progress: %.2f%%", entityType.getName(), estimateSpeed, estimatePercentileComplete));
    }

    @Override
    public void indexingCompleted() {
        log.info(String.format("%s - Reindexed %d entities", entityType.getName(), this.totalCounter.longValue()));
        running = false;
    }

    @Override
    public void documentsBuilt(int i) {

    }

    @Override
    public void entitiesLoaded(int i) {

    }

    public void addToTotalCount(long count) {
        this.totalCounter.add(count);
        log.info(String.format("%s - Going to reindex %d entities", entityType.getName(), count));
    }
    public void setError(String error) {
        this.error = error;
    }
    public float getPercentsCompleted() {
        return totalCounter.longValue() == 0 ? 0 : documentsDoneCounter.get() * 100f / totalCounter.longValue();
    }

}
