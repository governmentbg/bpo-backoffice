package bg.duosoft.ipas.core.validation;

import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Component
public class JSR303Validator implements IpasValidator {
    @Autowired(required = false)
    private Validator validator;

    @Override
    public List<ValidationError> validate(Object obj, Object... additionalArgs) {
        Validator validator = this.validator == null ? Validation.buildDefaultValidatorFactory().getValidator() : this.validator;
        Set<ConstraintViolation<Object>> violations = validator.validate(obj);
        List<ValidationError> errors = new ArrayList<>();
        if (violations != null) {
            violations.stream().map(e -> ValidationError.builder().pointer(e.getPropertyPath().toString()).message(e.getMessage())/*.messageCode(formatJsr303MessageTemplate(e.getMessageTemplate()))*/.invalidValue(e.getInvalidValue()).build()).forEach(errors::add);
        }
        return errors == null || errors.size() == 0 ? null : errors;

    }
}
