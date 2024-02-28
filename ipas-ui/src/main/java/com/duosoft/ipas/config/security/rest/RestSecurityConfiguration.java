package com.duosoft.ipas.config.security.rest;

import bg.duosoft.security.bpo.authentication.filter.PermissionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Arrays;

/**
 * User: ggeorgiev
 * Date: 20.5.2019 г.
 * Time: 13:51
 */
@Configuration
@Order(1)
public class RestSecurityConfiguration extends WebSecurityConfigurerAdapter {
    public static final String REST_SERVICES_ROLE_NAME = "IPAS_REST_SERVICES";
    @Autowired
    private PermissionFilter permissionFilter;
    @Autowired
    private RestRequestUserFilter restRequestUserFilter;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public FilterRegistrationBean registerContentCacheFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(restRequestUserFilter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .antMatcher("/rest/**").httpBasic().authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/rest/**")
                .hasRole(REST_SERVICES_ROLE_NAME)
                .and()
                .addFilterBefore(restRequestUserFilter, BasicAuthenticationFilter.class)//za da moga da procheta username-a ot request body-to, trqbva vyv filter da wrap-na HttpServletRequest-a, da mu vzema InputStream-a da procheta user-a i posle na chain-a da prodam wrap--natiq request!
                .addFilterAfter(permissionFilter, BasicAuthenticationFilter.class);

    }

    @Bean("restAuthenticationProvider")
    public RestAuthenticationProvider restAuthenticationProvider() {
        return new RestAuthenticationProvider();
    }

    @Override
    protected AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(restAuthenticationProvider()));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(restAuthenticationProvider());
    }

}
