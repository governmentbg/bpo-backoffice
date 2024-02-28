package bg.duosoft.ipas.util.filter.sorter;

import java.util.HashMap;
import java.util.Map;

public class ReceptionSorterUtils {

    //Reception table sort information
    public static final String RECEPTION_FILING_DATE = "filingDate";
    public static final String RECEPTION_FILING_NUMBER = "filingNumber";
    public static final String RECEPTION_SUBMISSION_TYPE = "submissionType";
    public static final String RECEPTION_ORIGINAL_EXPECTED = "originalExpected";
    public static final String RECEPTION_CREATE_DATE = "createDate";

    public static Map<String, String> receptionSorterColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put(RECEPTION_FILING_DATE, "r.FILING_DATE");
        map.put(RECEPTION_FILING_NUMBER, "r.FILE_SEQ,r.FILE_TYP,r.FILE_SER,r.FILE_NBR");
        map.put(RECEPTION_SUBMISSION_TYPE, "r.SUBMISSION_TYPE");
        map.put(RECEPTION_ORIGINAL_EXPECTED, "r.ORIGINAL_EXPECTED");
        map.put(RECEPTION_CREATE_DATE, "r.CREATE_DATE");
        return map;
    }

}
