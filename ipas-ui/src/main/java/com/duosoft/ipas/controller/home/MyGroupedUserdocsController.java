package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.core.service.MyGroupedUserdocsService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.util.filter.MyUserdocsFilter;
import bg.duosoft.ipas.util.filter.sorter.MyUserdocsSorterUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/my-grouped-userdocs")
public class MyGroupedUserdocsController {

    @Autowired
    private MyGroupedUserdocsService myGroupedUserdocsService;

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping(value = "/list")
    public String getMyObjectsList(Model model,@RequestParam String userdocGroupName, MyUserdocsFilter filter) {
        if (Objects.isNull(filter.getResponsibleUser())){
            filter.setResponsibleUser(SecurityUtils.getLoggedUserId());
        }
        filter.setUserdocGroupName(userdocGroupName);
        filter.setInProduction(Objects.isNull(filter.getInProduction())?true:filter.getInProduction());
        filter.setFinished(Objects.isNull(filter.getFinished())?false:filter.getFinished());
        filter.setNewlyAllocated(Objects.isNull(filter.getNewlyAllocated())?false:filter.getNewlyAllocated());
        model.addAttribute("userdocTypeMap", userdocTypesService.selectDistinctUserdocTypeMapForUserdocsByUsersAndUserdocGroupName(userService.getDepartmentAndAuthorizedByUserIds(SecurityUtils.getLoggedUserId()),userdocGroupName));
        fillGenericAttributes(model, filter,true,null);
        return "home/my_grouped_userdocs/my_grouped_userdocs";
    }

    @RequestMapping(value = "/search")
    public String search(Model model , MyUserdocsFilter filter) {
        fillGenericAttributes(model, filter,false,null);
        return "home/my_grouped_userdocs/my_grouped_userdocs_table::table";
    }


    @RequestMapping(value = "/update-table")
    public String updateMyObjectsTable(Model model ,@RequestParam Integer tableCount,MyUserdocsFilter filter) {
        fillGenericAttributes(model, filter,false,tableCount);
        return "home/my_grouped_userdocs/my_grouped_userdocs_table::table";
    }

    private void fillGenericAttributes(Model model, MyUserdocsFilter filter,boolean fillFilters,Integer tableCount) {
        if(Objects.nonNull(filter.getResponsibleUser())){
            filter.setResponsibleUserName(userService.getUser(filter.getResponsibleUser()).getUserName());
        }

        if (Objects.isNull(filter.getPage()))
            filter.setPage(MyUserdocsFilter.DEFAULT_PAGE);
        if (Objects.isNull(filter.getPageSize()))
            filter.setPageSize(MyUserdocsFilter.DEFAULT_PAGE_SIZE);
        if (Objects.isNull(filter.getSortOrder()))
            filter.setSortOrder(MyUserdocsFilter.DESC_ORDER);
        if (Objects.isNull(filter.getSortColumn()))
            filter.setSortColumn(MyUserdocsSorterUtils.FILING_DATE);

        List<UserdocSimpleResult> resultList = myGroupedUserdocsService.getMyUserdocsList(filter);

        Integer myGroupedUserdocsTableCount;
        if(Objects.isNull(tableCount)){
            myGroupedUserdocsTableCount= myGroupedUserdocsService.getMyUserdocsCount(filter);
        }else{
            myGroupedUserdocsTableCount = tableCount;
        }

        model.addAttribute("myUserdocsList", resultList);
        model.addAttribute("myUserdocsCount", myGroupedUserdocsTableCount);
        model.addAttribute("myUserdocsFilter", filter);
        if (fillFilters){
            model.addAttribute("statusMap",myGroupedUserdocsService.getGroupedUserdocStatuses(filter));
            model.addAttribute("fileTypes",myGroupedUserdocsService.getGroupedUserdocObjectType(filter));
        }
    }

}
