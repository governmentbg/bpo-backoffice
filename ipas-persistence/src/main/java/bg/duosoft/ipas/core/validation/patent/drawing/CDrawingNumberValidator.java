package bg.duosoft.ipas.core.validation.patent.drawing;

import bg.duosoft.ipas.core.model.patent.CDrawing;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 15.3.2019 г.
 * Time: 16:17
 */
@Component
public class CDrawingNumberValidator implements IpasTwoArgsValidator<CDrawing, List<CDrawing>> {
    @Override
    public List<ValidationError> validate(CDrawing obj, List<CDrawing> arg, Object... additionalArgs) {
        List<ValidationError> res = arg
                .stream()
                .filter(d -> d!=obj)
                .filter(d -> Objects.equals(d.getDrawingNbr(), obj.getDrawingNbr()))
                .map(d -> ValidationError.builder().pointer("drawingData").messageCode("duplicate.drawingNbr").invalidValue(obj.getDrawingNbr()).build())
                .collect(Collectors.toList());
        return res.size() == 0 ? null : res;

    }
}
