package bg.duosoft.ipas.core.mapper.acp;

import bg.duosoft.ipas.core.model.acp.CAcpExternalAffectedObject;
import bg.duosoft.ipas.persistence.model.entity.ext.acp.AcpExternalAffectedObject;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {})
public abstract class AcpExternalAffectedObjectMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "externalId", source = "externalId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "registrationNbr", source = "registrationNumber")
    public abstract CAcpExternalAffectedObject toCore(AcpExternalAffectedObject affectedObject);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract AcpExternalAffectedObject toEntity(CAcpExternalAffectedObject affectedObject);

}

