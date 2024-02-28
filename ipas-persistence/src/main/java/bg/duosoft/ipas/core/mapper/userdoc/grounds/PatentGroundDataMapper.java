package bg.duosoft.ipas.core.mapper.userdoc.grounds;

import bg.duosoft.ipas.core.model.userdoc.grounds.CPatentGroundData;
import bg.duosoft.ipas.persistence.model.entity.userdoc.grounds.IpPatentGroundData;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class PatentGroundDataMapper {
    @Mapping(target = "partialInvalidity",  source = "partialInvalidity")
    @BeanMapping(ignoreByDefault = true)
    public abstract CPatentGroundData toCore(IpPatentGroundData patentGroundData);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract IpPatentGroundData toEntity(CPatentGroundData patentGroundData);
}
