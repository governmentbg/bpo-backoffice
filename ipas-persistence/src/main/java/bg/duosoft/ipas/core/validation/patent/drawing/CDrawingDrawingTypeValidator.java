package bg.duosoft.ipas.core.validation.patent.drawing;

import bg.duosoft.ipas.core.model.patent.CDrawing;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.image.ImageValidatorBase;
import bg.duosoft.ipas.enums.ImageFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static bg.duosoft.ipas.enums.ImageFormat.*;

/**
 * User: ggeorgiev
 * Date: 11.3.2019 г.
 * Time: 18:39
 */
@Component
public class CDrawingDrawingTypeValidator extends ImageValidatorBase implements IpasTwoArgsValidator<CDrawing, List<CDrawing>> {

    @Autowired
    private MessageSource messageSource;

    @Override
    public List<ValidationError> validate(CDrawing obj, List<CDrawing> arg, Object... additionalArgs) {
        Integer maxFileSize = Objects.isNull(additionalArgs) || additionalArgs.length == 0 ? null : (Integer) additionalArgs[0];
        Set<ImageFormat> supportedFormats = Set.of(TIFF, JPEG, BMP, PNG, GIF);
        ImageValidationResult result = isValid(obj.getDrawingData(), supportedFormats, maxFileSize);
        List<ValidationError> errors = new ArrayList<>();
        String drawingFileName=null;
        if (additionalArgs.length > 1 && !Objects.isNull(additionalArgs[1])){
            drawingFileName = ((String)additionalArgs[1]).concat(" - ");
        }
        if (!result.isValidType()){
            String message = messageSource.getMessage("invalid.drawing.data", new String[]{drawingFileName}, LocaleContextHolder.getLocale());
            errors.add(ValidationError.builder().pointer("drawingData").message(message).build());
        }
        if (!result.isValidSize()){
            String message = messageSource.getMessage("invalid.drawing.size", new String[]{drawingFileName}, LocaleContextHolder.getLocale());
            errors.add(ValidationError.builder().pointer("drawingData").message(message).build());
        }
        return CollectionUtils.isEmpty(errors) ? null : errors;
    }
}
