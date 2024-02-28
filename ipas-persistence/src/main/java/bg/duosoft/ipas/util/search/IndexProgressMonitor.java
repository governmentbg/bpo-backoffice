package bg.duosoft.ipas.util.search;

import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 12.03.2021
 * Time: 12:20
 */
public class IndexProgressMonitor {
    private static IndexProgressMonitor indexProgressMonitor;
    private List<String> globalErrors = new ArrayList<>();
    private IndexProgressMonitor() {

    }
    private Map<Class<?>, EntityIndexProgressMonitor> monitors = new HashMap<>();

    public static synchronized IndexProgressMonitor getCurrentIndexProgressMonitor() {
        return indexProgressMonitor == null ? initIndexProgressMonitor() : indexProgressMonitor;
    }
    public static synchronized IndexProgressMonitor initIndexProgressMonitor() {
        if (indexProgressMonitor == null || !indexProgressMonitor.isRunning()) {
            indexProgressMonitor = new IndexProgressMonitor();
            return indexProgressMonitor;
        } else {
            throw new RuntimeException("Cannot create new progress monitor. It's running!!!!");
        }
    }
    public EntityIndexProgressMonitor addProgressMonitor(Class<?> cls) {
        EntityIndexProgressMonitor monitor = new EntityIndexProgressMonitor(cls);
        monitors.put(cls, monitor);
        return monitor;
    }

    public synchronized boolean isRunning() {
        return monitors.values().stream().filter(r -> r.isRunning()).findAny().isPresent();
    }
    public List<EntityIndexProgressMonitor> getEntityProgressMonitors() {
        return monitors.entrySet().stream().sorted(Comparator.comparing(e -> e.getKey().getName())).map(r -> r.getValue()).collect(Collectors.toList());
    }
    public EntityIndexProgressMonitor getEntityProgressMonitor(Class<?> cls) {
        return monitors.get(cls);
    }

    public String getGlobalError() {
        return CollectionUtils.isEmpty(globalErrors) ? null : globalErrors.stream().collect(Collectors.joining("\n"));
    }

    public synchronized void addGlobalError(String globalError) {
        this.globalErrors.add(globalError);
    }
}
