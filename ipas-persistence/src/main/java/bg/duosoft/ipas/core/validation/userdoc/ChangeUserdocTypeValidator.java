package bg.duosoft.ipas.core.validation.userdoc;

import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.action.ActionService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.reception.AbdocsDocumentTypeService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChangeUserdocTypeValidator implements IpasValidator<CUserdoc> {

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Autowired
    private AbdocsDocumentTypeService abdocsDocumentTypeService;

    @Autowired
    private ActionService actionService;

    @Override
    public List<ValidationError> validate(CUserdoc userdoc, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();

        CUserdocType originalUserdocType = userdocTypesService.selectUserdocTypeByDocId(userdoc.getDocumentId());
        rejectIfEmpty(errors, originalUserdocType, "userdoc.userdocType", "userdoc.change.type.empty.original.type");
        if (CollectionUtils.isEmpty(errors)) {
            CUserdocType newUserdocTypeObject = userdoc.getUserdocType();
            String newUserdocType = newUserdocTypeObject.getUserdocType();
            String oldUserdocType = originalUserdocType.getUserdocType();
            if (!(oldUserdocType.equalsIgnoreCase(newUserdocType))) {
                Integer abdocsDocumentType = abdocsDocumentTypeService.selectAbdocsDocTypeIdByType(newUserdocType);
                rejectIfEmpty(errors, abdocsDocumentType, "userdoc.userdocType", "userdoc.change.type.missing.abodcs.document.type");

                if (CollectionUtils.isEmpty(errors)) {
                    if (!(newUserdocTypeObject.getGenerateProcTyp().equalsIgnoreCase(originalUserdocType.getGenerateProcTyp()))) {
                        boolean hasActions = actionService.isActionExistsForProcessId(userdoc.getProcessSimpleData().getProcessId());
                        rejectIfTrue(errors, hasActions, "userdoc.userdocType", "userdoc.change.type.actions.exists");
                    }
                }

                if (CollectionUtils.isEmpty(errors)) {
                    CProcessId processId = userdoc.getProcessSimpleData().getProcessId();
                    boolean isAuthorizationActionsExists = actionService.isUserdocAuthorizationActionsExists(processId);
                    rejectIfTrue(errors, isAuthorizationActionsExists, "userdoc.userdocType", "userdoc.change.type.authorization.actions.exists");
                }

            }
        }
        return CollectionUtils.isEmpty(errors) ? null : errors;
    }
}
