package bg.duosoft.ipas.core.mapper.acp;

import bg.duosoft.ipas.core.model.acp.CAcpCheckReasonNomenclature;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAcpCheckReason;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {})
public abstract class AcpCheckReasonNomenclatureMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id",  source = "id")
    @Mapping(target = "description",  source = "description")
    public abstract CAcpCheckReasonNomenclature toCore(CfAcpCheckReason acpCheckReason);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfAcpCheckReason toEntity(CAcpCheckReasonNomenclature acpCheckReason);


    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CAcpCheckReasonNomenclature> toCoreList(List<CfAcpCheckReason> reasonList);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CfAcpCheckReason> toEntityList(List<CAcpCheckReasonNomenclature> reasonList);

}

