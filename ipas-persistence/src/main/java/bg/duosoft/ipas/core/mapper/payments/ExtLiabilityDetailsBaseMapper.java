package bg.duosoft.ipas.core.mapper.payments;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.CExtLiabilityDetail;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.integration.payments.service.PaymentsIntegrationService;
import bg.duosoft.ipas.persistence.model.entity.ext.ExtLiabilityDetailsBase;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Objects;

/**
 * User: ggeorgiev
 * Date: 18.08.2021
 * Time: 11:37
 */
public abstract class ExtLiabilityDetailsBaseMapper<E extends ExtLiabilityDetailsBase, C extends CExtLiabilityDetail> extends BaseObjectMapper<E, C> {
    @Autowired
    private PaymentsIntegrationService paymentsIntegrationService;

    @AfterMapping
    protected void afterToCore(E source, @MappingTarget C target) {
        Map<String, String> liabilityCodeNamesMap = paymentsIntegrationService.getLiabilityCodeNamesMap();
        if (Objects.nonNull(liabilityCodeNamesMap)) {
            target.setLiabilityCodeName(liabilityCodeNamesMap.get(source.getLiabilityCode()));
        }
        if (source.getPk().getFileTyp().equals(FileType.USERDOC.code())) {
            target.setDocumentId(new CDocumentId(source.getPk().getFileSeq(), source.getPk().getFileTyp(), source.getPk().getFileSer(), source.getPk().getFileNbr()));
        } else {
            target.setFileId(new CFileId(source.getPk().getFileSeq(), source.getPk().getFileTyp(), source.getPk().getFileSer(), source.getPk().getFileNbr()));
        }
    }

}
