package bg.duosoft.ipas.core.mapper.acp;

import bg.duosoft.ipas.core.model.acp.CAcpAdministrativePenalty;
import bg.duosoft.ipas.persistence.model.entity.ext.acp.AcpAdministrativePenalty;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AcpAdministrativePenaltyTypeMapper.class,AcpAdministrativePenaltyPaymentStatusMapper.class})
public abstract class AcpAdministrativePenaltyMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "penaltyType",  source = "penaltyType")
    @Mapping(target = "amount",  source = "amount")
    @Mapping(target = "partiallyPaidAmount",  source = "partiallyPaidAmount")
    @Mapping(target = "notificationDate",  source = "notificationDate")
    @Mapping(target = "otherTypeDescription",  source = "otherTypeDescription")
    @Mapping(target = "paymentStatus",  source = "paymentStatus")
    public abstract CAcpAdministrativePenalty toCore(AcpAdministrativePenalty penalty);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "administrativePenaltyTypeId",  source = "penaltyType.id")
    @Mapping(target = "paymentStatusId",  source = "paymentStatus.id")
    public abstract AcpAdministrativePenalty toEntity(CAcpAdministrativePenalty penalty);


}

