package bg.duosoft.ipas.core.validation.config;

import java.util.List;

public interface IpasValidator<T> extends DefaultValidation {
    List<ValidationError> validate(T obj, Object... additionalArgs);
}
