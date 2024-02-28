package bg.duosoft.ipas.core.validation.structure;

import bg.duosoft.ipas.core.model.structure.OfficeStructureId;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.persistence.repository.entity.user.IpUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 4.7.2019 г.
 * Time: 16:07
 */
@Service
public class TransferUserValidator implements IpasValidator<List<Integer>> {
    @Autowired
    private IpUserRepository ipUserRepository;
    @Override
    public List<ValidationError> validate(List<Integer> obj, Object... additionalArgs) {
        OfficeStructureId newStructureId = (OfficeStructureId) additionalArgs[0];
        List<ValidationError> errors = new ArrayList<>();
        if (obj == null || obj.size() == 0) {
            errors.add(ValidationError.builder().pointer("users").messageCode("transfer.user.empty.list").build());
        } else {
            long cnt = ipUserRepository.countUsersSameStructure(obj, newStructureId.getOfficeDivisionCode(), newStructureId.getOfficeDepartmentCode(), newStructureId.getOfficeSectionCode());
            if (cnt != 0) {
                errors.add(ValidationError.builder().pointer("users").messageCode("cannot.transfer.user.same.structure").build());
            }
        }

        return errors;
    }
}
