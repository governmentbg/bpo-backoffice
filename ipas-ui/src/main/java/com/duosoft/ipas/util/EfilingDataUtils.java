package com.duosoft.ipas.util;

import bg.duosoft.security.bpo.authorization.domain.UserDetails;
import com.duosoft.ipas.config.security.rest.RestLoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class EfilingDataUtils {

    public static final String UNDEFINED_USER = "**UNDEFINED_USER**";

    public static String getFormattedName(String logUserName, RestLoginUtils restLoginUtils) {
        if (!StringUtils.hasText(logUserName)) {
            return null;
        }
        if (logUserName.equals(UNDEFINED_USER)){
            return UNDEFINED_USER;
        }

        StringBuilder formattedNameBuilder = new StringBuilder(logUserName);
        try {
            UserDetails userByScreenName = restLoginUtils.getUserByScreenName(logUserName);
            formattedNameBuilder.append("(").append(userByScreenName.getFullName()).append(")");
        } catch (Exception e) {
            log.error("Error on select liferay user by login name " + logUserName + "!");
            log.error(e.getMessage());
        }
        return formattedNameBuilder.toString();
    }
}
