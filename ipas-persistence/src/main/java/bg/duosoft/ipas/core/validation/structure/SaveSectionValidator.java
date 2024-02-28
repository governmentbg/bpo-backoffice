package bg.duosoft.ipas.core.validation.structure;

import bg.duosoft.ipas.core.model.structure.OfficeDepartment;
import bg.duosoft.ipas.core.model.structure.OfficeSection;
import bg.duosoft.ipas.core.model.structure.OfficeStructureId;
import bg.duosoft.ipas.core.service.structure.OfficeDepartmentService;
import bg.duosoft.ipas.core.service.structure.OfficeSectionService;
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
public class SaveSectionValidator extends SaveStructureValidator implements IpasValidator<OfficeSection> {

//    @Autowired
//    private UserService userService;
    @Autowired
    private OfficeDepartmentService officeDepartmentService;
    @Autowired
    private OfficeSectionService officeSectionService;



    @Override
    public List<ValidationError> validate(OfficeSection section, Object... additionalArgs) {
        List<ValidationError> result = new ArrayList<>();
        validateStructureBase(section, result);
        OfficeStructureId structureId = section.getOfficeStructureId();
        if (structureId.getOfficeDivisionCode() == null) {
            result.add(ValidationError.builder().pointer("division").messageCode("missing.division").build());
        }
        if (structureId.getOfficeDepartmentCode() == null) {
            result.add(ValidationError.builder().pointer("department").messageCode("missing.department").build());
        }
        OfficeDepartment department = officeDepartmentService.getDepartment(structureId.getOfficeDivisionCode(), structureId.getOfficeDepartmentCode());
        if (department == null) {
            result.add(ValidationError.builder().pointer("department").messageCode("missing.department").build());
        }
        if (structureId.getOfficeSectionCode() != null) {
            OfficeSection sect = officeSectionService.getSection(structureId.getOfficeDivisionCode(), structureId.getOfficeDepartmentCode(), structureId.getOfficeSectionCode());
            if (sect == null) {
                result.add(ValidationError.builder().pointer("section").messageCode("missing.section").build());
            }
        }
        return result;
    }
}
