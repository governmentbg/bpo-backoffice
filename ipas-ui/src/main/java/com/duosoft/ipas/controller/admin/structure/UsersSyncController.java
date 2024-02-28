package com.duosoft.ipas.controller.admin.structure;

import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.integration.portal.model.core.CPortalUser;
import bg.duosoft.ipas.integration.portal.service.PortalService;
import bg.duosoft.ipas.integration.portal.utils.PortalUserSyncResult;
import bg.duosoft.ipas.integration.portal.utils.PortalUserUtils;
import bg.duosoft.ipas.properties.PropertyAccess;
import bg.duosoft.ipas.util.filter.UsersSyncFilter;
import bg.duosoft.ipas.util.filter.sorter.UsersSyncSorterUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping(value = "/users/sync")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).UsersAndRolesView.code())")
public class UsersSyncController {

    @Autowired
    private PropertyAccess propertyAccess;

    @Autowired
    private PortalService portalService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String view(Model model) {
        UsersSyncFilter filter = new UsersSyncFilter(UsersSyncFilter.ASC_ORDER, UsersSyncSorterUtils.SYNC_STATUS);
        if (model.containsAttribute("filter")) {
            filter = (UsersSyncFilter) model.asMap().get("filter");
        }
        fillModelAttributes(model, filter);
        return "structure/sync/view";
    }

    @PostMapping(value = "/update-table")
    public String updateTable(Model model, UsersSyncFilter filter) {
        fillModelAttributes(model, filter);
        return "structure/sync/table";
    }

    @PostMapping(value = "/sync-user")
    public String synchronizeUser(RedirectAttributes redirectAttributes, @RequestParam String screenName, UsersSyncFilter filter) {
        List<CPortalUser> portalUsers = portalService.selectNotSynchronizedUsersByGroupName(propertyAccess.getIpasManagementRole());

        CPortalUser portalUser = null;
        if (!CollectionUtils.isEmpty(portalUsers)) {
            portalUser = portalUsers.stream().filter(user -> user.getScreenName().equals(screenName)).findFirst().orElse(null);
        }

        User synchronizedUser = null;
        if (Objects.nonNull(portalUser)) {
            try {
                User ipasUser = userService.getUserByLogin(screenName);
                synchronizedUser = userService.synchronizeUser(portalUser, ipasUser);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            PortalUserSyncResult result = PortalUserUtils.convertToSynchronizedUserResult(synchronizedUser, portalUser);
            redirectAttributes.addFlashAttribute("synchronizedUsers", Collections.singletonList(result));
        }

        redirectAttributes.addFlashAttribute("filter", filter);
        return "redirect:/users/sync";
    }

    @PostMapping(value = "/sync-all-users")
    public String synchronizeAllUsers(RedirectAttributes redirectAttributes, UsersSyncFilter filter) {
        List<CPortalUser> portalUsers = portalService.selectNotSynchronizedUsersByGroupName(propertyAccess.getIpasManagementRole());
        if (!CollectionUtils.isEmpty(portalUsers)) {
            List<PortalUserSyncResult> resultList = new ArrayList<>();
            for (CPortalUser portalUser : portalUsers) {
                User synchronizedUser = null;
                try {
                    String screenName = portalUser.getScreenName();
                    User ipasUser = userService.getUserByLogin(screenName);
                    synchronizedUser = userService.synchronizeUser(portalUser, ipasUser);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                resultList.add(PortalUserUtils.convertToSynchronizedUserResult(synchronizedUser, portalUser));
            }
            redirectAttributes.addFlashAttribute("synchronizedUsers", resultList);
        }
        redirectAttributes.addFlashAttribute("filter", filter);
        return "redirect:/users/sync";
    }

    private void fillModelAttributes(Model model, UsersSyncFilter filter) {
        List<CPortalUser> users = portalService.selectNotSynchronizedUsersByGroupName(propertyAccess.getIpasManagementRole());
        if (!CollectionUtils.isEmpty(users)) {
            UsersSyncSorterUtils.sort(users, filter);
        }
        model.addAttribute("users", users);
        model.addAttribute("count", CollectionUtils.isEmpty(users) ? 0 : users.size());
        model.addAttribute("usersFilter", filter);
    }

}
