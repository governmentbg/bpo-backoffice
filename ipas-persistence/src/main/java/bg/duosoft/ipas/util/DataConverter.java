package bg.duosoft.ipas.util;

/**
 * User: Georgi
 * Date: 17.12.2019 г.
 * Time: 14:12
 */
public class DataConverter {
    public static final Integer parseInteger(String value, Integer defaultValue) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
