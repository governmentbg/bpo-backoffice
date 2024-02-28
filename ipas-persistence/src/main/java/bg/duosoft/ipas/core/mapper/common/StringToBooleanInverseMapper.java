package bg.duosoft.ipas.core.mapper.common;

import bg.duosoft.ipas.core.mapper.MapperHelper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class StringToBooleanInverseMapper {
    public String booleanToString(Boolean b) {
        return MapperHelper.getBooleanAsText(b == null ? null : !b);
    }

    public Boolean textToBoolean(String text) {
        Boolean res = MapperHelper.getTextAsBoolean(text);
        return res == null ? null : !res;
    }
}
