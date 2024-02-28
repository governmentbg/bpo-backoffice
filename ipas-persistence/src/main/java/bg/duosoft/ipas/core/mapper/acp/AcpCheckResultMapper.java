package bg.duosoft.ipas.core.mapper.acp;

import bg.duosoft.ipas.core.model.acp.CAcpCheckResult;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAcpCheckResult;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {})
public abstract class AcpCheckResultMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id",  source = "id")
    @Mapping(target = "description",  source = "description")
    public abstract CAcpCheckResult toCore(CfAcpCheckResult checkResult);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfAcpCheckResult toEntity(CAcpCheckResult checkResult);


    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CAcpCheckResult> toCoreList(List<CfAcpCheckResult> checkResults);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CfAcpCheckResult> toEntityList(List<CAcpCheckResult> checkResults);


}

