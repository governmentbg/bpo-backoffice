package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.LastActionService;
import bg.duosoft.ipas.core.service.nomenclature.FileTypeService;

import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.persistence.model.nonentity.LastActionsResult;
import bg.duosoft.ipas.util.filter.LastActionFilter;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping("/last-action")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).IpObjectsViewLastActionsList.code())")
public class LastActionController {

    @Autowired
    private LastActionService lastActionService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/list")
    public String getLastActions(Model model, LastActionFilter filter) {
        fillGenericAttributes(model, filter,true,null);
        return "home/lastaction/last_action";
    }

    @RequestMapping(value = "/search")
    public String search(Model model,  LastActionFilter filter) {
        fillGenericAttributes(model, filter,false,null);
        return "home/lastaction/last_action_table :: table";
    }

    @RequestMapping(value = "/update-table")
    public String updateLastActionTable(Model model, @RequestParam Integer tableCount , LastActionFilter filter) {
        fillGenericAttributes(model, filter,false,tableCount);
        return "home/lastaction/last_action_table :: table";
    }

    private void fillGenericAttributes(Model model, LastActionFilter filter,boolean fillFilters,Integer tableCount) {
        if (Objects.isNull(filter.getPage()))
            filter.setPage(LastActionFilter.DEFAULT_PAGE);
        if (Objects.isNull(filter.getPageSize()))
            filter.setPageSize(LastActionFilter.DEFAULT_PAGE_SIZE);
        if (!SecurityUtils.hasRights(SecurityRole.IpObjectsSearchForeignObjectsData))
            filter.setResponsibleUser(SecurityUtils.getLoggedUserId());

        List<LastActionsResult> lastActionList = lastActionService.getLastActionList(filter);

        Integer lastActionsTableCount;
        if(Objects.isNull(tableCount)){
            lastActionsTableCount= lastActionService.getLastActionCount(filter);
        }else{
            lastActionsTableCount = tableCount;
        }

        if (Objects.nonNull(filter.getResponsibleUser())){
            User user = userService.getUser(filter.getResponsibleUser());
            filter.setResponsibleUserName(user.getUserName());
        }

        if (Objects.nonNull(filter.getCaptureUser())){
            User user = userService.getUser(filter.getCaptureUser());
            filter.setCaptureUserName(user.getUserName());
        }

        model.addAttribute("lastActionList", lastActionList);
        model.addAttribute("lastActionFilter", filter);
        model.addAttribute("lastActionCount", lastActionsTableCount);
        if (fillFilters){
            model.addAttribute("fileTypesMap", lastActionService.getLastActionFileTypes(filter));
            model.addAttribute("userdocTypeMap",lastActionService.getLastActionUserdocTypes(filter));
        }
    }

}
