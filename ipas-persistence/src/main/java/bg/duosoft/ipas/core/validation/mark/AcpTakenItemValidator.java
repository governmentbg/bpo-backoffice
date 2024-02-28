package bg.duosoft.ipas.core.validation.mark;

import bg.duosoft.ipas.core.model.acp.CAcpTakenItem;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class AcpTakenItemValidator implements IpasValidator<CAcpTakenItem> {
    @Override
    public List<ValidationError> validate(CAcpTakenItem takenItem, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        if (Objects.isNull(takenItem.getType()) || Objects.isNull(takenItem.getType().getId())){
            errors.add(ValidationError.builder().pointer("taken.item.type").messageCode("required.field").build());
        }
        if (Objects.isNull(takenItem.getStorage()) || Objects.isNull(takenItem.getStorage().getId())){
            errors.add(ValidationError.builder().pointer("taken.item.storage").messageCode("required.field").build());
        }
        return errors;
    }
}
