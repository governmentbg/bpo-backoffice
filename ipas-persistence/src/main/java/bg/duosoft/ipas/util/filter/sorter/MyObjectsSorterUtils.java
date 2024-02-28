package bg.duosoft.ipas.util.filter.sorter;

import java.util.HashMap;
import java.util.Map;

public class MyObjectsSorterUtils {

    public static final String STATUS_DATE = "statusDate";
    public static final String STATUS = "status";
    public static final String FILING_NUMBER = "filingNumber";
    public static final String USER_NAME =  "userName";
    public static final String BORDERO = "bordero";
    public static final String JOURNAL_CODE = "journalCode";
    public static Map<String, String> sorterColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put(STATUS_DATE, "prc.STATUS_DATE");
        map.put(STATUS, "cfs.STATUS_NAME");
        map.put(USER_NAME, "us.USER_NAME");
        map.put(BORDERO, "e.gazno");
        map.put(JOURNAL_CODE, "a.journal_code");
        map.put(FILING_NUMBER, "prc.FILE_SEQ,prc.FILE_TYP,prc.FILE_SER,prc.FILE_NBR");
        return map;
    }

}
