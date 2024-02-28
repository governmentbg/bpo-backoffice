package bg.duosoft.ipas.core.mapper.payments;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.payments.CNotLinkedPayment;
import bg.duosoft.ipas.core.model.payments.CPaymentXmlDetail;
import bg.duosoft.ipas.core.model.userdoc.CUserdocHierarchyNode;
import bg.duosoft.ipas.core.service.file.FileService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

/**
 * User: ggeorgiev
 * Date: 19.08.2021
 * Time: 10:53
 */
@Mapper(componentModel = "spring")
public abstract class NotLinkedPaymentMapper {

    @Autowired
    private FileService fileService;

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "referenceNumber",            source = "paymentXmlDetail.referenceNumber")
    @Mapping(target = "paymentAmount",              source = "paymentXmlDetail.paymentAmount")
    @Mapping(target = "amountOutstanding",          source = "paymentXmlDetail.amountOutstanding")
    @Mapping(target = "payerName",                  source = "paymentXmlDetail.payerName")
    @Mapping(target = "datePayment",                source = "paymentXmlDetail.datePayment")
    @Mapping(target = "paymentTypeName",            source = "paymentXmlDetail.paymentTypeName")
    @Mapping(target = "fileId",                     source = "masterFileId")
    @Mapping(target = "userdocs",                   source = "userdocHierarchyNodes")
    @Mapping(target = "module",                     source = "paymentXmlDetail.module")
    public abstract CNotLinkedPayment toCore(CPaymentXmlDetail paymentXmlDetail, CFileId masterFileId, List<CUserdocHierarchyNode> userdocHierarchyNodes);

    @AfterMapping
    protected void afterToCore(@MappingTarget CNotLinkedPayment target) {
        Integer registrationNbr = fileService.selectRegistrationNumberById(target.getFileId());

        if (Objects.nonNull(registrationNbr)) {
            target.setRegistrationNbr(registrationNbr);
        }
    }

}
