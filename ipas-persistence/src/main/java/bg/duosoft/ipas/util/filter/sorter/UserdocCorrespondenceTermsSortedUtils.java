package bg.duosoft.ipas.util.filter.sorter;

import java.util.HashMap;
import java.util.Map;

public class UserdocCorrespondenceTermsSortedUtils {
    public static final String EXPIRATION_DATE = "expirationDate";
    public static final String USERDOC_TYPE = "userdocType";
    public static final String REGISTRATION_NUMBER = "registrationNumber";
    public static final String USER_NAME =  "userName";
    public static final String STATUS_NAME =  "statusName";
    public static Map<String, String> sorterColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put(EXPIRATION_DATE, "expirationDate");
        map.put(USERDOC_TYPE, "USERDOC_NAME");
        map.put(USER_NAME, "userName");
        map.put(STATUS_NAME, "statusName");
        map.put(REGISTRATION_NUMBER, "EXTERNAL_SYSTEM_ID, DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR");
        return map;
    }
}
