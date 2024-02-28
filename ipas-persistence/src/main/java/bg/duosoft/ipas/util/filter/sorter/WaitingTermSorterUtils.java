package bg.duosoft.ipas.util.filter.sorter;

import java.util.HashMap;
import java.util.Map;

public class WaitingTermSorterUtils {

    public static final String EXPIRATION_DATE = "expirationDate";
    public static final String FILING_NUMBER = "filingNumber";
    public static final String STATUS = "status";
    public static final String USER_NAME =  "userName";

    public static Map<String, String> sorterColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put(EXPIRATION_DATE, "p.EXPIRATION_DATE");
        map.put(FILING_NUMBER, "filingNumber");
        map.put(STATUS, "s.STATUS_NAME");
        map.put(USER_NAME, "us.USER_NAME");
        return map;
    }

}
