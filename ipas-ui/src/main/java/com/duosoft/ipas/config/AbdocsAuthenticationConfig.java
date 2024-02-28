package com.duosoft.ipas.config;

import bg.duosoft.abdocs.model.AbdocsToken;
import bg.duosoft.abdocs.model.response.ApiTokenResponse;
import bg.duosoft.abdocs.service.AbdocsAuthenticator;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.security.bpo.authorization.domain.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Component
public class AbdocsAuthenticationConfig implements AbdocsAuthenticator {
    private static final String ABDOCS_SESSION_TOKEN = "ABDOCS_TOKEN";

    @Autowired
    private AbdocsService abdocsService;

    public String getToken() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();
        HttpSession httpSession = request.getSession();
        AbdocsToken token = (AbdocsToken) httpSession.getAttribute(ABDOCS_SESSION_TOKEN);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (token == null || token.getExpireDate().compareTo(LocalDateTime.now()) < 0) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String username = userDetails.getUsername();
            ApiTokenResponse abdocsToken = abdocsService.selectApiToken(username);
            LocalDateTime expireDateTime = DateUtils.convertToLocalDatTime(abdocsToken.getExpires());
            token = new AbdocsToken(abdocsToken.getToken(), expireDateTime);
            httpSession.setAttribute(ABDOCS_SESSION_TOKEN, token);
        }
        return token.getToken();
    }

    @Override
    public void invalidateToken() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();
        HttpSession httpSession = request.getSession();
        httpSession.removeAttribute(ABDOCS_SESSION_TOKEN);
    }
}
