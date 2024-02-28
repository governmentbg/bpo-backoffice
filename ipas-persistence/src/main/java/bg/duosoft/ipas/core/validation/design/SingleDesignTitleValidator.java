package bg.duosoft.ipas.core.validation.design;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class SingleDesignTitleValidator implements IpasValidator<CPatent> {
    @Override
    public List<ValidationError> validate(CPatent obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        if (Objects.isNull(obj.getTechnicalData()) || Objects.isNull(obj.getTechnicalData().getTitle()) || obj.getTechnicalData().getTitle().isEmpty()) {
            ValidationError validationError = ValidationError.builder().pointer(obj.getFile().getFileId().getFileNbr()+".designSingleTitle").messageCode("required.field").build();
            validationError.setPointerMessageCode("designSingleTitle");
            errors.add(validationError);
        }
        
        return CollectionUtils.isEmpty(errors) ? null : errors;
    }
}