package bg.duosoft.ipas.core.validation.mark;

import bg.duosoft.ipas.core.model.acp.CAcpAdministrativePenalty;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class AcpValidator implements IpasValidator<CMark> {
    @Override
    public List<ValidationError> validate(CMark obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        if (Objects.nonNull(obj.getAcpAdministrativePenalty())) {
            rejectIfAmountIsLessThanPartiallyPaidAmount(errors, obj.getAcpAdministrativePenalty());
            rejectIfAmountIsNullButPartiallyPaidAmountIsNotNull(errors, obj.getAcpAdministrativePenalty());
        }
        return errors;
    }

    private void rejectIfAmountIsLessThanPartiallyPaidAmount(List<ValidationError> errors, CAcpAdministrativePenalty administrativePenalty) {
        if (Objects.nonNull(administrativePenalty.getPartiallyPaidAmount()) && Objects.nonNull(administrativePenalty.getAmount())
                && (administrativePenalty.getPartiallyPaidAmount().compareTo(administrativePenalty.getAmount()) == 1 || administrativePenalty.getPartiallyPaidAmount().compareTo(administrativePenalty.getAmount()) == 0)) {
            errors.add(ValidationError.builder().pointer("penalty.partiallyPaidAmount").messageCode("acp.administrative.change.payment.status.warning.message").build());
        }
    }

    private void rejectIfAmountIsNullButPartiallyPaidAmountIsNotNull(List<ValidationError> errors, CAcpAdministrativePenalty administrativePenalty) {
        if (Objects.nonNull(administrativePenalty.getPartiallyPaidAmount()) && Objects.isNull(administrativePenalty.getAmount())) {
            errors.add(ValidationError.builder().pointer("penalty.amount").messageCode("acp.administrative.penalty.empty.amount.warning.message").build());
        }
    }
}
