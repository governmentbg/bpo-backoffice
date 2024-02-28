package bg.duosoft.ipas.integration.payments.mapper;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.payments.CPaymentXmlDetail;
import bg.duosoft.ipas.integration.payments.model.PaymentXmlDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class PaymentXmlDetailMapper extends BaseObjectMapper<PaymentXmlDetail, CPaymentXmlDetail> {
}
