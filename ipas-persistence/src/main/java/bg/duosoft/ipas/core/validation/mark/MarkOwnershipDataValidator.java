package bg.duosoft.ipas.core.validation.mark;

import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class MarkOwnershipDataValidator implements IpasValidator<CMark> {

    @Override
    public List<ValidationError> validate(CMark mark, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        rejectIfOwnerListIsEmpty(errors, mark.getFile().getOwnershipData());
        return errors;
    }

    private void rejectIfOwnerListIsEmpty(List<ValidationError> errors, COwnershipData ownershipData) {
        rejectIfEmpty(errors, ownershipData, "file.ownershipData.ownerList");
        if (CollectionUtils.isEmpty(errors)) {
            rejectIfEmptyCollection(errors, ownershipData.getOwnerList(), "file.ownershipData.ownerList");
        }
    }
}
