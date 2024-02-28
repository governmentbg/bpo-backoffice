package bg.duosoft.ipas.core.mapper.acp;

import bg.duosoft.ipas.core.model.acp.CAcpAdministrativePenaltyPaymentStatus;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAcpAdministrativePenaltyPaymentStatus;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {})
public abstract class AcpAdministrativePenaltyPaymentStatusMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id",  source = "id")
    @Mapping(target = "description",  source = "description")
    public abstract CAcpAdministrativePenaltyPaymentStatus toCore(CfAcpAdministrativePenaltyPaymentStatus status);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfAcpAdministrativePenaltyPaymentStatus toEntity(CAcpAdministrativePenaltyPaymentStatus status);


    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CAcpAdministrativePenaltyPaymentStatus> toCoreList(List<CfAcpAdministrativePenaltyPaymentStatus> statuses);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CfAcpAdministrativePenaltyPaymentStatus> toEntityList(List<CAcpAdministrativePenaltyPaymentStatus> statuses);


}

