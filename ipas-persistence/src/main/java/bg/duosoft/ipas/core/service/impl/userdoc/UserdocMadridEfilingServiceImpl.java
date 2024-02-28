package bg.duosoft.ipas.core.service.impl.userdoc;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.service.action.InternationalMarkActionService;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.core.service.reception.ReceptionService;
import bg.duosoft.ipas.core.service.userdoc.UserdocMadridEfilingService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.enums.UserdocType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserdocMadridEfilingServiceImpl implements UserdocMadridEfilingService {

    private final DocService docService;
    private final ReceptionService receptionService;
    private final UserdocService userdocService;
    private final InternationalMarkActionService internationalMarkActionService;

    @Override
    public synchronized CReceptionResponse acceptMadridEfilingUserdoc(String applicationReference, CFileId affectedId, CUserdoc userdoc) {
        CDocumentId parentDocumentId = docService.selectDocumentIdByExternalSystemIdAndTypeAndParentFileId(applicationReference, UserdocType.MARK_INTERNATIONAL_REGISTRATION_REQUEST, affectedId);

        CReceptionResponse response;
        if (Objects.isNull(parentDocumentId)) {
            response = receptionService.acceptUserdoc(userdoc, new ArrayList<>(), affectedId,null,null);
            CUserdoc insertedUserdoc = userdocService.findUserdoc(response.getDocId());
            internationalMarkActionService.insertUserdocNormalAction(insertedUserdoc, ConfigParamService.EXT_CONFIG_PARAM_ZMR_MADRID_EFILING_ACTION_TYPE);
        } else {
            response = receptionService.acceptAdditionalMadridEfilingUserdoc(parentDocumentId, userdoc);
        }

        return response;
    }
}
