package bg.duosoft.ipas.core.validation.config;

import bg.duosoft.ipas.util.DefaultValue;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public interface DefaultValidation {

    default void rejectIfEmpty(List<ValidationError> errors, Object value, String pointer, String messageCode) {
        if (Objects.isNull(value)) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode(messageCode).build());
        }
    }

    default void rejectIfEmpty(List<ValidationError> errors, Object value, String pointer) {
        if (Objects.isNull(value)) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode("required.field").build());
        }
    }

    default <V> void rejectIfEmpty(List<ValidationError> errors, Object value, String pointer, String messageCode, V invalidValue) {
        if (Objects.isNull(value)) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode(messageCode).invalidValue(invalidValue).build());
        }
    }

    default void rejectIfEmptyString(List<ValidationError> errors, String value, String pointer, String messageCode) {
        if (StringUtils.isEmpty(value)) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode(messageCode).build());
        }
    }

    default <V> void rejectIfEmptyString(List<ValidationError> errors, String value, String pointer, String messageCode, V invalidValue) {
        if (StringUtils.isEmpty(value)) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode(messageCode).invalidValue(invalidValue).build());
        }
    }

    default boolean rejectIfEmptyString(List<ValidationError> errors, String value, String pointer) {
        if (StringUtils.isEmpty(value)) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode("required.field").build());
            return true;
        }
        return false;
    }

    default void rejectIfNotMatchRegex(List<ValidationError> errors, String value, String regex, String pointer, String messageCode) {
        if (!StringUtils.isEmpty(value) && !value.matches(regex)) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode(messageCode).build());
        }
    }

    default void rejectIfNotPositiveNumber(List<ValidationError> errors, Integer number, String pointer, String messageCode) {
        if (Objects.nonNull(number) && number < DefaultValue.MIN_POSITIVE_NUMBER) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode(messageCode).build());
        }
    }

    default <E> void rejectIfEmptyCollection(List<ValidationError> errors, List<E> value, String pointer, String messageCode) {
        if (CollectionUtils.isEmpty(value)) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode(messageCode).build());
        }
    }

    default <E> void rejectIfEmptyCollection(List<ValidationError> errors, List<E> value, String pointer) {
        if (CollectionUtils.isEmpty(value)) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode("required.field").build());
        }
    }

    default void rejectIfBothAreNotTrue(List<ValidationError> errors, Boolean first, Boolean second, String pointer, String messageCode) {
        if ((Objects.isNull(first) || !first) && (Objects.isNull(second) || !second)) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode(messageCode).build());
        }
    }

    default void rejectIfTrue(List<ValidationError> errors, Boolean value, String pointer, String messageCode) {
        if (Objects.nonNull(value) && value) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode(messageCode).build());
        }
    }

    default void rejectIfTrue(List<ValidationError> errors, Boolean value, String pointer, String messageCode, Object invalidValue) {
        if (Objects.nonNull(value) && value) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode(messageCode).invalidValue(invalidValue).build());
        }
    }

    default void rejectIfFalse(List<ValidationError> errors, Boolean value, String pointer, String messageCode) {
        if (Objects.nonNull(value) && !value) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode(messageCode).build());
        }
    }

    default void rejectIfFirstDateIsAfterSecond(List<ValidationError> errors, LocalDate firstDate, LocalDate secondDate, String pointer, String messageCode) {
        if (firstDate.isAfter(secondDate)) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode(messageCode).build());
        }
    }

    default void rejectIfFirstDateIsBeforeSecond(List<ValidationError> errors, LocalDate firstDate, LocalDate secondDate, String pointer, String messageCode) {
        if (firstDate.isBefore(secondDate)) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode(messageCode).build());
        }
    }

    default void rejectIfBothDatesAreEquals(List<ValidationError> errors, LocalDate firstDate, LocalDate secondDate, String pointer, String messageCode) {
        if (firstDate.isEqual(secondDate)) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode(messageCode).build());
        }
    }

    default void rejectIfFirstDateIsAfterSecond(List<ValidationError> errors, Date firstDate, Date secondDate, String pointer, String messageCode) {
        if (firstDate.after(secondDate)) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode(messageCode).build());
        }
    }

    default void rejectIfFirstDateIsBeforeSecond(List<ValidationError> errors, Date firstDate, Date secondDate, String pointer, String messageCode) {
        if (firstDate.before(secondDate)) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode(messageCode).build());
        }
    }

}
