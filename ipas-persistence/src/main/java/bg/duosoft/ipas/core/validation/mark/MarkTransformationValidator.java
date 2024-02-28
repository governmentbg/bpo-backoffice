package bg.duosoft.ipas.core.validation.mark;

import bg.duosoft.ipas.core.model.file.CRelationshipExtended;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.util.date.DateUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raya
 * 21.03.2019
 */
@Component
public class MarkTransformationValidator implements IpasValidator<CMark> {

    @Override
    public List<ValidationError> validate(CMark mark, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        CRelationshipExtended transformation = mark.getRelationshipExtended();
        if(transformation != null && transformation.getApplicationType() != null){
            switch (transformation.getApplicationType()){
                case "EM":
                    break;
                case "WO":
                    break;
            }
            if (transformation.getFilingDate() != null && mark.getFile().getFilingData().getFilingDate() != null && DateUtils.dateToLocalDate(mark.getFile().getFilingData().getFilingDate()).isBefore(DateUtils.dateToLocalDate(transformation.getFilingDate()))) {
                errors.add(ValidationError.builder().pointer("relationshipExtended.filingDate").messageCode("wrong.rel.ext.filing.date").build());
            }
        }
        return errors;
    }
}
