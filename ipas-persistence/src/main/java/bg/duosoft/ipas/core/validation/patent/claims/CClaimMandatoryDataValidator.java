package bg.duosoft.ipas.core.validation.patent.claims;

import bg.duosoft.ipas.core.model.patent.CClaim;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class CClaimMandatoryDataValidator implements IpasTwoArgsValidator<CClaim, List<CClaim>> {

    @Override
    public List<ValidationError> validate(CClaim obj, List<CClaim> arg, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();

        if (Objects.isNull(obj.getClaimDescription()) || obj.getClaimDescription().isEmpty())
            errors.add(ValidationError.builder().pointer("claim.bg.description").messageCode("required.field").invalidValue(obj.getClaimDescription()).build());

        if (Objects.isNull(obj.getClaimNbr()) )
            errors.add(ValidationError.builder().pointer("claim.nbr").messageCode("required.field").invalidValue(obj.getClaimNbr()).build());

        return errors.size() == 0 ? null : errors;
    }
}
