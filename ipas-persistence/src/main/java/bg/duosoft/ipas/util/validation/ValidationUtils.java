package bg.duosoft.ipas.util.validation;

import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

public class ValidationUtils {

    public static boolean isPointerExists(List<ValidationError> errors, String pointer) {
        if (CollectionUtils.isEmpty(errors))
            return false;

        ValidationError result = errors.stream()
                .filter(validationError -> validationError.getPointer().equals(pointer))
                .findFirst()
                .orElse(null);

        return result != null;
    }

    public static String getMessageCodeByPointer(List<ValidationError> errors, String pointer) {
        if (CollectionUtils.isEmpty(errors))
            return null;

        ValidationError result = errors.stream()
                .filter(validationError -> validationError.getPointer().equals(pointer))
                .findFirst()
                .orElse(null);
        if (Objects.isNull(result))
            return null;

        return result.getMessageCode();
    }
}
