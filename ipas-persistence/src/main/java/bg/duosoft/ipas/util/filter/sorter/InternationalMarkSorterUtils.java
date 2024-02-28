package bg.duosoft.ipas.util.filter.sorter;

import java.util.HashMap;
import java.util.Map;

public class InternationalMarkSorterUtils {
    public static final String STATUS = "status";
    public static final String RECEPTION_DATE = "receptionDate";
    public static final String FILING_DATE = "filingDate";
    public static final String FILING_NUMBER = "filingNumber";
    public static final String TITLE = "title";
    public static final String TRANSCATION_TYPE = "transaction_type";
    public static final String REGISTRATION_NUMBER = "registrationNumber";
    public static final String RESPONSIBLE_USER = "responsibleUser";
    public static Map<String, String> sorterColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put(STATUS, "m.STATUS_NAME");
        map.put(RECEPTION_DATE, "m.RECEPTION_DATE");
        map.put(FILING_DATE, "m.FILING_DATE");
        map.put(FILING_NUMBER, "filingNumber");
        map.put(TITLE, "m.TITLE");
        map.put(TRANSCATION_TYPE, "m.transaction_type");
        map.put(REGISTRATION_NUMBER, "m.registration_nbr");
        map.put(RESPONSIBLE_USER, "m.USER_NAME");
        return map;
    }
}
