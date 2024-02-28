package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.core.service.UserdocWaitingTermsService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.util.filter.MyUserdocsFilter;
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
@RequestMapping("/userdoc-waiting-terms")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).IpObjectsUserdocWaitingTermsList.code())")
public class UserdocWaitingTermsController{

    @Autowired
    private UserdocWaitingTermsService userdocWaitingTermsService;

    @GetMapping(value = "/list")
    public String getMyObjectsList(Model model,  MyUserdocsFilter filter) {
        fillGenericAttributes(model, filter,true,null);
        return "home/userdoc_waiting_terms/userdoc_waiting_terms";
    }

    @RequestMapping(value = "/search")
    public String search(Model model ,MyUserdocsFilter filter) {
        fillGenericAttributes(model, filter,false,null);
        return "home/userdoc_waiting_terms/userdoc_waiting_terms_table::table";
    }

    @RequestMapping(value = "/update-table")
    public String updateMyObjectsTable(Model model , @RequestParam Integer tableCount, MyUserdocsFilter filter) {
        fillGenericAttributes(model, filter,false,tableCount);
        return "home/userdoc_waiting_terms/userdoc_waiting_terms_table::table";
    }

    private void fillGenericAttributes(Model model, MyUserdocsFilter filter,boolean fillFilters,Integer tableCount) {
        if (Objects.isNull(filter.getPage()))
            filter.setPage(MyUserdocsFilter.DEFAULT_PAGE);

        if (Objects.isNull(filter.getPageSize()))
            filter.setPageSize(MyUserdocsFilter.DEFAULT_PAGE_SIZE);

        if (!SecurityUtils.hasRights(SecurityRole.IpObjectsSearchForeignObjectsData))
            filter.setResponsibleUser(SecurityUtils.getLoggedUserId());

        List<UserdocSimpleResult> resultList = userdocWaitingTermsService.getUserdocWaitingTermsList(filter);

        if (fillFilters){
            model.addAttribute("userdocTypeMap",userdocWaitingTermsService.getUserdocWaitingTermsUserdocTypesMap(filter));
        }
        Integer userdocWaitingTermsCount;
        if(Objects.isNull(tableCount)){
            userdocWaitingTermsCount= userdocWaitingTermsService.getUserdocWaitingTermsCount(filter);
        }else{
            userdocWaitingTermsCount = tableCount;
        }
        model.addAttribute("userdocWaitingTermsList", resultList);
        model.addAttribute("userdocWaitingTermsCount", userdocWaitingTermsCount);
        model.addAttribute("userdocWaitingTermsFilter", filter);
    }

}
