package bg.duosoft.ipas.core.mapper.userdoc.userdoc_type;

import bg.duosoft.ipas.enums.UserdocGroup;
import org.mapstruct.Mapper;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Mapper(componentModel = "spring")
public class UserdocGroupToStringMapper {

    public UserdocGroup toCore(String userdocGroupName) {
        if (!StringUtils.isEmpty(userdocGroupName)) {
            UserdocGroup userdocGroup = UserdocGroup.selectByCode(userdocGroupName);
            if (Objects.nonNull(userdocGroup))
                return userdocGroup;
        }
        return null;
    }

    public String toEntity(UserdocGroup userdocGroup) {
        return userdocGroup.code();
    }

}
