package bg.duosoft.ipas.integration.payments.mapper;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.payments.CLiabilityDetal;
import bg.duosoft.ipas.integration.payments.model.PaymentLiabilityDetail;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class LiabilityDetailMapper extends BaseObjectMapper<PaymentLiabilityDetail, CLiabilityDetal> {

    @AfterMapping
    protected void afterToCore(PaymentLiabilityDetail source, @MappingTarget CLiabilityDetal target) {
        target.setId(source.getId());
        target.setReferenceNumber(source.getReferenceNumber());
        target.setLiabilityCode(source.getLiabilityCode());
        target.setAmount(source.getAmount());
        target.setAmountOutstanding(source.getAmountOutstanding());
        target.setStatus(source.getStatus());
        target.setReferenceDate(source.getReferenceDate());
        target.setDateCreated(source.getDateCreated());
        target.setPaid(source.isPaid());
        target.setUserdocNotes(source.getUserdocNotes());
        target.setUserdocTypeName(source.getUserdocTypeName());
        target.setUserdocNumber(source.getUserdocNumber());
    }

}
