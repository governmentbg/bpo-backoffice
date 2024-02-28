package bg.duosoft.ipas.core.validation.patent.ipcs;


import bg.duosoft.ipas.core.model.patent.CPatentIpcClass;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class CPatentIpcPkValidator implements IpasTwoArgsValidator<CPatentIpcClass, List<CPatentIpcClass>> {

    @Override
    public List<ValidationError> validate(CPatentIpcClass obj, List<CPatentIpcClass> arg, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        long cnt = arg.stream().filter(r->r!=obj).filter(r -> existingIpcValidation(r, obj)).count();
        if (cnt > 0) {
            errors.add(ValidationError.builder().pointer("ipcData").messageCode("invalid.ipc.data").invalidValue(obj).build());
        }
        return CollectionUtils.isEmpty(errors) ? null : errors;
    }

    private boolean existingIpcValidation(CPatentIpcClass listObj, CPatentIpcClass addObj) {

        return Objects.equals(listObj.getIpcEdition(), addObj.getIpcEdition()) && Objects.equals(listObj.getIpcSection(), addObj.getIpcSection())
                && Objects.equals(listObj.getIpcClass(), addObj.getIpcClass()) && Objects.equals(listObj.getIpcSubclass(), addObj.getIpcSubclass())
                && Objects.equals(listObj.getIpcGroup(), addObj.getIpcGroup()) && Objects.equals(listObj.getIpcSubgroup(), addObj.getIpcSubgroup());
    }
}
