package bg.duosoft.ipas.core.service.impl.userdoc;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.service.action.InternationalMarkActionService;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.core.service.reception.InternalReceptionService;
import bg.duosoft.ipas.core.service.reception.ReceptionService;
import bg.duosoft.ipas.core.service.userdoc.UserdocInternationalMarkService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.enums.UserdocType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserdocInternationalMarkServiceImpl implements UserdocInternationalMarkService {

    private final DocService docService;
    private final UserdocService userdocService;
    private final ReceptionService receptionService;
    private final InternationalMarkActionService internationalMarkActionService;

    private static final String PROCESSED_EXTERNAL_SYSTEM_TRANSACTION = "PROCESSED";
    private static final String PROCESSED_EXTERNAL_SYSTEM_TRANTYP_CBNT = "CBNT";
    private static final String PROCESSED_EXTERNAL_SYSTEM_TRANTYP_CBNP = "CBNP";


    public synchronized CReceptionResponse acceptInternationalMarkUserdoc(String parentDocumentNumber, CFileId affectedId, CUserdoc userdoc, String transaction, String trantyp) {
        CDocumentId parentDocumentId = selectParentDocumentId(parentDocumentNumber);

        CReceptionResponse res = receptionService.acceptInternationalMarkUserdoc(userdoc, new ArrayList<>(), affectedId, null, parentDocumentId);
        internationalMarkActionService.insertSpecialAction(res.getDocId());
        insertNormalActionsBasedOnExternalTransaction(parentDocumentId, transaction, trantyp);
        return res;
    }

    private void insertNormalActionsBasedOnExternalTransaction(CDocumentId parentDocumentId, String transaction, String trantyp) {
        if (PROCESSED_EXTERNAL_SYSTEM_TRANSACTION.equals(transaction)) {
            String actionExtParam = getExtParamBasedOnExternalTrantyp(trantyp);

            if (StringUtils.hasText(actionExtParam)) {
                CUserdoc parentUserdoc = userdocService.findUserdoc(parentDocumentId);
                internationalMarkActionService.insertUserdocNormalAction(parentUserdoc, actionExtParam);
            }
        }
    }

    private String getExtParamBasedOnExternalTrantyp(String trantyp) {
        if (PROCESSED_EXTERNAL_SYSTEM_TRANTYP_CBNT.equals(trantyp)) {
            return ConfigParamService.EXT_CONFIG_PARAM_ZMR_FULL_CESSATION_ACTION_TYPE;
        } else if (PROCESSED_EXTERNAL_SYSTEM_TRANTYP_CBNP.equals(trantyp)) {
            return ConfigParamService.EXT_CONFIG_PARAM_ZMR_PARTIAL_CESSATION_ACTION_TYPE;
        }

        return null;
    }

    private CDocumentId selectParentDocumentId(String parentDocumentNumber) {
        if (StringUtils.hasText(parentDocumentNumber)) {
            return docService.selectDocumentIdByExternalSystemId(parentDocumentNumber);
        }

        return null;
    }
}
