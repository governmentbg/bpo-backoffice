package com.duosoft.ipas.config.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * User: ggeorgiev
 * Date: 14.5.2019 г.
 * Time: 14:41
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@PropertySource("classpath:/security/liferay.properties")
@PropertySource("classpath:/security/base-security.properties")
@PropertySource("classpath:/security/cas/cas.properties")
@PropertySource("classpath:/security/mock/mock-security.properties")
@ComponentScan("bg.duosoft.security")
public class SecurityConfig {
    @Bean
    public FilterRegistrationBean registrationIpasPermissionFilter(IpasPermissionFilter ipasPermissionFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(ipasPermissionFilter);
        registration.setEnabled(false);
        return registration;
    }
}
