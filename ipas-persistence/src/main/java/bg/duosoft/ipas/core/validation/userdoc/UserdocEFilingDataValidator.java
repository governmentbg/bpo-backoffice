package bg.duosoft.ipas.core.validation.userdoc;

import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UserdocEFilingDataValidator implements IpasValidator<CUserdoc> {
    @Override
    public List<ValidationError> validate(CUserdoc obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        if (Objects.nonNull(obj.getUserdocEFilingData()) && Objects.isNull(obj.getUserdocEFilingData().getLogUserName())){
            errors.add(ValidationError.builder().pointer("esignature.user").messageCode("required.field").build());
        }
        return CollectionUtils.isEmpty(errors) ? null : errors;
    }
}
