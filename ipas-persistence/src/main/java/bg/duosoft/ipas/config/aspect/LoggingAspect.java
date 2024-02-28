package bg.duosoft.ipas.config.aspect;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect implements Ordered {
    @Pointcut("execution(public * bg.duosoft.ipas.core.service..*.*(..))")
    public void allMethods() {
    }

    @Before("allMethods()")
    public void logAllMethods(JoinPoint joinPoint) {
        if (log.isTraceEnabled()) {
            Object[] args = joinPoint.getArgs();
            StringBuilder b = new StringBuilder();
            b.append("Calling " + joinPoint.getSignature() + "\n");
            b.append("Arguments[\n");
            if (args != null || args.length > 0) {
                Gson gson = new Gson();
                Arrays.stream(args).forEach(a -> b.append(gson.toJson(a) + "\n"));
            }
            b.append("]");
            log.trace(b.toString());
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
