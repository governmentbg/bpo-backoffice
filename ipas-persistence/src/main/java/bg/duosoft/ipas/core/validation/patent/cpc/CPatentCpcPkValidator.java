package bg.duosoft.ipas.core.validation.patent.cpc;


import bg.duosoft.ipas.core.model.patent.CPatentCpcClass;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class CPatentCpcPkValidator implements IpasTwoArgsValidator<CPatentCpcClass, List<CPatentCpcClass>> {

    @Override
    public List<ValidationError> validate(CPatentCpcClass obj, List<CPatentCpcClass> arg, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        long cnt = arg.stream().filter(r->r!=obj).filter(r -> existingCpcValidation(r, obj)).count();
        if (cnt > 0) {
            errors.add(ValidationError.builder().pointer("cpcData").messageCode("invalid.cpc.data").invalidValue(obj).build());
        }
        return CollectionUtils.isEmpty(errors) ? null : errors;
    }

    private boolean existingCpcValidation(CPatentCpcClass listObj, CPatentCpcClass addObj) {

        return Objects.equals(listObj.getCpcEdition(), addObj.getCpcEdition()) && Objects.equals(listObj.getCpcSection(), addObj.getCpcSection())
                && Objects.equals(listObj.getCpcClass(), addObj.getCpcClass()) && Objects.equals(listObj.getCpcSubclass(), addObj.getCpcSubclass())
                && Objects.equals(listObj.getCpcGroup(), addObj.getCpcGroup()) && Objects.equals(listObj.getCpcSubgroup(), addObj.getCpcSubgroup());
    }
}
