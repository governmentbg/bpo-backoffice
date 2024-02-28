package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.core.service.InternationalMarkService;
import bg.duosoft.ipas.core.service.enotif.EnotifService;
import bg.duosoft.ipas.persistence.model.nonentity.IPMarkSimpleResult;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.util.filter.InternationalMarkFilter;
import bg.duosoft.ipas.util.filter.LastActionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/international-marks")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).IpObjectsViewInternationalMarksList.code())")
public class InternationalMarkHomeController {

    @Autowired
    private EnotifService enotifService;

    @Autowired
    private InternationalMarkService internationalMarkService;

    @GetMapping(value = "/list")
    public String getInternationalMarksList(Model model, InternationalMarkFilter filter,@RequestParam(required = false) String gazno) {
        if (Objects.nonNull(gazno)){
            filter.setGazno(gazno);
            model.addAttribute("enotifPubDate",enotifService.findById(gazno).getPubDate());
        }
        fillGenericAttributes(model, filter,null);
        return "home/international_marks/international_marks";
    }

    @PostMapping(value = "/search")
    public String search(Model model, InternationalMarkFilter filter) {
        fillGenericAttributes(model, filter, null);
        return "home/international_marks/international_marks_table::table";
    }

    @RequestMapping(value = "/update-table")
    public String updateInternationalMarksTable(Model model, @RequestParam Integer tableCount, InternationalMarkFilter filter) {
        fillGenericAttributes(model, filter,tableCount);
        return "home/international_marks/international_marks_table::table";
    }

    private void fillGenericAttributes(Model model, InternationalMarkFilter filter, Integer tableCount) {
        if (Objects.isNull(filter.getPage()))
            filter.setPage(LastActionFilter.DEFAULT_PAGE);
        if (Objects.isNull(filter.getPageSize()))
            filter.setPageSize(LastActionFilter.DEFAULT_PAGE_SIZE);

        List<IPMarkSimpleResult> markList = internationalMarkService.getInternationalMarksList(filter);

        Integer count;
        if(Objects.isNull(tableCount)){
            count= internationalMarkService.getInternationalMarksCount(filter);
        }else{
            count = tableCount;
        }
        model.addAttribute("marksList", markList);
        model.addAttribute("marksCount", count);
        model.addAttribute("marksFilter", filter);
    }
}
