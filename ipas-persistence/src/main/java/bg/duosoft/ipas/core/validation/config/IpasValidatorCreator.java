package bg.duosoft.ipas.core.validation.config;

import bg.duosoft.ipas.core.validation.JSR303Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class IpasValidatorCreator {
    @Autowired
    private ApplicationContext applicationContext;


    public <T> IpasValidator<T> create(boolean invokeJsr303Validation, Class<? extends IpasValidator<T>>... validators) {
        return new SimpleValidatorGroupImpl<>(invokeJsr303Validation, validators);
    }

    public <U, V> IpasTwoArgsValidator<U, V> createTwoArgsValidator(boolean invokeJsr303Validation, Class<? extends IpasTwoArgsValidator<U, V>>... validators) {
        return new SimpleTwoArgsValidatorGroupImpl<>(invokeJsr303Validation, validators);
    }

    private class SimpleValidatorGroupImpl<T> implements IpasValidator<T> {
        private List<Class<? extends IpasValidator<T>>> validators;
        private boolean invokeJsr303Validation;

        private SimpleValidatorGroupImpl(boolean invokeJsr303Validation, Class<? extends IpasValidator<T>>... validators) {
            this.invokeJsr303Validation = invokeJsr303Validation;
            this.validators = validators == null || validators.length == 0 ? null : Arrays.asList(validators);
        }

        @Override
        public List<ValidationError> validate(T obj, Object... args) {
            List<ValidationError> errors = new ArrayList<>();
            if (validators != null && validators.size() > 0) {
                errors = validators.stream().map(v -> applicationContext.getBean(v)).map(v -> (IpasValidator<T>) v).map(v -> v.validate(obj, args)).filter(Objects::nonNull).map(r -> r.stream()).flatMap(Function.identity()).collect(Collectors.toList());
            }
            if (invokeJsr303Validation && (validators == null || !validators.contains(JSR303Validator.class))) {
                List<ValidationError> jsr302Validations = applicationContext.getBean(JSR303Validator.class).validate(obj);
                if (!CollectionUtils.isEmpty(jsr302Validations)) {
                    errors.addAll(jsr302Validations);
                }
            }
            return errors == null || errors.size() == 0 ? null : errors;
        }
    }

    private class SimpleTwoArgsValidatorGroupImpl<U, V> implements IpasTwoArgsValidator<U, V> {
        private List<Class<? extends IpasTwoArgsValidator<U, V>>> validators;
        private boolean invokeJsr303Validation;

        private SimpleTwoArgsValidatorGroupImpl(boolean invokeJsr303Validation, Class<? extends IpasTwoArgsValidator<U, V>>... validators) {
            this.invokeJsr303Validation = invokeJsr303Validation;
            this.validators = validators == null || validators.length == 0 ? null : Arrays.asList(validators);
        }

        @Override
        public List<ValidationError> validate(U obj, V arg, Object... additionalArgs) {
            List<ValidationError> errors = new ArrayList<>();
            if (validators != null && validators.size() > 0) {
                errors = validators.stream().map(v -> applicationContext.getBean(v)).map(v -> (IpasTwoArgsValidator<U, V>) v).map(v -> v.validate(obj, arg, additionalArgs)).filter(Objects::nonNull).map(r -> r.stream()).flatMap(Function.identity()).collect(Collectors.toList());
            }
            if (invokeJsr303Validation && (validators == null || !validators.contains(JSR303Validator.class))) {
                List<ValidationError> jsr302Validations = applicationContext.getBean(JSR303Validator.class).validate(obj);
                if (!CollectionUtils.isEmpty(jsr302Validations)) {
                    errors.addAll(jsr302Validations);
                }
            }
            return errors == null || errors.size() == 0 ? null : errors;
        }
    }

}
