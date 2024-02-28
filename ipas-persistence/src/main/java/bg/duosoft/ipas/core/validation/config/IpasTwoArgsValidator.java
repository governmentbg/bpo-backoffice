package bg.duosoft.ipas.core.validation.config;

import java.util.List;

/**
 * User: ggeorgiev
 * Date: 15.3.2019 г.
 * Time: 16:01
 */
public interface IpasTwoArgsValidator<U, V> {
    List<ValidationError> validate(U obj, V arg, Object... additionalArgs);
}
