package bg.duosoft.ipas.core.mapper.miscellaneous;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StringToBooleanMapper.class})
public abstract class SimpleUserMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "userName", target = "userName")
    @Mapping(source = "indInactive", target = "indInactive")
    public abstract CUser toCore(IpUser ipUser);

    public abstract List<CUser> toCoreList(List<IpUser> ipUsers);


    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract IpUser toEntity(CUser cUser);

    @InheritInverseConfiguration
    public abstract List<IpUser> toEntityList(List<CUser> cUsers);
}
