package bg.duosoft.ipas.core.validation.mark;

import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.model.IpObjectPartialInternalValidationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MarkPartialInternalValidator implements IpasValidator<CMark> {

    private final MarkValidator markValidator;

    @Override
    public List<ValidationError> validate(CMark mark, Object... additionalArgs) {
        return markValidator.validate(mark, IpObjectPartialInternalValidationProperties.getInstance());
    }
}
