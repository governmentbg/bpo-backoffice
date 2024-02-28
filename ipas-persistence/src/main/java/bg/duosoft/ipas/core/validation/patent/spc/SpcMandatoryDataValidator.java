package bg.duosoft.ipas.core.validation.patent.spc;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.FileType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class SpcMandatoryDataValidator implements IpasValidator<CPatent> {
    @Override
    public List<ValidationError> validate(CPatent obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();

        if (obj.getFile().getFileId().getFileType().equals(FileType.SPC.code()) && (Objects.isNull(obj.getFile().getRelationshipList()) || CollectionUtils.isEmpty(obj.getFile().getRelationshipList())))
            errors.add(ValidationError.builder().pointer("main.patent.autocomplete").messageCode("required.field").build());

        return errors;
    }
}
