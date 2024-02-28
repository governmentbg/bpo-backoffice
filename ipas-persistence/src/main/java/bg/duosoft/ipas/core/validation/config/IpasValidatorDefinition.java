package bg.duosoft.ipas.core.validation.config;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IpasValidatorDefinition {
    Class<? extends IpasValidator>[] value() default {};
}