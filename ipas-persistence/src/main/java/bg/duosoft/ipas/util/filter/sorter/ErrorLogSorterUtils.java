package bg.duosoft.ipas.util.filter.sorter;

import java.util.HashMap;
import java.util.Map;

public class ErrorLogSorterUtils {

    public static final String DATE_CREATED = "createdDate";
    public static final String ABOUT = "about";
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String CUSTOM_MESSAGE = "customMessage";
    public static final String PRIORITY = "priority";
    public static final String ACTION = "action";
    public static final String DATE_RESOLVED = "resolvedDate";

    public static Map<String, String> sorterColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put(DATE_CREATED, "e.dateCreated");
        map.put(ABOUT, "e.about");
        map.put(ERROR_MESSAGE, "e.errorMessage");
        map.put(CUSTOM_MESSAGE, "e.customMessage");
        map.put(PRIORITY, "e.priority");
        map.put(ACTION, "e.action");
        map.put(DATE_RESOLVED, "e.dateResolved");
        return map;
    }

}
