package bg.duosoft.ipas.core.validation.patent;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CPctApplicationData;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.util.date.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Georgi
 * Date: 11.2.2020 г.
 * Time: 18:29
 */
@Component
public class PctValidator implements IpasValidator<CPatent> {
    @Override
    public List<ValidationError> validate(CPatent patent, Object... additionalArgs) {
        CPctApplicationData pct = patent.getPctApplicationData();
        if (pct == null) {
            return null;
        }
        List<ValidationError> errors = new ArrayList<>();
        if (StringUtils.isEmpty(pct.getPctApplicationId())) {
            errors.add(ValidationError.builder().pointer("pctApplicationData.pctApplicationId").messageCode("required.field").build());
        }
        if (pct.getPctApplicationDate() == null) {
            errors.add(ValidationError.builder().pointer("pctApplicationData.pctApplicationDate").messageCode("required.field").build());
        } else if (DateUtils.dateToLocalDate(patent.getFile().getFilingData().getFilingDate()).isBefore(DateUtils.dateToLocalDate(pct.getPctApplicationDate()))) {
            errors.add(ValidationError.builder().pointer("pctApplicationData.pctApplicationDate").messageCode("pct.application.date.before.filing.date").build());
        }

        return errors;
    }
}
