package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.NewlyAllocatedUserdocService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.persistence.model.nonentity.NewlyAllocatedUserdocSimpleResult;
import bg.duosoft.ipas.util.filter.NewlyAllocatedUserdocFilter;
import bg.duosoft.ipas.util.filter.sorter.NewlyAllocatedUserdocSorterUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/newly-allocated-userdocs")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).NewlyAllocatedUserdocs.code())")
public class NewlyAllocatedUserdocController {

    @Autowired
    private NewlyAllocatedUserdocService newlyAllocatedUserdocService;

    @Autowired
    private UserService userService;


    @GetMapping(value = "/list")
    public String getList(Model model, NewlyAllocatedUserdocFilter filter) {
        fillGenericAttributes(model, filter,true,null);
        return "home/newly_allocated_userdocs/newly_allocated_userdocs";
    }

    @RequestMapping(value = "/search")
    public String search(Model model,  NewlyAllocatedUserdocFilter filter) {
        fillGenericAttributes(model, filter,false,null);
        return "home/newly_allocated_userdocs/newly_allocated_userdocs_table :: table";
    }

    @RequestMapping(value = "/update-table")
    public String updateTable(Model model, @RequestParam Integer tableCount , NewlyAllocatedUserdocFilter filter) {
        fillGenericAttributes(model, filter,false,tableCount);
        return "home/newly_allocated_userdocs/newly_allocated_userdocs_table :: table";
    }


    private void fillGenericAttributes(Model model, NewlyAllocatedUserdocFilter filter, boolean fillFilters, Integer tableCount) {
        if (Objects.isNull(filter.getPage()))
            filter.setPage(NewlyAllocatedUserdocFilter.DEFAULT_PAGE);
        if (Objects.isNull(filter.getPageSize()))
            filter.setPageSize(NewlyAllocatedUserdocFilter.DEFAULT_PAGE_SIZE);
        if (Objects.isNull(filter.getSortOrder()))
            filter.setSortOrder(NewlyAllocatedUserdocFilter.DESC_ORDER);
        if (Objects.isNull(filter.getSortColumn()))
            filter.setSortColumn(NewlyAllocatedUserdocSorterUtils.USERDOC_DATE_CHANGED);
        if (!SecurityUtils.hasRights(SecurityRole.IpObjectsSearchForeignObjectsData))
            filter.setResponsibleUser(SecurityUtils.getLoggedUserId());

        List<NewlyAllocatedUserdocSimpleResult> newlyAllocatedUserdocs = newlyAllocatedUserdocService.selectNewlyAllocatedUserdocs(filter);

        Integer newlyAllocatedUserdocsCount;
        if(Objects.isNull(tableCount)){
            newlyAllocatedUserdocsCount= newlyAllocatedUserdocService.selectNewlyAllocatedUserdocsCount(filter);
        }else{
            newlyAllocatedUserdocsCount = tableCount;
        }

        if (Objects.nonNull(filter.getResponsibleUser())){
            User user = userService.getUser(filter.getResponsibleUser());
            filter.setResponsibleUserName(user.getUserName());
        }

        model.addAttribute("newlyAllocatedUserdocs", newlyAllocatedUserdocs);
        model.addAttribute("newlyAllocatedUserdocsCount", newlyAllocatedUserdocsCount);
        model.addAttribute("newlyAllocatedUserdocsFilter", filter);
        if (fillFilters){
            model.addAttribute("fileTypes",newlyAllocatedUserdocService.getUserdocObjectTypes(filter));
            model.addAttribute("userdocTypeMap", newlyAllocatedUserdocService.getUserdocTypes(filter));
        }
    }
}
