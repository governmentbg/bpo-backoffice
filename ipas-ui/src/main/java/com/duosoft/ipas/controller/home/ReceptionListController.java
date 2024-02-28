package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.core.model.reception.CReceptionRequest;
import bg.duosoft.ipas.core.service.reception.ReceptionRequestService;
import bg.duosoft.ipas.persistence.model.nonentity.ReceptionRequestSimpleResult;
import bg.duosoft.ipas.util.filter.ReceptionListFilter;
import bg.duosoft.ipas.util.filter.sorter.ReceptionSorterUtils;
import bg.duosoft.ipas.util.json.JsonUtil;
import liquibase.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/reception/list")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).IpObjectsViewReceptionList.code())")
public class ReceptionListController {

    @Autowired
    private ReceptionRequestService receptionRequestService;

    @GetMapping
    public String getReceptionList(Model model, @RequestParam (required = false) String data) {
        ReceptionListFilter filter = null;
        if (!StringUtils.isEmpty(data)){
            filter = JsonUtil.readJson(data, ReceptionListFilter.class);
        }
        String sortOrder = Objects.nonNull(filter) && !StringUtils.isEmpty(filter.getSortOrder())?filter.getSortOrder():ReceptionListFilter.ASC_ORDER;
        String sortColumn =  Objects.nonNull(filter) && !StringUtils.isEmpty(filter.getSortColumn())?filter.getSortColumn():ReceptionSorterUtils.RECEPTION_CREATE_DATE;
        ReceptionListFilter receptionListFilter = new ReceptionListFilter(sortOrder,sortColumn);
        fillReceptionAttributes(model, receptionListFilter);
        return "home/reception/receptions";
    }

    @PostMapping(value = "/update-table")
    public String updateReceptionTable(Model model, @RequestParam String data) {
        ReceptionListFilter receptionListFilter = JsonUtil.readJson(data, ReceptionListFilter.class);
        fillReceptionAttributes(model, receptionListFilter);
        return "home/reception/reception_table::reception-table";
    }

    private void fillReceptionAttributes(Model model, ReceptionListFilter receptionListFilter) {
        List<ReceptionRequestSimpleResult> receptions = receptionRequestService.getReceptionsWithoutStatus(receptionListFilter);
        model.addAttribute("receptions", receptions);
        model.addAttribute("receptionsCount", CollectionUtils.isEmpty(receptions) ? 0 : receptions.size());
        model.addAttribute("receptionListFilter", receptionListFilter);
    }

}
