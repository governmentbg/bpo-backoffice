package bg.duosoft.ipas.core.validation.patent.drawing;

import bg.duosoft.ipas.core.model.patent.CDrawing;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: ggeorgiev
 * Date: 15.3.2019 г.
 * Time: 16:19
 */
@Component
public class CDrawingValidator implements IpasTwoArgsValidator<CDrawing, List<CDrawing>> {
    @Autowired
    private IpasValidatorCreator ipasValidatorCreator;

    @Override
    public List<ValidationError> validate(CDrawing obj, List<CDrawing> arg, Object... additionalArgs) {
        IpasTwoArgsValidator<CDrawing, List<CDrawing>> validator = ipasValidatorCreator.createTwoArgsValidator(true, CDrawingNumberValidator.class, CDrawingDrawingTypeValidator.class);
        return validator.validate(obj, arg, additionalArgs);
    }
}
