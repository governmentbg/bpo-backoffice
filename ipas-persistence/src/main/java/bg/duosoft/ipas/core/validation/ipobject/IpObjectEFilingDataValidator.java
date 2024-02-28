package bg.duosoft.ipas.core.validation.ipobject;

import bg.duosoft.ipas.core.model.efiling.CEFilingData;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class IpObjectEFilingDataValidator implements IpasValidator<CEFilingData> {
    @Override
    public List<ValidationError> validate(CEFilingData obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        if (Objects.nonNull(obj) && Objects.isNull(obj.getLogUserName())){
            errors.add(ValidationError.builder().pointer("esignature.user").messageCode("required.field").build());
        }
        return CollectionUtils.isEmpty(errors) ? null : errors;
    }

}
