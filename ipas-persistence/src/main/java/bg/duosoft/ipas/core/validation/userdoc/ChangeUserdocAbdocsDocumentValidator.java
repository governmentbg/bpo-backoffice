package bg.duosoft.ipas.core.validation.userdoc;

import bg.duosoft.abdocs.model.DocRegistrationType;
import bg.duosoft.abdocs.model.DocumentTypeDto;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.miscellaneous.CAbdocsDocumentType;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.util.DefaultValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChangeUserdocAbdocsDocumentValidator implements IpasValidator<CAbdocsDocumentType> {

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Autowired
    private AbdocsService abdocsService;

    @Override
    public List<ValidationError> validate(CAbdocsDocumentType abdocsDocumentType, Object... additionalArgs) {
        List<ValidationError> result = new ArrayList<>();

        if (abdocsDocumentType == null) {
            result.add(ValidationError.builder().pointer("userdoc.abdocs.document.update").messageCode("missing.abdocs.document.type").build());
        } else {
            CUserdocType cUserdocType = userdocTypesService.selectUserdocTypeById(abdocsDocumentType.getType());
            if (cUserdocType == null) {
                result.add(ValidationError.builder().pointer("userdoc.abdocs.document.update").messageCode("missing.userdoc.type").build());
            } else {
                List<DocumentTypeDto> documentTypes = abdocsService.selectDocumentTypesByGroup(DefaultValue.ABDOCS_DOCUMENTS_IPAS_GROUP);
                DocumentTypeDto documentTypeDto = documentTypes.stream().filter(document -> document.getNomValueId().equals(abdocsDocumentType.getAbdocsDocTypeId())).findFirst().orElse(null);
                if (documentTypeDto == null) {
                    result.add(ValidationError.builder().pointer("userdoc.abdocs.document.update").messageCode("missing.abdocs.document.type").build());
                } else {
                    if (!documentTypeDto.getName().equals(abdocsDocumentType.getName())) {
                        result.add(ValidationError.builder().pointer("userdoc.abdocs.document.update").messageCode("missing.abdocs.document.type").build());
                    }

                    if (!documentTypeDto.getIsActive()) {
                        result.add(ValidationError.builder().pointer("userdoc.abdocs.document.update").messageCode("inactive.abdocs.document.type").build());
                    }
                }

                if (!checkIfRegistrationTypeExist(abdocsDocumentType.getDocRegistrationType()) ) {
                    result.add(ValidationError.builder().pointer("userdoc.abdocs.document.update").messageCode("missing.registration.type").build());
                }
            }
        }

        return result;
    }

    private boolean checkIfRegistrationTypeExist (Integer value) {
        if (value == null) {
            return false;
        }

        for (DocRegistrationType type : DocRegistrationType.values()) {
            if (type.value() == value.intValue()) {
                return true;
            }
        }
        return false;
    }
}
