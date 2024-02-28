package bg.duosoft.ipas.core.mapper.acp;

import bg.duosoft.ipas.core.model.acp.CAcpDetails;
import bg.duosoft.ipas.persistence.model.entity.ext.acp.AcpDetails;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public abstract class AcpDetailsMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "affectedObjectOthers",  source = "affectedObjectOthers")
    public abstract CAcpDetails toCore(AcpDetails acpDetails);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract AcpDetails toEntity(CAcpDetails acpDetails);
}
