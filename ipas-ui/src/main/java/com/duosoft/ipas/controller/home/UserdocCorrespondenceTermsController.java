package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.UserdocCorrespondenceTermsService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.util.filter.MyUserdocsFilter;
import bg.duosoft.ipas.util.filter.sorter.MyUserdocsSorterUtils;
import bg.duosoft.ipas.util.filter.sorter.UserdocCorrespondenceTermsSortedUtils;
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
@RequestMapping("/userdoc-correspondence-terms")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).IpObjectsUserdocExpiredTermsList.code())")
public class UserdocCorrespondenceTermsController {

    @Autowired
    private UserdocCorrespondenceTermsService userdocCorrespondenceTermsService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/list")
    public String getMyObjectsList(Model model, MyUserdocsFilter filter) {
        filter.setFinished(Objects.isNull(filter.getFinished())?true:filter.getFinished());
        filter.setInTerm(Objects.isNull(filter.getInTerm())?true:filter.getInTerm());
        fillGenericAttributes(model, filter,true,null);
        return "home/userdoc_correspondence_terms/userdoc_correspondence_terms";
    }


    @RequestMapping(value = "/search")
    public String search(Model model, MyUserdocsFilter filter) {
        fillGenericAttributes(model, filter,false,null);
        return "home/userdoc_correspondence_terms/userdoc_correspondence_terms_table::table";
    }

    @RequestMapping(value = "/update-table")
    public String updateMyObjectsTable(Model model, @RequestParam Integer tableCount, MyUserdocsFilter filter) {
        fillGenericAttributes(model, filter,false,tableCount);
        return "home/userdoc_correspondence_terms/userdoc_correspondence_terms_table::table";
    }

    private void fillGenericAttributes(Model model, MyUserdocsFilter filter,boolean fillFilters,Integer tableCount) {
        if (Objects.isNull(filter.getPage()))
            filter.setPage(MyUserdocsFilter.DEFAULT_PAGE);

        if (Objects.isNull(filter.getPageSize()))
            filter.setPageSize(MyUserdocsFilter.DEFAULT_PAGE_SIZE);

        if (!SecurityUtils.hasRights(SecurityRole.IpObjectsSearchForeignObjectsData))
            filter.setResponsibleUser(SecurityUtils.getLoggedUserId());

        if (Objects.isNull(filter.getSortColumn()))
            filter.setSortColumn(UserdocCorrespondenceTermsSortedUtils.EXPIRATION_DATE);

        List<UserdocSimpleResult> resultList = userdocCorrespondenceTermsService.getUserdocCorrespondenceTermsList(filter);

        Integer userdocCorrespondenceTermsCount;
        if(Objects.isNull(tableCount)){
            userdocCorrespondenceTermsCount= userdocCorrespondenceTermsService.getUserdocCorrespondenceTermsCount(filter);
        }else{
            userdocCorrespondenceTermsCount = tableCount;
        }

        if (Objects.nonNull(filter.getResponsibleUser())){
            User user = userService.getUser(filter.getResponsibleUser());
            filter.setResponsibleUserName(user.getUserName());
        }
        model.addAttribute("userdocCorrespondenceTermsList", resultList);
        model.addAttribute("userdocCorrespondenceTermsCount", userdocCorrespondenceTermsCount);
        model.addAttribute("userdocCorrespondenceTermsFilter", filter);
        if (fillFilters){
            model.addAttribute("statuses", userdocCorrespondenceTermsService.getStatuses(filter));
            model.addAttribute("userdocTypeMap", userdocCorrespondenceTermsService.getUserdocCorrespondenceTermsUserdocTypesMap(filter));
            model.addAttribute("fileTypes",userdocCorrespondenceTermsService.getUserdocCorrespondenceTermsObjectType(filter));
        }
    }

}
