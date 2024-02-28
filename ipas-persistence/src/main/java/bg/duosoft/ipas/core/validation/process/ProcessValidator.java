package bg.duosoft.ipas.core.validation.process;

import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessValidator implements IpasValidator<CProcess> {

    //TODO
    @Override
    public List<ValidationError> validate(CProcess obj, Object... additionalArgs) {
        return null;
    }
}
