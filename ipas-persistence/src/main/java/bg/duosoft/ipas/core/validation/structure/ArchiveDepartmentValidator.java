package bg.duosoft.ipas.core.validation.structure;

import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.structure.OfficeDepartment;
import bg.duosoft.ipas.core.model.structure.OfficeSection;
import bg.duosoft.ipas.core.service.person.SimpleUserService;
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
public class ArchiveDepartmentValidator implements IpasValidator<OfficeDepartment> {

    @Autowired
    private OfficeDepartmentService departmentService;
    @Autowired
    private OfficeSectionService officeSectionService;
//    @Autowired
//    private UserService userService;
    @Autowired
    private SimpleUserService simpleUserService;

    @Override
    public List<ValidationError> validate(OfficeDepartment obj, Object... additionalArgs) {
        List<OfficeSection> activeSectionsOfDepartment = officeSectionService.getSectionsOfDepartment(obj.getOfficeStructureId().getOfficeDivisionCode(), obj.getOfficeStructureId().getOfficeDepartmentCode(), true);
        List<ValidationError> result = new ArrayList<>();
        if (activeSectionsOfDepartment.size() > 0) {
            result.add(ValidationError.builder().pointer("sections").messageCode("active.sections.exist").invalidValue(activeSectionsOfDepartment.size()).build());
        }
        List<CUser> activeUsers = simpleUserService.getUsers(obj.getOfficeStructureId().getOfficeDivisionCode(), obj.getOfficeStructureId().getOfficeDepartmentCode(), null, false, true);
        if (activeUsers.size() > 0) {
            result.add(ValidationError.builder().pointer("users").messageCode("active.users.exist").invalidValue(activeUsers.size()).build());
        }
        return result;
    }
}
