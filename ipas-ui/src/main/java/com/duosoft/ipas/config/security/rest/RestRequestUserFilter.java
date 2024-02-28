package com.duosoft.ipas.config.security.rest;

import bg.duosoft.ipas.util.json.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.duosoft.ipas.config.security.rest.RestAuthenticationProvider.REST_USERNAME_ATTRIBUTE;

/**
 * User: Georgi
 * Date: 11.5.2020 г.
 * Time: 14:24
 */
@Component
@Slf4j
public class RestRequestUserFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpRequestWrapper req = new HttpRequestWrapper((HttpServletRequest) servletRequest);
        String content = IOUtils.toString(req.getInputStream());
        try {
            RestUsernameRequest json = JsonUtil.readJson(content, RestUsernameRequest.class);
            req.setAttribute(REST_USERNAME_ATTRIBUTE, json.getUsername());
        } catch (Exception e) {
            log.warn("Cannot create RestApiRequest from the request's content. Request:" + content);
        }

        filterChain.doFilter(req, servletResponse);
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RestUsernameRequest {
        private String username;
    }
}

