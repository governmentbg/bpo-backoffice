package bg.duosoft.ipas.core.validation.userdoc;

import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.reception.UserdocReceptionRelationService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AddReceptionRelationForUserdocTypeValidator implements IpasValidator<String> {

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Autowired
    private UserdocReceptionRelationService userdocReceptionRelationService;

    @Override
    public List<ValidationError> validate(String userdocType, Object... additionalArgs) {
        String mainType = (String) additionalArgs[0];
        Boolean isVisible = (Boolean) additionalArgs[1];
        List<ValidationError> result = new ArrayList<>();

        if(userdocType == null) {
            result.add(ValidationError.builder().pointer("userdoc.type.relation.add").messageCode("missing.userdoc.type").build());
        }
        if (mainType == null) {
            result.add(ValidationError.builder().pointer("userdoc.type.relation.add").messageCode("missing.main.type").build());
        }
        if (isVisible == null) {
            result.add(ValidationError.builder().pointer("userdoc.type.relation.add").messageCode("missing.is.visible").build());
        }

        CUserdocType cUserdocType = userdocTypesService.selectUserdocTypeById(userdocType);
        if (cUserdocType == null) {
            result.add(ValidationError.builder().pointer("userdoc.type.relation.add").messageCode("missing.userdoc.type").build());
        } else {
            Map<String, String> mainTypesMap = userdocReceptionRelationService.selectMainTypesMap();
            if (!mainTypesMap.containsKey(mainType)) {
                result.add(ValidationError.builder().pointer("userdoc.type.relation.add").messageCode("missing.main.type").build());
            } else {
                Boolean isExist = userdocReceptionRelationService.existsById(userdocType,mainType);
                if (isExist) {
                    result.add(ValidationError.builder().pointer("userdoc.type.relation.add").messageCode("relation.already.exist").build());
                }
            }
        }
        return result;
    }
}
