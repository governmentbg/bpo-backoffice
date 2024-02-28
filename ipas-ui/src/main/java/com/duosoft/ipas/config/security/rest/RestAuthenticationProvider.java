package com.duosoft.ipas.config.security.rest;

import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.properties.PropertyAccess;
import bg.duosoft.security.bpo.authorization.domain.UserDetails;
import bg.duosoft.security.bpo.authorization.domain.impl.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.duosoft.ipas.config.security.rest.RestSecurityConfiguration.REST_SERVICES_ROLE_NAME;

@Slf4j
public class RestAuthenticationProvider implements AuthenticationProvider {
    public static final String REST_USERNAME_ATTRIBUTE = "restusername";
    private static Logger logger = LoggerFactory.getLogger(RestAuthenticationProvider.class);
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PropertyAccess propertyAccess;
    @Autowired
    private RestLoginUtils loginUtils;

    /**
     * Validates token against external service and get user details
     * Creates a new user with user/groups on authorities.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        log.trace("RestAuthenticationProvider :: authenticate ::START");

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String principal = (String) token.getPrincipal();
        boolean isUserPasswordValid = loginUtils.isUserPasswordValid(principal, (String) token.getCredentials());
        if (!isUserPasswordValid) {
            log.error("Invalid password...Principal:" + principal);
            throw new BadCredentialsException("Bad username or password");
        }

        String restUsername = (String) request.getAttribute(REST_USERNAME_ATTRIBUTE);
        log.trace("RestAuthenticationProvider :: authenticate ::  principal [" + principal + "] requestUser[" + restUsername + "]");
        if (restUsername == null) {
            log.error("Cannot get restUsername");
            throw new AuthenticationCredentialsNotFoundException("No username in the request!");
        }

        UserDetails principalUser = getLiferayUserDetails(principal);
        User user = userService.getUserByLogin(restUsername);
        if (user == null) {
            log.error("No user with username " + restUsername + " exists in ipas DB!");
            throw new AuthenticationCredentialsNotFoundException("No user with username " + restUsername + " exists in ipas DB!");
        } else if (Objects.equals(true, user.getIndInactive())) {
            log.error("User is disabled " + restUsername);
            throw new DisabledException("User is disabled!");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        //if the principal user has the REST_SERVICES_ROLE_NAME it's added to the request user, otherwise the authorization will not succeed. The hasRole is executed on the logged user (the request one)
        if (principalUser.hasAuthority("ROLE_" + REST_SERVICES_ROLE_NAME)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + REST_SERVICES_ROLE_NAME));
            authorities.add(new SimpleGrantedAuthority("ROLE_" + propertyAccess.getIpasManagementRole()));
        }

        UserDetailsImpl ud = new UserDetailsImpl(user.getLogin(), "", true, true, true, true, authorities, false);
        ud.setFullName(user.getFullName());
        ud.setEmailAdmin(user.getEmail());
        log.trace("RestAuthenticationProvider :: authenticate :: END");
        return new RestAuthenticationToken(restUsername, authorities, token.getCredentials(), token.getDetails(), ud, true);

    }
    private UserDetails getLiferayUserDetails(String screenName) {
        try {
            return loginUtils.getUserByScreenName(screenName);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Cannot find user with name: " + screenName  + " ( " + e.getMessage() + " ) ", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == UsernamePasswordAuthenticationToken.class
                || authentication == PreAuthenticatedAuthenticationToken.class;
    }

}
