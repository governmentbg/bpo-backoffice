package bg.duosoft.ipas.core.validation.userdoc;

import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AddInvalidatedUserdocTypeValidator implements IpasValidator<String> {
    @Autowired
    private UserdocTypesService userdocTypesService;

    @Override
    public List<ValidationError> validate(String userdocType, Object... additionalArgs) {
        String invaliteUserdocType = (String) additionalArgs[0];
        List<ValidationError> result = new ArrayList<>();

        if (userdocType == null) {
            result.add(ValidationError.builder().pointer("invalidated.userdoc.type.add").messageCode("missing.userdoc.type").build());
        }
        if (invaliteUserdocType == null) {
            result.add(ValidationError.builder().pointer("invalidated.userdoc.type.add").messageCode("missing.invalidate.userdoc.type").build());
        }
        CUserdocType cUserdocType = userdocTypesService.selectUserdocTypeById(userdocType);
        if (cUserdocType == null) {
            result.add(ValidationError.builder().pointer("invalidated.userdoc.type.add").messageCode("missing.userdoc.type").build());
        } else {
            List<String> userdocsForInvalidation = userdocTypesService.selectUserdocTypesForInvalidation(cUserdocType.getUserdocType());
            if (!userdocsForInvalidation.contains(invaliteUserdocType)) {
                result.add(ValidationError.builder().pointer("invalidated.userdoc.type.add").messageCode("missing.invalidate.userdoc.type").build());
            } else {
                if (cUserdocType.getInvalidatedUserdocTypes().contains(invaliteUserdocType)) {
                    result.add(ValidationError.builder().pointer("invalidated.userdoc.type.add").messageCode("invalite.userdoc.type.already.exist").build());
                }
            }
        }

        return result;
    }
}
