package bg.duosoft.ipas.util.filter.sorter;

import java.util.HashMap;
import java.util.Map;

public class OffidocTypeSorterUtils {

    public static final String OFFIDOC_NAME = "offidocName";
    public static final String OFFIDOC_NAME_WFILE = "wFileName";

    public static Map<String, String> offidocTypeSorterColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put(OFFIDOC_NAME, "ot.offidocName");
        map.put(OFFIDOC_NAME_WFILE, "ot.nameWfile");
        return map;
    }
}
