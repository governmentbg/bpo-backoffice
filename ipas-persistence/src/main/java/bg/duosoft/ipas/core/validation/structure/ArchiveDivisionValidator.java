package bg.duosoft.ipas.core.validation.structure;

import bg.duosoft.ipas.core.model.structure.OfficeDepartment;
import bg.duosoft.ipas.core.model.structure.OfficeDivision;
import bg.duosoft.ipas.core.service.person.SimpleUserService;
import bg.duosoft.ipas.core.service.structure.OfficeDepartmentService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User: ggeorgiev
 * Date: 5.7.2019 г.
 * Time: 11:27
 */
@Component
public class ArchiveDivisionValidator implements IpasValidator<OfficeDivision> {

    @Autowired
    private OfficeDepartmentService departmentService;

    @Autowired
    private ArchiveDepartmentValidator archiveDepartmentValidator;

    @Autowired
    private SimpleUserService userService;

    @Override
    public List<ValidationError> validate(OfficeDivision obj, Object... additionalArgs) {
        List<ValidationError> result = new ArrayList<>();
        List<OfficeDepartment> departmentDivisions = departmentService.getDepartmentsOfDivision(obj.getOfficeStructureId().getOfficeDivisionCode(), false);
        departmentDivisions.stream().map(r -> archiveDepartmentValidator.validate(r, additionalArgs)).filter(Objects::nonNull).filter(r -> !CollectionUtils.isEmpty(r)).flatMap(r -> r.stream()).forEach(result::add);
        departmentDivisions.stream().filter(r -> r.getActive()).findAny().ifPresent(r -> result.add(ValidationError.builder().pointer("departments").messageCode("active.departments.exist").build()));
        userService.getUsers(obj.getOfficeStructureId().getOfficeDivisionCode(), null, null, true, true).stream().findAny().ifPresent(r -> ValidationError.builder().pointer("users").messageCode("active.users.exist").build());

        return result;
    }
}
