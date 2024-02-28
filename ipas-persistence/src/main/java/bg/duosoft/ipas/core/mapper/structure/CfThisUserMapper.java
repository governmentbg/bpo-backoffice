package bg.duosoft.ipas.core.mapper.structure;

import bg.duosoft.ipas.persistence.model.entity.user.CfThisUser;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import org.mapstruct.*;

/**
 * User: ggeorgiev
 * Date: 25.7.2019 г.
 * Time: 14:49
 */

@Mapper(componentModel = "spring")
public abstract class CfThisUserMapper {
    @Mapping(target = "rowVersion", source = "rowVersion")
    @Mapping(target = "userId", source = "userId")
    @BeanMapping(ignoreByDefault = true)
    public abstract CfThisUser ipUserToCfThisUser(IpUser ipUser);

    @AfterMapping
    protected void afterMapping(@MappingTarget CfThisUser target, IpUser source) {
        target.setUsername(source.getLogin() + " - " + source.getUserName());
    }

}
