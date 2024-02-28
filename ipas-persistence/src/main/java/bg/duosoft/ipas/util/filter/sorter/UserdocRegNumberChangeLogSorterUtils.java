package bg.duosoft.ipas.util.filter.sorter;

import java.util.HashMap;
import java.util.Map;

public class UserdocRegNumberChangeLogSorterUtils {

    public static final String CHANGE_DATE = "date";
    public static final String FILING_NUMBER = "filingNumber";
    public static final String OLD_REGISTATION_NUMBER = "oldRegNumber";
    public static final String NEW_REGISTRATION_NUMBER = "newRegNumber";
    public static final String RESPONSIBLE_USER = "responsibleUser";

    public static Map<String, String> sorterColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put(CHANGE_DATE, "l.DATE");
        map.put(FILING_NUMBER, "l.DOC_ORI, l.DOC_LOG, l.DOC_SER, l.DOC_NBR");
        map.put(OLD_REGISTATION_NUMBER, "l.OLD_REGISTRATION_NUMBER");
        map.put(NEW_REGISTRATION_NUMBER, "l.NEW_REGISTRATION_NUMBER");
        map.put(RESPONSIBLE_USER, "u.USER_NAME");
        return map;
    }
}
