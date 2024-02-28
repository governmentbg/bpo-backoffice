package bg.duosoft.ipas.util.offidoc_type;

import bg.duosoft.ipas.enums.OffidocDirection;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.LinkedHashMap;

public class OffidocTypeUtils {

    public static LinkedHashMap<String, String> generateOffidocDirectionSelectOptions(MessageSource messageSource) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        for (OffidocDirection offidocDirection : OffidocDirection.values()) {
            map.put(offidocDirection.getValue(), messageSource.getMessage("offidoc.direction." + offidocDirection.name(), null, offidocDirection.name(), LocaleContextHolder.getLocale()));
        }
        return map;
    }
}
