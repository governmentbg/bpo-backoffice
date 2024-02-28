package bg.duosoft.ipas.core.validation.patent;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.model.IpObjectAuthorizationValidationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PatentRecordalAuthorizationValidator implements IpasValidator<CPatent> {

    private final PatentValidator patentValidator;

    @Override
    public List<ValidationError> validate(CPatent mark, Object... additionalArgs) {
        return patentValidator.validate(mark, IpObjectAuthorizationValidationProperties.getInstance());
    }
}
