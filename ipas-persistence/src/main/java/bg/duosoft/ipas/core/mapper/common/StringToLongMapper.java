package bg.duosoft.ipas.core.mapper.common;

import org.mapstruct.Mapper;
import org.springframework.util.StringUtils;
@Mapper(componentModel = "spring")
public class StringToLongMapper {
    public Long stringToLong(String string) {
        return StringUtils.isEmpty(string)? null : Long.parseLong( string );
    }
}
