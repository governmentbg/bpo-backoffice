package com.duosoft.ipas.config.security;

import bg.duosoft.abdocs.exception.AbdocsServiceException;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.security.bpo.authorization.domain.UserDetails;
import bg.duosoft.security.bpo.authorization.domain.impl.UserDetailsImpl;
import bg.duosoft.security.bpo.authorization.token.BPOAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * User: ggeorgiev
 * Date: 18.6.2019 г.
 * Time: 18:19
 */
@Slf4j
@Component("delegatePermissionFilter")
public class IpasPermissionFilter extends GenericFilterBean {

    @Autowired
    private UserService userService;

    @Autowired
    private AbdocsService abdocsServiceAdmin;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.trace("doFilter[IpasPermissionFilter] START");
        Authentication oldAuthentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (oldAuthentication != null &&
                oldAuthentication.getPrincipal() != null &&
                oldAuthentication.getPrincipal() instanceof UserDetailsImpl ?
                ((UserDetailsImpl) oldAuthentication.getPrincipal()) :
                null);
        if (userDetails != null && !userDetails.isFullyInitialized()) {
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            String userName = userDetails.getUsername();
            User user = userService.getUserByLogin(userName);
            if (user == null) {
                throw new AuthorizationServiceException("User " + userName + " does not exist in IPAS");
            } else if (Objects.equals(true, user.getIndInactive())) {
                throw new DisabledException("User is disabled!!!!");
            }
            List<UserDetails> authorizedByUsers = getAuthorizedByUsers(userName);

            Set<GrantedAuthority> permissions = new HashSet<>();

            permissions.addAll(authorities);//adding the portal's permissions to the list of permissions!!!
            permissions.addAll(getAuthorities(user.getUserId()));//adding user's roles
            permissions.addAll(authorizedByUsers.stream().map(r -> r.getAuthorities()).flatMap(r -> r.stream()).collect(Collectors.toList()));//the roles of the users that has authorized the current user

            userDetails.setRoles(permissions.stream().map(r -> r.getAuthority()).collect(Collectors.toList()));
            userDetails.setFullyInitialized(true);
            userDetails.setUserId(user.getUserId().longValue());
            userDetails.setAuthorizedByUsers(authorizedByUsers);




            BPOAuthenticationToken authentication = new BPOAuthenticationToken(userDetails.getUsername(), "", oldAuthentication.getDetails(), userDetails, permissions, true);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.trace(" Created  fullyInitialized BPOAuthenticationToken :: [" + authentication + "] ");


        }
        log.trace("doFilter[IpasPermissionFilter] END");
        filterChain.doFilter(servletRequest, servletResponse);
    }
    private List<UserDetails> getAuthorizedByUsers(String userName) {
        List<UserDetails> substituteUsers = new ArrayList<>();
        try {
            List<String> substituteUserNames = abdocsServiceAdmin.selectSubstituteUsers(userName);
            if (!CollectionUtils.isEmpty(substituteUserNames)) {
                substituteUsers = substituteUserNames
                        .stream()
                        .map(userService::getUserByLogin)
                        .filter(Objects::nonNull)
                        .filter(u -> !Objects.equals(true, u.getIndInactive()))
                        .map(this::createUserDetails)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            logger.error("Cannot read the authorizedBy users from abdocs!", e);
        }
        return substituteUsers;
    }
    private UserDetails createUserDetails(User u) {
        boolean active = !Objects.equals(true, u.getIndInactive());
        List<GrantedAuthority> authorities = getAuthorities(u.getUserId());
        UserDetailsImpl res = new UserDetailsImpl(u.getLogin(), "", active, active, active, active, authorities, false);
        res.setEmailAdmin(u.getEmail());
        res.setFullName(u.getFullName());
        res.setUserId(u.getUserId().longValue());
        res.setRoles(authorities == null ? null : authorities.stream().map(r -> r.getAuthority()).collect(Collectors.toList()));
        return res;
    }
    private List<GrantedAuthority> getAuthorities(Integer userId) {
          return  userService.getRolesPerUser(userId).stream()
//                            .filter(r -> r != SecurityRole.MarkUpdate)//Removing MarkEdit, because of the test!
                        .flatMap(r -> Stream.of(r, "ROLE_" + r))
                        .distinct()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
    }
}
