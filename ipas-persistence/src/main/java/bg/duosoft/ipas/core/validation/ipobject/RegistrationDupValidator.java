package bg.duosoft.ipas.core.validation.ipobject;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CRegistrationData;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class RegistrationDupValidator implements IpasValidator<CFile> {
    @Override
    public List<ValidationError> validate(CFile obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        CRegistrationData registrationData = obj.getRegistrationData();
        if (Objects.nonNull(registrationData) && Objects.nonNull(registrationData.getRegistrationId())) {
            String registrationDup = registrationData.getRegistrationId().getRegistrationDup();
            if (StringUtils.hasText(registrationDup) && registrationDup.length() > 2) {
                errors.add(ValidationError.builder().pointer("file.registrationData.registrationId.registrationDup").messageCode("required.regdup.length").build());
            }
        }

        return CollectionUtils.isEmpty(errors) ? null : errors;
    }
}
