package bg.duosoft.ipas.core.validation.offidoc;

import bg.duosoft.ipas.core.model.offidoc.COffidocType;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.OffidocDirection;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateOffidocTypeValidator implements IpasValidator<COffidocType> {

    @Override
    public List<ValidationError> validate(COffidocType cOffidocType, Object... additionalArgs) {
        List<ValidationError> result = new ArrayList<>();

        if (cOffidocType == null) {
            result.add(ValidationError.builder().pointer("offidoc.type.update").messageCode("missing.offidoc.type").build());
        } else {
            rejectIfEmptyString(result, cOffidocType.getOffidocName(), "offidoc.name.label");

            if (!checkIfOffidocDirectionExists(cOffidocType.getDirection())) {
                result.add(ValidationError.builder().pointer("offidoc.name.label").messageCode("required.field").build());
            }
        }
        return result;
    }

    private boolean checkIfOffidocDirectionExists (String value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }

        for (OffidocDirection direction : OffidocDirection.values()) {
            if (value.equals(direction.getValue())) {
                return true;
            }
        }
        return false;
    }
}
