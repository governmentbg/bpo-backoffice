package bg.duosoft.ipas.util.filter.sorter;

import java.util.HashMap;
import java.util.Map;

public class MyUserdocsSorterUtils {

    public static final String USERDOC_TYPE = "userdocType";
    public static final String EXPIRATION_DATE = "expirationDate";
    public static final String FILING_DATE = "filingDate";
    public static final String STATUS_DATE = "statusDate";
    public static final String STATUS = "status";
    public static final String REGISTRATION_NUMBER = "registrationNumber";
    public static final String USER_NAME =  "userName";
    public static Map<String, String> sorterColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put(FILING_DATE, "d.FILING_DATE");
        map.put(EXPIRATION_DATE, "p.EXPIRATION_DATE");
        map.put(USERDOC_TYPE, "udt.USERDOC_NAME");
        map.put(USER_NAME, "us.USER_NAME");
        map.put(STATUS, "s.STATUS_NAME");
        map.put(REGISTRATION_NUMBER, "d.EXTERNAL_SYSTEM_ID, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, p.DOC_NBR");
        map.put(STATUS_DATE, "p.STATUS_DATE");
        return map;
    }

}
