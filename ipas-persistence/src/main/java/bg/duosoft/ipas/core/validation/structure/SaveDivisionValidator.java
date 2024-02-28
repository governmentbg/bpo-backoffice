package bg.duosoft.ipas.core.validation.structure;

import bg.duosoft.ipas.core.model.structure.OfficeDivision;
import bg.duosoft.ipas.core.model.structure.OfficeStructureId;
import bg.duosoft.ipas.core.service.structure.OfficeDivisionService;
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
public class SaveDivisionValidator extends SaveStructureValidator implements IpasValidator<OfficeDivision> {

    @Autowired
    private OfficeDivisionService officeDivisionService;

    @Override
    public List<ValidationError> validate(OfficeDivision section, Object... additionalArgs) {
        List<ValidationError> result = new ArrayList<>();
        validateStructureBase(section, result);

        OfficeStructureId structureId = section.getOfficeStructureId();
        if (structureId.getOfficeDivisionCode() != null) {
            OfficeDivision div = officeDivisionService.getDivision(structureId.getOfficeDivisionCode());
            if (div == null) {
                result.add(ValidationError.builder().pointer("division").messageCode("missing.division").build());
            }
        }

        return result;
    }
}
