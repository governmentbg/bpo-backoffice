package bg.duosoft.ipas.core.validation.ipobject;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CRegistrationData;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.FileType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class EntitlementDateValidator implements IpasValidator<CFile> {
    @Override
    public List<ValidationError> validate(CFile obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        CRegistrationData registrationData = obj.getRegistrationData();

        CFileId fileId = obj.getFileId();
        FileType fileType = FileType.selectByCode(fileId.getFileType());
        switch (fileType) {
            case PLANTS_AND_BREEDS: {
                if (Objects.nonNull(registrationData)) {
                    Date registrationDate = registrationData.getRegistrationDate();
                    if (Objects.nonNull(registrationDate)) {
                        Date entitlementDate = registrationData.getEntitlementDate();
                        if (Objects.isNull(entitlementDate)) {
                            errors.add(ValidationError.builder().pointer("file.registrationData.entitlementDate").messageCode("required.field").build());
                        }
                    }
                }
                break;
            }
            default: {
                if (Objects.isNull(registrationData) || Objects.isNull(registrationData.getEntitlementDate())) {
                    errors.add(ValidationError.builder().pointer("file.registrationData.entitlementDate").messageCode("required.field").build());
                }
                break;
            }
        }
        return CollectionUtils.isEmpty(errors) ? null : errors;
    }
}
