package bg.duosoft.ipas.core.validation.design;


import bg.duosoft.ipas.core.model.design.CPatentLocarnoClasses;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Component
public class SingleDesignUniqueLocarnoClassValidator implements IpasTwoArgsValidator<CPatentLocarnoClasses, List<CPatentLocarnoClasses>> {
    @Override
    public List<ValidationError> validate(CPatentLocarnoClasses obj, List<CPatentLocarnoClasses> arg, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        if (!Objects.isNull(arg)){
            long cnt = arg.stream().filter(r->r!=obj).filter(r-> r.getLocarnoClassCode().equals(obj.getLocarnoClassCode())).count();
            if (cnt > 0) {
                errors.add(ValidationError.builder().pointer("locarnoClassesData").messageCode("invalid.locarno.classes.data").invalidValue(obj).build());
            }
        }
        return CollectionUtils.isEmpty(errors) ? null : errors;
    }
}
