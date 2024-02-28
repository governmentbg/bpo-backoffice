package bg.duosoft.ipas.core.mapper.acp;

import bg.duosoft.ipas.core.model.acp.CAcpCheckReason;
import bg.duosoft.ipas.persistence.model.entity.ext.acp.AcpCheckReason;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {AcpCheckReasonNomenclatureMapper.class})
public abstract class AcpCheckReasonMapper {


    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "acpCheckReason",  source = "acpCheckReason")
    public abstract CAcpCheckReason toCore(AcpCheckReason acpCheckReason);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "pk.reasonId",  source = "acpCheckReason.id")
    public abstract AcpCheckReason toEntity(CAcpCheckReason acpCheckReason);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CAcpCheckReason> toCoreList(List<AcpCheckReason> acpCheckReasons);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<AcpCheckReason> toEntityList(List<CAcpCheckReason> acpCheckReasons);
}
