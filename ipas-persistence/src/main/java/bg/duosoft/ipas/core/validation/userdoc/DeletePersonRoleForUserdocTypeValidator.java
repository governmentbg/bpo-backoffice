package bg.duosoft.ipas.core.validation.userdoc;

import bg.duosoft.ipas.core.model.userdoc.CUserdocPersonRole;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.userdoc.UserdocPersonRoleService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeletePersonRoleForUserdocTypeValidator implements IpasValidator<String> {

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Autowired
    private UserdocPersonRoleService userdocPersonRoleService;

    @Override
    public List<ValidationError> validate(String userdocType, Object... additionalArgs) {
        UserdocPersonRole role = (UserdocPersonRole) additionalArgs[0];
        List<ValidationError> result = new ArrayList<>();

        if (userdocType == null) {
            result.add(ValidationError.builder().pointer("userdoc.type.role.delete").messageCode("missing.userdoc.type").build());
        }
        if (role == null) {
            result.add(ValidationError.builder().pointer("userdoc.type.role.delete").messageCode("missing.role").build());
        }
        CUserdocType cUserdocType = userdocTypesService.selectUserdocTypeById(userdocType);
        if (cUserdocType == null) {
            result.add(ValidationError.builder().pointer("userdoc.type.role.delete").messageCode("missing.userdoc.type").build());
        } else {
            CUserdocPersonRole cUserdocPersonRole = userdocPersonRoleService.findPersonRoleByRoleAndUserdocType(role, userdocType);
            if (cUserdocPersonRole == null) {
                result.add(ValidationError.builder().pointer("userdoc.type.role.delete").messageCode("missing.role").build());
            } else {
                if (!cUserdocType.getRoles().contains(cUserdocPersonRole)) {
                    result.add(ValidationError.builder().pointer("userdoc.type.role.delete").messageCode("missing.role").build());
                }
            }
        }


        return result;
    }
}
