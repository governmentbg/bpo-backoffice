package bg.duosoft.ipas.util.filter.sorter;

import java.util.HashMap;
import java.util.Map;

public class ExpiredTermSortedUtils {
    public static final String ACTION_DATE = "actionDate";
    public static final String ACTION_TYPE_NAME = "actionTypeName";
    public static final String FILING_NUMBER = "filingNumber";
    public static final String USER_NAME =  "userName";
    public static final String STATUS_NAME =  "statusName";

    public static Map<String, String> sorterColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put(ACTION_DATE, "a.ACTION_DATE");
        map.put(ACTION_TYPE_NAME, "cfa.ACTION_TYPE_NAME");
        map.put(FILING_NUMBER, "filingNumber");
        map.put(USER_NAME, "us.USER_NAME");
        map.put(STATUS_NAME, "cs.STATUS_NAME");
        return map;
    }
}
