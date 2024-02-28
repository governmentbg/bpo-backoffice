package bg.duosoft.ipas.util.search;

import org.hibernate.search.bridge.StringBridge;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: ggeorgiev
 * Date: 23.08.2021
 * Time: 13:31
 */
public class IntregnBridge implements StringBridge {
    private static Pattern PATTERN = Pattern.compile("^(\\d+).*$");
    @Override
    public String objectToString(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            //removing the non digit numbers at the end of the string!!!The intregnumber filed might be like 12345X
            String obj = (String)o;
            if (StringUtils.isEmpty(obj)) {
                return null;
            }
            Matcher m = PATTERN.matcher(obj);
            if (m.find()) {
                return m.group(1);
            } else {
                return null;
            }
        } else {
            return o.toString();
        }
    }
}
