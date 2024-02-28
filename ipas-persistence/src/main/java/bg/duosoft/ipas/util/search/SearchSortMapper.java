package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.enums.SearchResultColumn;

import java.util.HashMap;
import java.util.Map;

public class SearchSortMapper {

    private static Map<String, String> map;
    static {
        map = new HashMap<>();
        map.put(SearchResultColumn.PK_SORT, SearchUtil.FIELD_FILE_PK_SORT);
        map.put(SearchResultColumn.FILING_DATA_SORT, SearchUtil.FIELD_FILE_FILING_DATA_SORT);
        map.put(SearchResultColumn.REGISTRATION_NBR_SORT, SearchUtil.FIELD_FILE_REGISTRATION_NBR_SORT);
        map.put(SearchResultColumn.MAIN_OWNER_SORT, SearchUtil.FIELD_MAIN_OWNER_IP_PERSON_PERSON_NAME_SORT);
        map.put(SearchResultColumn.SERVICE_PERSON_SORT, SearchUtil.FIELD_SERVICE_PERSON_IP_PERSON_PERSON_NAME_SORT);
        map.put(SearchResultColumn.TITLE_SORT, SearchUtil.FIELD_FILE_TITLE_SORT);
        map.put(SearchResultColumn.PERSON_NAME_SORT, SearchUtil.FIELD_IP_PERSON_PERSON_NAME_SORT);
        map.put(SearchResultColumn.DOC_PK_SORT, SearchUtil.FIELD_PK_SORT);
        map.put(SearchResultColumn.DOC_FILING_DATA_SORT, SearchUtil.FIELD_IP_DOC_FILING_DATE_SORT);
        map.put(SearchResultColumn.DOC_NUMBER, SearchUtil.FIELD_IP_DOC_EXTERNAL_SYSTEM_ID);
    }

    public static String toSearchSortField(String searchResultColumn) {
        return map.get(searchResultColumn);
    }
}
