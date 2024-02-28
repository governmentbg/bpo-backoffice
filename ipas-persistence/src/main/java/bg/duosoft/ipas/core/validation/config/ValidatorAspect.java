package bg.duosoft.ipas.core.validation.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Aspect
public class ValidatorAspect implements Ordered {
    @Autowired
    private IpasValidatorCreator ipasValidatorCreator;


    @Pointcut("execution(public * bg.duosoft.ipas.core.service..*.*(..))")
    public void allMethods() {
    }

    /**
     * intercepts all the methods with {@link IpasValidatorDefinition} annotation and executes the validations. If the method has more than one argument, the arguments are added as additionalArguments to the validator!
     *
     * @param joinPoint
     */
    @Before("allMethods() && @annotation(bg.duosoft.ipas.core.validation.config.IpasValidatorDefinition)")
    public void validateAnnotatedMethods(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            Object arg = joinPoint.getArgs()[0];
            Object[] additionalArgs = joinPoint.getArgs().length == 1 ? null : Arrays.copyOfRange(joinPoint.getArgs(), 1, joinPoint.getArgs().length);
            Optional<List<ValidationError>> errors = validate(method.getAnnotations(), arg, additionalArgs);
            if (errors.isPresent() && !CollectionUtils.isEmpty(errors.get())) {
                throw new IpasValidationException(errors.get());
            }
        }
    }

    /**
     * checks every one of the parameters in the invoked service method for {@link IpasValidatorDefinition} annotation. If there are any, it's validations are getting invoked
     *
     * @param joinPoint
     */
    @Before("allMethods()")
    public void validateAllMethods(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Annotation[][] allAnnotations = method.getParameterAnnotations();
        if (allAnnotations != null && allAnnotations.length > 0) {
            List<ValidationError> errors = new ArrayList<>();
            for (int argCnt = 0; argCnt < allAnnotations.length; argCnt++) {
                Annotation[] parameterAnnotations = allAnnotations[argCnt];
                Object arg = joinPoint.getArgs()[argCnt];

                Optional<List<ValidationError>> validationErrors = validate(parameterAnnotations, arg);
                validationErrors.ifPresent(errors::addAll);
            }
            if (errors.size() > 0) {
                throw new IpasValidationException(errors);
            }
        }
    }

    private Optional<List<ValidationError>> validate(Annotation[] parameterAnnotations, Object arg, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        if (parameterAnnotations.length > 0) {
            Optional<IpasValidatorDefinition> validatorAnnotation = Arrays.stream(parameterAnnotations).filter(a -> a instanceof IpasValidatorDefinition).map(a -> (IpasValidatorDefinition) a).findFirst();
            if (validatorAnnotation.isPresent()) {
                Class<? extends IpasValidator<Object>>[] validationClasses = (Class<? extends IpasValidator<Object>>[]) validatorAnnotation.get().value();
                IpasValidator<Object> validator = ipasValidatorCreator.create(true, validationClasses);
                List<ValidationError> validate;
                if (additionalArgs != null && additionalArgs.length > 0) {
                    validate = validator.validate(arg, additionalArgs);
                } else {
                    validate = validator.validate(arg);
                }
                if (!CollectionUtils.isEmpty(validate))
                    errors = validate;
            }
        }
        return errors.size() == 0 ? Optional.empty() : Optional.of(errors);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
