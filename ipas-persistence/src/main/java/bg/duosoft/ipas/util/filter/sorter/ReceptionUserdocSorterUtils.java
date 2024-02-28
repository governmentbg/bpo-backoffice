package bg.duosoft.ipas.util.filter.sorter;

import java.util.HashMap;
import java.util.Map;

public class ReceptionUserdocSorterUtils {

    //Reception table sort information
    public static final String RECEPTION_USERDOC_FILING_DATE = "filingDate";
    public static final String RECEPTION_USERDOC_DOCUMENT_ID = "documentId";
    public static final String RECEPTION_USERDOC_SUBMISSION_TYPE = "submissionType";
    public static final String RECEPTION_USERDOC_ORIGINAL_EXPECTED = "originalExpected";
    public static final String RECEPTION_USERDOC_EXTERNAL_SYSTEM_ID = "registrationNumber";
    public static final String RECEPTION_USERDOC_TYPE = "userdocType";
    public static final String RECEPTION_USERDOC_CREATE_DATE = "createDate";

    public static Map<String, String> sorterColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put(RECEPTION_USERDOC_FILING_DATE, "r.FILING_DATE");
        map.put(RECEPTION_USERDOC_DOCUMENT_ID, "r.DOC_ORI,r.DOC_LOG,r.DOC_SER,r.DOC_NBR");
        map.put(RECEPTION_USERDOC_SUBMISSION_TYPE, "st.NAME");
        map.put(RECEPTION_USERDOC_ORIGINAL_EXPECTED, "r.ORIGINAL_EXPECTED");
        map.put(RECEPTION_USERDOC_EXTERNAL_SYSTEM_ID, "d.EXTERNAL_SYSTEM_ID");
        map.put(RECEPTION_USERDOC_TYPE, "ut.USERDOC_NAME");
        map.put(RECEPTION_USERDOC_CREATE_DATE, "r.CREATE_DATE");
        return map;
    }

}
