package bg.duosoft.ipas.core.mapper.acp;

import bg.duosoft.ipas.core.model.acp.CAcpCheckData;
import bg.duosoft.ipas.persistence.model.entity.ext.acp.AcpCheckData;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AcpCheckResultMapper.class})
public abstract class AcpCheckDataMapper {


    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "checkDate",  source = "checkDate")
    @Mapping(target = "acpCheckResult",  source = "acpCheckResult")
    public abstract CAcpCheckData toCore(AcpCheckData acpCheckData);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "resultId",  source = "acpCheckResult.id")
    public abstract AcpCheckData toEntity(CAcpCheckData acpCheckData);

}
