package bg.duosoft.ipas.util.parser;

public class IntegerUtils {

    public static Integer tryParseInt(String value, Integer defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
