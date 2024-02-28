package bg.duosoft.ipas.core.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: ggeorgiev
 * Date: 26.04.2021
 * Time: 17:10
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RestGenerationIgnore {
}
