package bg.duosoft.ipas.core.mapper.common;

import bg.duosoft.ipas.core.mapper.MapperHelper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class IntegerToBooleanMapper {
    public Integer booleanToNumber(Boolean b) {
        return MapperHelper.getBooleanAsNumber(b);
    }

    public Boolean numberToBoolean(Integer integer) {
        return MapperHelper.getNumberAsBoolean(integer);
    }
}
