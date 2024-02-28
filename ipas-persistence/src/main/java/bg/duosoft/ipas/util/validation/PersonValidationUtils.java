package bg.duosoft.ipas.util.validation;

import java.util.Arrays;
import java.util.List;

public class PersonValidationUtils {
    public static String ADDRESS_STREET = "addressStreet";
    public static String CITY = "city";

    public static List<String> excludeFieldsOnReception() {
        return Arrays.asList(ADDRESS_STREET, CITY);
    }
}
