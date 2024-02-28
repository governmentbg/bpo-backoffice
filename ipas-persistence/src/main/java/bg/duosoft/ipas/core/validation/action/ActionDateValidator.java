package bg.duosoft.ipas.core.validation.action;

import bg.duosoft.ipas.core.model.action.CAction;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class ActionDateValidator implements IpasValidator<CAction> {

    @Override
    public List<ValidationError> validate(CAction obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();

        rejectIfEmpty(errors, obj.getActionDate(), "action.actionDate");
        return CollectionUtils.isEmpty(errors) ? null : errors;
    }
}
