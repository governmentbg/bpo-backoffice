package bg.duosoft.ipas.core.validation.structure;

import bg.duosoft.ipas.core.model.structure.OfficeDepartment;
import bg.duosoft.ipas.core.model.structure.OfficeDivision;
import bg.duosoft.ipas.core.model.structure.OfficeSection;
import bg.duosoft.ipas.core.service.structure.OfficeDepartmentService;
import bg.duosoft.ipas.core.service.structure.OfficeDivisionService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User: ggeorgiev
 * Date: 5.7.2019 г.
 * Time: 14:54
 */
@Slf4j
@Component
public class TransferSectionValidator implements IpasValidator<List<OfficeSection>> {
    @Autowired
    private OfficeDivisionService officeDivisionService;
    @Autowired
    private OfficeDepartmentService officeDepartmentService;

    @Override
    public List<ValidationError> validate(List<OfficeSection> obj, Object... additionalArgs) {
        String divisionCode = (String) additionalArgs[0];
        String departmentCode = (String) additionalArgs[1];
        List<ValidationError> errors = new ArrayList<>();
        if (divisionCode == null) {
            errors.add(ValidationError.builder().pointer("division").messageCode("missing.division").build());
        } else {
            OfficeDivision division = officeDivisionService.getDivision(divisionCode);
            if (division == null) {
                errors.add(ValidationError.builder().pointer("division").messageCode("missing.division").build());
            }
        }
        if (departmentCode == null) {
            errors.add(ValidationError.builder().pointer("department").messageCode("missing.department").build());
        } else if (departmentCode != null && divisionCode != null) {
            OfficeDepartment department = officeDepartmentService.getDepartment(divisionCode, departmentCode);
            if (department == null) {
                errors.add(ValidationError.builder().pointer("department").messageCode("missing.department").build());
            }
        }

        obj.stream().filter(r -> Objects.equals(divisionCode, r.getOfficeStructureId().getOfficeDivisionCode()) && Objects.equals(departmentCode, r.getOfficeStructureId().getOfficeDepartmentCode())).forEach(r -> errors.add(ValidationError.builder().pointer("sections").messageCode("cannot.transfer.same.department").build()));
        return errors;
    }
}
