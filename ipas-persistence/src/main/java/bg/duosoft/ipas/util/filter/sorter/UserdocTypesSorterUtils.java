package bg.duosoft.ipas.util.filter.sorter;

import java.util.HashMap;
import java.util.Map;

public class UserdocTypesSorterUtils {

    public static final String USERDOC_NAME = "userdocName";
    public static final String USERDOC_GROUP_NAME = "userdocGroupName";
    public static final String STATUS = "userdocStatus";

    public static Map<String, String> userdocTypesSorterColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put(USERDOC_NAME, "ut.userdocName");
        map.put(USERDOC_GROUP_NAME, "ut.userdocGroupName");
        map.put(STATUS, "ut.indInactive");
        return map;
    }
}
