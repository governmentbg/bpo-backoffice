package bg.duosoft.ipas.core.mapper.common;

import bg.duosoft.ipas.core.mapper.MapperHelper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class StringToBooleanMapper {
    public String booleanToString(Boolean b) {
        return MapperHelper.getBooleanAsText(b);
    }

    public Boolean textToBoolean(String text) {
        return MapperHelper.getTextAsBoolean(text);
    }
}
