package bg.duosoft.ipas.util.filter.sorter;

import java.util.HashMap;
import java.util.Map;

public class LastActionSortedUtils {

    public static final String USER_NAME =  "userName";

    public static Map<String, String> sorterColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put(USER_NAME, "us.USER_NAME");
        return map;
    }
}
