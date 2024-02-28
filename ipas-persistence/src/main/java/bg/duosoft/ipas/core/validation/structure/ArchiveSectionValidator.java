package bg.duosoft.ipas.core.validation.structure;

import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.structure.OfficeSection;
import bg.duosoft.ipas.core.service.person.SimpleUserService;
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
public class ArchiveSectionValidator implements IpasValidator<OfficeSection> {

    @Autowired
    private SimpleUserService userService;

    @Override
    public List<ValidationError> validate(OfficeSection section, Object... additionalArgs) {
        List<ValidationError> result = new ArrayList<>();
        List<CUser> activeUsers = userService.getUsers(section.getOfficeStructureId().getOfficeDivisionCode(), section.getOfficeStructureId().getOfficeDepartmentCode(), section.getOfficeStructureId().getOfficeSectionCode(), false, true);
        if (activeUsers.size() > 0) {
            result.add(ValidationError.builder().pointer("users").messageCode("active.users.exist").invalidValue(activeUsers.size()).build());
        }
        return result;
    }
}
