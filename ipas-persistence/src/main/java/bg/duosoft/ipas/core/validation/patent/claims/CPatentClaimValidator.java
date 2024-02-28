package bg.duosoft.ipas.core.validation.patent.claims;

import bg.duosoft.ipas.core.model.patent.CClaim;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CPatentClaimValidator implements IpasTwoArgsValidator<CClaim, List<CClaim>> {

    @Autowired
    private IpasValidatorCreator ipasValidatorCreator;

    @Override
    public List<ValidationError> validate(CClaim obj, List<CClaim> arg, Object... additionalArgs) {
        IpasTwoArgsValidator<CClaim, List<CClaim>> validator = ipasValidatorCreator.createTwoArgsValidator(false, CClaimMandatoryDataValidator.class, CClaimUniqueNumberValidator.class);
        return validator.validate(obj, arg, additionalArgs);
    }
}
