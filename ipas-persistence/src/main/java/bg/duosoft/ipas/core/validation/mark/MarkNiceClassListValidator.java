package bg.duosoft.ipas.core.validation.mark;

import bg.duosoft.ipas.core.model.mark.CNiceClassList;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MarkNiceClassListValidator implements IpasValidator<CNiceClassList> {

    @Override
    public List<ValidationError> validate(CNiceClassList cNiceClassList, Object... additionalArgs) {
        List<ValidationError> result = new ArrayList<>();

        if (cNiceClassList == null) {
            result.add(ValidationError.builder().pointer("nice.class.list.edit").messageCode("missing.nice.class.list").build());
        } else {
            rejectIfEmptyString(result, cNiceClassList.getHeading(), "nice.class.heading");
            rejectIfEmptyString(result, cNiceClassList.getAlphaList(), "nice.class.alpha.list");
        }
        return result;
    }
}
