package bg.duosoft.ipas.util.error_log;

import bg.duosoft.ipas.enums.ErrorLogAbout;
import bg.duosoft.ipas.enums.ErrorLogPriority;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.LinkedHashMap;

public class ErrorLogUtils {

    public static LinkedHashMap<Boolean, String> generateResolvedSelectOptions(MessageSource messageSource) {
        LinkedHashMap<Boolean, String> map = new LinkedHashMap<>();
        map.put(true, messageSource.getMessage("yes", null, LocaleContextHolder.getLocale()));
        map.put(false, messageSource.getMessage("no", null, LocaleContextHolder.getLocale()));
        return map;
    }

    public static LinkedHashMap<String, String> generatePrioritySelectOptions(MessageSource messageSource) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(ErrorLogPriority.HIGH.getValue(), messageSource.getMessage("error.log.priority.filter.HIGH", null, LocaleContextHolder.getLocale()));
        map.put(ErrorLogPriority.MEDIUM.getValue(), messageSource.getMessage("error.log.priority.filter.MEDIUM", null, LocaleContextHolder.getLocale()));
        map.put(ErrorLogPriority.LOW.getValue(), messageSource.getMessage("error.log.priority.filter.LOW", null, LocaleContextHolder.getLocale()));
        return map;
    }

    public static LinkedHashMap<String, String> generateAboutSelectOptions(MessageSource messageSource) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(ErrorLogAbout.ABDOCS.getValue(), messageSource.getMessage("error.log.about.filter.ABDOCS", null, LocaleContextHolder.getLocale()));
        map.put(ErrorLogAbout.IPAS.getValue(), messageSource.getMessage("error.log.about.filter.IPAS", null, LocaleContextHolder.getLocale()));
        return map;
    }

}
