package bg.duosoft.ipas.core.validation.mark.attachment;

import bg.duosoft.ipas.core.model.mark.CMarkAttachment;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.image.ImageValidatorBase;
import bg.duosoft.ipas.enums.ImageFormat;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static bg.duosoft.ipas.enums.ImageFormat.JPEG;
import static bg.duosoft.ipas.enums.ImageFormat.PNG;

/**
 * User: ggeorgiev
 * Date: 11.3.2019 г.
 * Time: 18:09
 */
@Component
public class MarkImageValidator extends ImageValidatorBase implements IpasValidator<CMarkAttachment> {
    @Override
    public List<ValidationError> validate(CMarkAttachment obj, Object... additionalArgs) {
        Integer attachmentIndex = Objects.isNull(additionalArgs) ? null : (Integer) additionalArgs[0];
        Integer maxFileSize = Objects.isNull(additionalArgs) ? null : (Integer) additionalArgs[1];
        Set<ImageFormat> supportedFormats = Set.of(JPEG, PNG);
        ImageValidationResult result = isValid(obj.getData(), supportedFormats, maxFileSize);

        List<ValidationError> errors = new ArrayList<>();
        if (!result.isValidType())
            errors.add(ValidationError.builder().pointer("imageFile-" + attachmentIndex).messageCode("invalid.logo.data").invalidValue(result.getFormatType()).build());
        if (!result.isValidSize())
            errors.add(ValidationError.builder().pointer("imageFile-" + attachmentIndex).messageCode("invalid.logo.size").invalidValue(result.getSize()).build());

        return CollectionUtils.isEmpty(errors) ? null : errors;
    }
}
