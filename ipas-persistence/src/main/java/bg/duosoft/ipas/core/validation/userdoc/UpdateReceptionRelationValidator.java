package bg.duosoft.ipas.core.validation.userdoc;

import bg.duosoft.ipas.core.model.reception.CUserdocReceptionRelation;
import bg.duosoft.ipas.core.service.reception.UserdocReceptionRelationService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateReceptionRelationValidator implements IpasValidator<String> {

    @Autowired
    private UserdocReceptionRelationService userdocReceptionRelationService;

    @Override
    public List<ValidationError> validate(String userdocType, Object... additionalArgs) {
        String mainType = (String) additionalArgs[0];
        Boolean isVisible = (Boolean) additionalArgs[1];
        List<ValidationError> result = new ArrayList<>();

        if (userdocType == null) {
            result.add(ValidationError.builder().pointer("userdoc.type.relation.update").messageCode("missing.userdoc.type").build());
        }
        if (mainType == null) {
            result.add(ValidationError.builder().pointer("userdoc.type.relation.update").messageCode("missing.main.type").build());
        }
        if (isVisible == null) {
            result.add(ValidationError.builder().pointer("userdoc.type.relation.update").messageCode("missing.is.visible").build());
        }

        CUserdocReceptionRelation cUserdocReceptionRelation = userdocReceptionRelationService.selectById(userdocType,mainType);
        if (cUserdocReceptionRelation == null) {
            result.add(ValidationError.builder().pointer("userdoc.type.relation.update").messageCode("missing.relation").build());
        }


        return result;
    }
}
