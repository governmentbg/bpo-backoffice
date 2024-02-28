package com.duosoft.ipas.config.security.rest;

import bg.duosoft.security.bpo.authorization.domain.UserDetails;
import bg.duosoft.security.bpo.liferay.service.LiferayCasService;
import bg.duosoft.security.bpo.liferay.service.LiferayUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * User: ggeorgiev
 * Date: 09.04.2021
 * Time: 15:52
 */
@Component
public class RestLoginUtils {
    @Autowired
    private LiferayUserService liferayUserService;
    @Autowired
    private LiferayCasService liferayCasService;

    @Cacheable(value = "portalValidateUserPasswordCache", cacheManager = "uiCacheManager", unless = "#result == false")
    public boolean isUserPasswordValid(String username, String password) {
        //trying to get ticket granting ticket 2 times
        String ticketGrantingTicket;
        try {
            ticketGrantingTicket = liferayCasService.getTicketGrantingTicket(username, password);
        } catch (Exception e) {
            ticketGrantingTicket = liferayCasService.getTicketGrantingTicket(username, password);
        }
        return !StringUtils.isEmpty(ticketGrantingTicket);
    }

    @Cacheable(value = "portalUserByScreenNameCache", cacheManager = "uiCacheManager")
    public UserDetails getUserByScreenName(String username) {
        return liferayUserService.loadUserByUsername(username);
    }
}
