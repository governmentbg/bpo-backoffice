package bg.duosoft.ipas.core.validation.structure;

import bg.duosoft.ipas.core.model.structure.OfficeDepartment;
import bg.duosoft.ipas.core.model.structure.OfficeDivision;
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
public class TransferDepartmentValidator implements IpasValidator<List<OfficeDepartment>> {
    @Autowired
    private OfficeDivisionService officeDivisionService;

    @Override
    public List<ValidationError> validate(List<OfficeDepartment> obj, Object... additionalArgs) {
        String divisionCode = (String) additionalArgs[0];
        List<ValidationError> errors = new ArrayList<>();
        if (divisionCode == null) {
            errors.add(ValidationError.builder().pointer("division").messageCode("missing.division").build());
        } else {
            OfficeDivision division = officeDivisionService.getDivision(divisionCode);
            if (division == null) {
                errors.add(ValidationError.builder().pointer("division").messageCode("missing.division").build());
            }
        }
        obj.stream().filter(r -> Objects.equals(divisionCode, r.getOfficeStructureId().getOfficeDivisionCode())).forEach(r -> errors.add(ValidationError.builder().pointer("department").messageCode("cannot.transfer.same.division").build()));
        return errors;
    }
}
