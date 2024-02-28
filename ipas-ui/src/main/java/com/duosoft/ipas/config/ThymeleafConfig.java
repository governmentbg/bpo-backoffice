package com.duosoft.ipas.config;

import bg.duosoft.ipas.util.security.SecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Collections;
import java.util.Set;

/**
 * User: ggeorgiev
 * Date: 29.7.2019 г.
 * Time: 14:55
 */
@Configuration
public class ThymeleafConfig {
    @Bean
    public SecurityUtilDialect securityUtilDialect() {
        return new SecurityUtilDialect();
    }

    public static class SecurityUtilDialect  extends AbstractDialect implements IExpressionObjectDialect{
        public SecurityUtilDialect() {
            super("Security Util Dialect");
        }

        @Override
        public IExpressionObjectFactory getExpressionObjectFactory() {
            return new IExpressionObjectFactory() {

                @Override
                public Set<String> getAllExpressionObjectNames() {
                    return Collections.singleton("securityUtil");
                }

                @Override
                public Object buildObject(IExpressionContext context,
                                          String expressionObjectName) {
                    return new SecurityUtils();
                }

                @Override
                public boolean isCacheable(String expressionObjectName) {
                    return true;
                }
            };
        }
    }
}
