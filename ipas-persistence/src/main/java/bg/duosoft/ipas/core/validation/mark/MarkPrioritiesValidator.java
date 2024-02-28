package bg.duosoft.ipas.core.validation.mark;

import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.ipobject.PrioritiesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: Georgi
 * Date: 6.2.2020 г.
 * Time: 18:16
 */
@Component
public class MarkPrioritiesValidator implements IpasValidator<CMark> {
    @Autowired
    private PrioritiesValidator validator;
    @Override
    public List<ValidationError> validate(CMark obj, Object... additionalArgs) {
        return validator.validate(obj.getFile(), additionalArgs);
    }
}
