package bg.duosoft.ipas.core.mapper.logging;

import bg.duosoft.ipas.core.model.logging.CLogChangeElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * User: ggeorgiev
 * Date: 03.12.2021
 * Time: 14:44
 */
@Component
public class LoggingMessagesMapper {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private Map<String, String> loggingMessagesConfiguration;

    public String generateLoggingMessageContent(CLogChangeElement element) {
        StringBuilder res = new StringBuilder();
        generateContent(res, element, 0);
        return res.toString();
    }
    private Pattern CONFIG_KEY = Pattern.compile("\\{(.*?)\\}");
    private void generateContent(StringBuilder res, CLogChangeElement current, int level) {
        if (current == null) {
            return;
        }
        if (!current.hasChildren()) {
            _addValue(res, current.getKey(), current.getStringValue(), level);

        } else {
            String loggingMessagesConfig = loggingMessagesConfiguration.get(current.getKey());
            if (loggingMessagesConfig != null ) {
                //v konfiguraciqta moje da ima {CDrawing.drawingNbr|CDrawingExt.drawingNbr}. Syzdava se edin map kojto ima
                // key = CDrawing.drawingNbr|CDrawingExt.drawingNbr i value list ot CDrawing.drawingNbr i CDrawingExt.drawingNbr
                //sled koeto se vyrti po tozi map i za vseki key se tyrsi za child elementi s nqkoe ot value-tata (v sluchaq CDrawing.drawingNbr ili CDrawingExt.drawingNbr)
                Matcher keysMatcher = CONFIG_KEY.matcher(loggingMessagesConfig);
                Map<String, List<String>> keys = new HashMap<>();
                while (keysMatcher.find()) {
                    String key = keysMatcher.group(1);
                    keys.put(key, Arrays.stream(key.split("\\|")).collect(Collectors.toList()));
                }
                AtomicReference<String> value = new AtomicReference<>(loggingMessagesConfig);
                keys
                        .entrySet()
                        .stream()
                        .forEach(k -> value.set(value.get().replaceAll("\\{" + k.getKey().replaceAll("\\|", "\\\\|") + "\\}", _getValueByKey(current, k.getValue()))));

                _addValue(res, current.getKey(), value.get(), level);

            } else {
                current.getAllChildren().forEach(c -> generateContent(res, c, level + 1));
            }

        }
    }
    private String _getValueByKey(CLogChangeElement el, List<String> keys) {
       return keys
                .stream()
                .map(k -> el.getChildrenByKey(k))
                .filter(Objects::nonNull)
                .map(e -> e.stream().map(r -> r.getStringValue()).collect(Collectors.joining(",")))
                .findFirst()
                .orElse(messageSource.getMessage("empty.value", null, LocaleContextHolder.getLocale()));
    }
    private void _addValue(StringBuilder res, String key, String value, int level) {
        IntStream.range(0, level).forEach(i -> res.append("\t"));
        res.append(messageSource.getMessage(key, null, LocaleContextHolder.getLocale()) + ":");
        res.append("\t");
        res.append(value == null ? messageSource.getMessage("empty.value", null, LocaleContextHolder.getLocale()) : value);
        res.append("\n<br />");
    }
}
