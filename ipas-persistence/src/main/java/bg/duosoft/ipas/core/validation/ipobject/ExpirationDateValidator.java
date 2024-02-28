package bg.duosoft.ipas.core.validation.ipobject;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CRegistrationData;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.FileType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class ExpirationDateValidator implements IpasValidator<CFile> {
    @Override
    public List<ValidationError> validate(CFile obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();

        String fileType = obj.getFileId().getFileType();
        if (!isFileTypeExcluded(fileType)) {
            CRegistrationData registrationData = obj.getRegistrationData();
            if (Objects.nonNull(registrationData)) {
                Date registrationDate = registrationData.getRegistrationDate();
                if (Objects.nonNull(registrationDate)) {
                    Date expirationDate = registrationData.getExpirationDate();
                    if (Objects.isNull(expirationDate)) {
                        errors.add(ValidationError.builder().pointer("file.registrationData.expirationDate").messageCode("required.field").build());
                    }
                }
            }
        }

        return CollectionUtils.isEmpty(errors) ? null : errors;
    }

    private boolean isFileTypeExcluded(String fileType) {
        switch (FileType.selectByCode(fileType)) {
            case GEOGRAPHICAL_INDICATIONS:
                return true;
            default:
                return false;
        }
    }
}
