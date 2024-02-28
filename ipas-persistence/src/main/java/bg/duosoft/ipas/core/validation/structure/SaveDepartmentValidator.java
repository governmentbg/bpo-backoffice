package bg.duosoft.ipas.core.validation.structure;

import bg.duosoft.ipas.core.model.structure.OfficeDepartment;
import bg.duosoft.ipas.core.model.structure.OfficeStructureId;
import bg.duosoft.ipas.core.service.structure.OfficeDepartmentService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 5.7.2019 г.
 * Time: 11:27
 */
@Component
public class SaveDepartmentValidator extends SaveStructureValidator implements IpasValidator<OfficeDepartment> {

    @Autowired
    private OfficeDepartmentService officeDepartmentService;

    @Override
    public List<ValidationError> validate(OfficeDepartment section, Object... additionalArgs) {
        List<ValidationError> result = new ArrayList<>();
        validateStructureBase(section, result);

        OfficeStructureId structureId = section.getOfficeStructureId();
        if (structureId.getOfficeDivisionCode() == null) {
            result.add(ValidationError.builder().pointer("division").messageCode("missing.division").build());
        }
        if (structureId.isDepartment()) {
            OfficeDepartment department = officeDepartmentService.getDepartment(structureId.getOfficeDivisionCode(), structureId.getOfficeDepartmentCode());
            if (department == null) {
                result.add(ValidationError.builder().pointer("department").messageCode("missing.department").build());
            }

        }


        return result;
    }
}
