package bg.duosoft.ipas.core.mapper.userdoc.grounds;

import bg.duosoft.ipas.core.model.userdoc.grounds.CGroundNiceClasses;
import bg.duosoft.ipas.persistence.model.entity.userdoc.grounds.IpUserdocGroundsNiceClasses;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class GroundNiceClassesMapper {
    @Mapping(target = "niceClassCode",  source = "pk.niceClassCode")
    @Mapping(target = "niceClassDescription",  source = "niceClassDescription")
    @BeanMapping(ignoreByDefault = true)
    public abstract CGroundNiceClasses toCore(IpUserdocGroundsNiceClasses ipUserdocGroundsNiceClasses);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion", constant = "1")
    public abstract IpUserdocGroundsNiceClasses toEntity(CGroundNiceClasses cGroundNiceClasses);
}
