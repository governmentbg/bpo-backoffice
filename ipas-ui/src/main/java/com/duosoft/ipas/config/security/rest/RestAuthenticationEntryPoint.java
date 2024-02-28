package com.duosoft.ipas.config.security.rest;

import bg.duosoft.ipas.rest.custommodel.RestApiError;
import bg.duosoft.ipas.util.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * User: Georgi
 * Date: 11.5.2020 г.
 * Time: 23:44
 */
@Component("restAuthenticationEntryPoint")
@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException {
        log.error("Error authenticating rest user...", authenticationException);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String json = JsonUtil.createJson(new RestApiError(authenticationException.getMessage(), authenticationException.getClass().getCanonicalName(), new Date(), HttpStatus.UNAUTHORIZED.value()));
        response.getOutputStream().println(json);
    }
}
