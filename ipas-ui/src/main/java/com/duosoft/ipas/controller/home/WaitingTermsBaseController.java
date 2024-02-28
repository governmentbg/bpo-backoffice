package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.WaitingTermService;
import bg.duosoft.ipas.core.service.nomenclature.FileTypeService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.util.filter.LastActionFilter;
import bg.duosoft.ipas.util.filter.ReceptionUserdocListFilter;
import bg.duosoft.ipas.util.filter.WaitingTermFilter;
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

public abstract class WaitingTermsBaseController {

    @Autowired
    private WaitingTermService waitingTermService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileTypeService fileTypeService;

    protected abstract void fillGenericAttributesInitActions(Model model, WaitingTermFilter filter);

    @GetMapping(value = "/list")
    public String getWaitingInTermList(Model model, WaitingTermFilter filter) {
        fillGenericAttributes(model, filter, null, true);
        return "home/waitingterm/waiting_term";
    }

    @RequestMapping(value = "/search")
    public String search(Model model, WaitingTermFilter filter) {
        fillGenericAttributes(model, filter, null, false);
        return "home/waitingterm/waiting_term_table::table";
    }

    @RequestMapping(value = "/update-table")
    public String updateWaitingInTermTable(Model model, @RequestParam Integer tableCount, WaitingTermFilter filter) {
        fillGenericAttributes(model, filter, tableCount, false);
        return "home/waitingterm/waiting_term_table::table";
    }

    private void fillGenericAttributes(Model model, WaitingTermFilter filter, Integer tableCount, boolean fillFilters) {
        fillGenericAttributesInitActions(model, filter);
        if (Objects.isNull(filter.getPage()))
            filter.setPage(LastActionFilter.DEFAULT_PAGE);
        if (Objects.isNull(filter.getPageSize()))
            filter.setPageSize(LastActionFilter.DEFAULT_PAGE_SIZE);
        if (!SecurityUtils.hasRights(SecurityRole.IpObjectsSearchForeignObjectsData))
            filter.setResponsibleUser(SecurityUtils.getLoggedUserId());

        List<IPObjectSimpleResult> waitingTermList = waitingTermService.getWaitingTermList(filter);

        Integer waitingTermsCount;
        if (Objects.isNull(tableCount)) {
            waitingTermsCount = waitingTermService.getWaitingTermCount(filter);
        } else {
            waitingTermsCount = tableCount;
        }

        if (Objects.nonNull(filter.getResponsibleUser())) {
            User user = userService.getUser(filter.getResponsibleUser());
            filter.setResponsibleUserName(user.getUserName());
        }

        if (fillFilters) {
            model.addAttribute("fileTypesMap", fileTypeService.getFileTypesMapBasedOnSecurityRights());
            model.addAttribute("statuses", waitingTermService.getStatuses(filter));
        }

        model.addAttribute("waitingTermList", waitingTermList);
        model.addAttribute("waitingTermCount", waitingTermsCount);
        model.addAttribute("waitingTermFilter", filter);
    }

}
