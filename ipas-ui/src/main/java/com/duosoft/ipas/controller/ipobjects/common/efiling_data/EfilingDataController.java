package com.duosoft.ipas.controller.ipobjects.common.efiling_data;

import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.model.util.PortalUserSearchResult;
import bg.duosoft.ipas.enums.UserGroupName;
import bg.duosoft.ipas.integration.portal.model.core.CPortalUser;
import bg.duosoft.ipas.integration.portal.service.PortalService;
import com.duosoft.ipas.util.EfilingDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Controller
@RequestMapping("/efiling-data")
@Slf4j
public class EfilingDataController {
    @Autowired
    private PortalService portalService;

    @GetMapping(value = "/portal-users-autocomplete", produces = "application/json")
    @ResponseBody
    public List<PortalUserSearchResult> userAutocomplete(@RequestParam String name) {
        List<CPortalUser> cPortalUsers = portalService.selectCachedUsersOfGroupByGroupName(UserGroupName.ROLE_TMEFILING_APPLICATION.getValue());
        List<PortalUserSearchResult> result = new ArrayList<>();
        StringBuilder fullNameBuilder = new StringBuilder();


        if (!CollectionUtils.isEmpty(cPortalUsers) && StringUtils.hasText(name)) {
            if (EfilingDataUtils.UNDEFINED_USER.toLowerCase().contains(name.toLowerCase())) {
                result.add(new PortalUserSearchResult(EfilingDataUtils.UNDEFINED_USER, EfilingDataUtils.UNDEFINED_USER));
            }
            for (CPortalUser portalUser : cPortalUsers) {
                fullNameBuilder.append(portalUser.getScreenName()).append("(").append(portalUser.getFirstName());
                if (StringUtils.hasText(portalUser.getMiddleName())) {
                    fullNameBuilder.append(" ").append(portalUser.getMiddleName());
                }
                fullNameBuilder.append(" ").append(portalUser.getLastName()).append(")");
                if (fullNameBuilder.toString().toLowerCase().contains(name.toLowerCase())) {
                    result.add(new PortalUserSearchResult(portalUser.getScreenName(), fullNameBuilder.toString()));
                }
                fullNameBuilder.setLength(0);
            }
            if (!CollectionUtils.isEmpty(result)) {
                result.sort(Comparator.comparing(PortalUserSearchResult::getLogin));
            }
        }
        return result;
    }
}
