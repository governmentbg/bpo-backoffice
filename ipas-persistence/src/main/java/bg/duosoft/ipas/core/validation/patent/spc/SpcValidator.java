package bg.duosoft.ipas.core.validation.patent.spc;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpcValidator implements IpasValidator<CPatent> {

    @Autowired
    private IpasValidatorCreator validatorCreator;

    @Override
    public List<ValidationError> validate(CPatent obj, Object... additionalArgs) {
        IpasValidator<CPatent> validator = validatorCreator.create(false,SpcMandatoryDataValidator.class);
        return validator.validate(obj,additionalArgs);
    }
}
