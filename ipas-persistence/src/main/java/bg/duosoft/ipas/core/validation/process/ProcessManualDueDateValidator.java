package bg.duosoft.ipas.core.validation.process;

import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class ProcessManualDueDateValidator implements IpasValidator<CProcess> {

    @Override
    public List<ValidationError> validate(CProcess obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();

        Date dueDate = obj.getDueDate();
        rejectIfEmpty(errors, dueDate, "process.dueDate");
        return errors;
    }

}
