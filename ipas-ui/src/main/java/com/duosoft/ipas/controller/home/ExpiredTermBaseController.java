package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.ExpiredTermService;
import bg.duosoft.ipas.core.service.nomenclature.FileTypeService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.util.filter.ExpiredTermFilter;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;


public abstract class ExpiredTermBaseController {

    @Autowired
    private ExpiredTermService expiredTermService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileTypeService fileTypeService;

    protected abstract void fillGenericAttributesInitActions(Model model, ExpiredTermFilter filter);

    @GetMapping(value = "/list")
    public String getExpiredTermList(Model model, ExpiredTermFilter filter) {
        fillGenericAttributes(model, filter, true, null);
        return "home/expired_term/expired_term";
    }

    @RequestMapping(value = "/update-table")
    public String updateExpiredTermTable(Model model, @RequestParam Integer tableCount, ExpiredTermFilter filter) {
        fillGenericAttributes(model, filter, false, tableCount);
        return "home/expired_term/expired_term_table::table";
    }

    @RequestMapping(value = "/search")
    public String searchExpiredTerms(Model model, ExpiredTermFilter filter) {
        fillGenericAttributes(model, filter, false, null);
        return "home/expired_term/expired_term_table::table";
    }

    private void fillGenericAttributes(Model model, ExpiredTermFilter filter, boolean fillFilters, Integer tableCount) {
        fillGenericAttributesInitActions(model, filter);
        if (Objects.isNull(filter.getPage()))
            filter.setPage(ExpiredTermFilter.DEFAULT_PAGE);
        if (Objects.isNull(filter.getPageSize()))
            filter.setPageSize(ExpiredTermFilter.DEFAULT_PAGE_SIZE);

        if (!SecurityUtils.hasRights(SecurityRole.IpObjectsSearchForeignObjectsData))
            filter.setResponsibleUser(SecurityUtils.getLoggedUserId());

        List<IPObjectSimpleResult> expiredTermList = expiredTermService.getExpiredTermsList(filter);
        Integer expiredTermCount;

        if (fillFilters) {
            model.addAttribute("statuses", expiredTermService.getStatuses(filter));
            model.addAttribute("actionTypes", expiredTermService.getExpiredActionTypes(filter));
            model.addAttribute("fileTypesMap", fileTypeService.getFileTypesMapBasedOnSecurityRights());
        }
        if (Objects.isNull(tableCount)) {
            expiredTermCount = expiredTermService.getExpiredTersmCount(filter);
        } else {
            expiredTermCount = tableCount;
        }

        if (Objects.nonNull(filter.getResponsibleUser())) {
            User user = userService.getUser(filter.getResponsibleUser());
            filter.setResponsibleUserName(user.getUserName());
        }
        model.addAttribute("expiredTermList", expiredTermList);
        model.addAttribute("expiredTermCount", expiredTermCount);
        model.addAttribute("expiredTermFilter", filter);
    }
}
