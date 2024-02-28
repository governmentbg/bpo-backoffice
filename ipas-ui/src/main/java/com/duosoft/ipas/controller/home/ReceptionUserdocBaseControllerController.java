package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.core.service.reception.ReceptionUserdocRequestService;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.util.filter.ReceptionUserdocListFilter;
import bg.duosoft.ipas.util.filter.sorter.ReceptionUserdocSorterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

public abstract class ReceptionUserdocBaseControllerController {


    protected abstract void fillGenericAttributesInitActions(Model model, ReceptionUserdocListFilter filter);

    @Autowired
    private ReceptionUserdocRequestService receptionUserdocRequestService;

    @GetMapping(value = "/list")
    public String selectList(Model model, ReceptionUserdocListFilter filter) {
        fillAttributes(model, filter, true, null);
        return "home/reception/userdoc/receptions";
    }

    @PostMapping("/update-table")
    public String updateTable(Model model, @RequestParam Integer tableCount, ReceptionUserdocListFilter filter) {
        fillAttributes(model, filter, false, tableCount);
        return "home/reception/userdoc/reception_table :: reception-table";
    }

    @RequestMapping(value = "/search")
    public String search(Model model, ReceptionUserdocListFilter filter) {
        fillAttributes(model, filter, false, null);
        return "home/reception/userdoc/reception_table :: reception-table";
    }


    private void fillAttributes(Model model, ReceptionUserdocListFilter filter, boolean fillFilters, Integer tableCount) {
        fillGenericAttributesInitActions(model, filter);
        if (Objects.isNull(filter.getPage()))
            filter.setPage(ReceptionUserdocListFilter.DEFAULT_PAGE);
        if (Objects.isNull(filter.getPageSize()))
            filter.setPageSize(ReceptionUserdocListFilter.DEFAULT_PAGE_SIZE);
        if (Objects.isNull(filter.getSortOrder()))
            filter.setSortOrder(ReceptionUserdocListFilter.DESC_ORDER);
        if (Objects.isNull(filter.getSortColumn()))
            filter.setSortColumn(ReceptionUserdocSorterUtils.RECEPTION_USERDOC_CREATE_DATE);

        List<UserdocSimpleResult> userdocReceptions = receptionUserdocRequestService.selectUserdocReceptions(filter);

        Integer userdocReceptionListCount;
        if (Objects.isNull(tableCount)) {
            userdocReceptionListCount = receptionUserdocRequestService.selectUserdocReceptionsCount(filter);
        } else {
            userdocReceptionListCount = tableCount;
        }

        model.addAttribute("userdocReceptions", userdocReceptions);
        model.addAttribute("total", userdocReceptionListCount);
        model.addAttribute("receptionUserdocListFilter", filter);
        if (fillFilters) {
            model.addAttribute("fileTypes", receptionUserdocRequestService.getUserdocObjectTypes(filter));
            model.addAttribute("userdocTypeMap", receptionUserdocRequestService.getUserdocTypes(filter));
        }
    }

}
