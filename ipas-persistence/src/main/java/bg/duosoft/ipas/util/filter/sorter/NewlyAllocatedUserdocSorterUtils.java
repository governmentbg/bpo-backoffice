package bg.duosoft.ipas.util.filter.sorter;

import java.util.HashMap;
import java.util.Map;

public class NewlyAllocatedUserdocSorterUtils {

    public static final String USERDOC_FILING_DATE = "filingDate";
    public static final String USERDOC_EXTERNAL_SYSTEM_ID = "registrationNumber";
    public static final String USERDOC_TYPE_NAME = "userdocTypeName";
    public static final String USERDOC_DATE_CHANGED = "dateChanged";
    public static final String USER_NAME =  "userName";

    public static Map<String, String> sorterColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put(USERDOC_FILING_DATE, "d.FILING_DATE");
        map.put(USERDOC_EXTERNAL_SYSTEM_ID, "d.EXTERNAL_SYSTEM_ID,d.DOC_SER,d.DOC_NBR");
        map.put(USERDOC_TYPE_NAME, "ut.USERDOC_NAME");
        map.put(USERDOC_DATE_CHANGED, "puc.DATE_CHANGED");
        map.put(USER_NAME, "us.USER_NAME");
        return map;
    }
}
