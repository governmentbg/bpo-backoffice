package bg.duosoft.ipas.core.validation.patent.claims;

import bg.duosoft.ipas.core.model.patent.CClaim;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CClaimUniqueNumberValidator implements IpasTwoArgsValidator<CClaim, List<CClaim>> {
    @Override
    public List<ValidationError> validate(CClaim obj, List<CClaim> arg, Object... additionalArgs) {

        List<ValidationError> errors = arg.
                stream().
                filter(cClaim -> !Objects.isNull(cClaim.getClaimNbr())).
                filter(cClaim -> Objects.equals(cClaim.getClaimNbr(), obj.getClaimNbr()))
                .map(d -> ValidationError.builder().pointer("claim.nbr").messageCode("duplicate.claimNbr").invalidValue(obj.getClaimNbr()).build())
                .collect(Collectors.toList());

        return errors.size() == 0 ? null : errors;
    }
}
