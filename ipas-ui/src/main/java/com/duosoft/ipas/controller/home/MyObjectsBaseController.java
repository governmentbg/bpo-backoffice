package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.core.service.MyObjectsService;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.util.filter.MyObjectsFilter;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

public abstract class MyObjectsBaseController {


    @Autowired
    private MyObjectsService myObjectsService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private UserService userService;

    protected abstract void fillGenericAttributesInitActions(Model model, MyObjectsFilter filter);

    @GetMapping(value = "/list")
    public String getMyObjectsList(Model model, MyObjectsFilter filter) {
        if (Objects.isNull(filter.getResponsibleUser())) {
            filter.setResponsibleUser(SecurityUtils.getLoggedUserId());
        }
        filter.setNewlyAllocated(Objects.isNull(filter.getNewlyAllocated()) ? false : filter.getNewlyAllocated());
        filter.setPriorityRequest(Objects.isNull(filter.getPriorityRequest()) ? false : filter.getPriorityRequest());
        fillGenericAttributes(model, filter, null);
        return "home/my_objects/my_objects";
    }

    @RequestMapping(value = "/search")
    public String search(Model model, MyObjectsFilter filter) {
        fillGenericAttributes(model, filter, null);
        return "home/my_objects/my_objects_table::table";
    }

    @RequestMapping(value = "/update-table")
    public String updateMyObjectsTable(Model model, @RequestParam Integer tableCount, MyObjectsFilter filter) {
        fillGenericAttributes(model, filter, tableCount);
        return "home/my_objects/my_objects_table::table";
    }

    private void fillGenericAttributes(Model model, MyObjectsFilter filter, Integer tableCount) {
        fillGenericAttributesInitActions(model, filter);
        if (Objects.isNull(filter.getPage()))
            filter.setPage(MyObjectsFilter.DEFAULT_PAGE);
        if (Objects.isNull(filter.getPageSize()))
            filter.setPageSize(MyObjectsFilter.DEFAULT_PAGE_SIZE);

        if (Objects.nonNull(filter.getResponsibleUser())) {
            filter.setResponsibleUserName(userService.getUser(filter.getResponsibleUser()).getUserName());
        }

        List<IPObjectSimpleResult> myObjectsList = myObjectsService.getMyObjectsList(filter);

        Integer myObjectsTableCount;
        if (Objects.isNull(tableCount)) {
            myObjectsTableCount = myObjectsService.getMyObjectsCount(filter);
        } else {
            myObjectsTableCount = tableCount;
        }

        if (CollectionUtils.isEmpty(filter.getFileTypes())) {
            model.addAttribute("statusMap", statusService.selectIpObjectsStatusMapByUsersIncludeCount(userService.getDepartmentAndAuthorizedByUserIds(SecurityUtils.getLoggedUserId())));
        } else {
            model.addAttribute("statusMap", statusService.selectIpObjectsStatusMapByUsersAndFileTypesIncludeCount(userService.getDepartmentAndAuthorizedByUserIds(SecurityUtils.getLoggedUserId()), filter.getFileTypes()));
        }

        model.addAttribute("myObjectsList", myObjectsList);
        model.addAttribute("myObjectsCount", myObjectsTableCount);
        model.addAttribute("myObjectsFilter", filter);
    }

}
