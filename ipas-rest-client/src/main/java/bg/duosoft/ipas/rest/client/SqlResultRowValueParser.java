package bg.duosoft.ipas.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

/**
 * User: Georgi
 * Date: 26.11.2020 г.
 * Time: 12:47
 */
public class SqlResultRowValueParser {
    private static final String UTC_DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    private static ObjectMapper objectMapper;
    private Map<String, Object> row;
    static {
        objectMapper = new ObjectMapper();
        objectMapper.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
    }
    public SqlResultRowValueParser(Map<String, Object> row) {
        this.row = new LinkedCaseInsensitiveMap<>();//transforms the map to case-insenstive one
        this.row.putAll(row);
    }

    /**
     * @param key - case insensitive
     * @param cls - class of the returned value
     * @return value for the specific key from the row.
     */
    public <T> T getValue(String key, Class<T> cls) {
        try {

            List<String> keys = getPossibleKeys(key);
            boolean contains = keys.stream().anyMatch(k -> row.containsKey(k));
            if (!contains) {
                throw new RuntimeException("No column with keys:" + keys);
            }
            Object res = keys.stream().map(k -> row.get(k)).filter(Objects::nonNull).findFirst().orElse(null);
            if (res == null) {
                return null;
            }
            if (cls.equals(Timestamp.class)) {
                return (T) getTimestamp(res.toString());
            }
            if (cls.equals(Date.class)) {
                try {
                    //tries if the date format is a timestamp. If it's not, then it should be milliseconds since unix time
                    return (T) getTimestamp(res.toString());
                } catch (Exception e) {
                    return (T) new Date(Long.parseLong(res.toString()));
                }
            }
            if (cls.isAssignableFrom(res.getClass())) {
                return (T) res;
            }
            return objectMapper.readValue(res.toString(), cls);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private static Timestamp getTimestamp(String val) throws ParseException {
        Date _res = new SimpleDateFormat(UTC_DATE_FORMAT_PATTERN).parse(val);//it turned out that the SimpleDateFormat is not thread safe, so it's created on every parse request!!!!
        return new Timestamp(_res.getTime());
    }
    private static List<String> getPossibleKeys(String key) {
        List<String> keys = new ArrayList<>();
        keys.add(key);
        String k = key.replaceAll("([A-Z])", "_$1").toLowerCase();
        keys.add(k);
        return keys;
    }
}
