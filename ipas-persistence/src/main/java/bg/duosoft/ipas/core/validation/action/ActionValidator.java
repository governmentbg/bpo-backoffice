package bg.duosoft.ipas.core.validation.action;

import bg.duosoft.ipas.core.model.action.CAction;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActionValidator implements IpasValidator<CAction> {

    //TODO
    @Override
    public List<ValidationError> validate(CAction obj, Object... additionalArgs) {
        return null;
    }
}
