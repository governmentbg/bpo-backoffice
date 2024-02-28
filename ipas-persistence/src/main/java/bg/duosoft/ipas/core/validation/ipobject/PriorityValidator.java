package bg.duosoft.ipas.core.validation.ipobject;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CParisPriority;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.util.date.DateUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Raya
 * 14.03.2019
 */
@Component
public class PriorityValidator  implements IpasTwoArgsValidator<CParisPriority, CFile> {

    @Override
    public List<ValidationError> validate(CParisPriority obj, CFile file, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        if (file.getFilingData().getFilingDate() != null && obj.getPriorityDate() != null) {
            if (DateUtils.dateToLocalDate(file.getFilingData().getFilingDate()).isBefore(DateUtils.dateToLocalDate(obj.getPriorityDate()))) {
                errors.add(ValidationError.builder().pointer("priorityDate").messageCode("priority.date.before.filing.date").build());
            }
        }
        return errors;

        /*if(obj != null && obj.getPriorityDate() != null && filingDate != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(filingDate);
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            Date fdate = calendar.getTime();
            calendar.add(Calendar.MONTH, -6);
            Date fdateMinus6month  = calendar.getTime();

            List<ValidationError> errors = new ArrayList<>();
            if(obj.getPriorityDate().before(fdateMinus6month)){
                ValidationError error = new ValidationError("priorityDate",
                    "Priority date is earlier than 6 months from filing date", "priorityDate.moreThan6m.before.filingDate", obj.getPriorityDate());
                errors.add(error);
            } else if(obj.getPriorityDate().after(fdate)){
                ValidationError error = new ValidationError("priorityDate",
                    "priorityDate.after.filingDate", "Priority date is after filing date", obj.getPriorityDate());
                errors.add(error);
            }

            if(errors.size()>0){
                return errors;
            }
            return null;
        }*/

    }
}
