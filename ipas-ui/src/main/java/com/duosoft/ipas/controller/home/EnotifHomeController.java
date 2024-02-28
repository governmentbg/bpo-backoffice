package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.core.model.mark.CEnotif;
import bg.duosoft.ipas.core.service.enotif.EnotifService;
import bg.duosoft.ipas.util.filter.InternationalMarkFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/enotif")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).IpObjectsViewInternationalMarkReceptionsList.code())")
public class EnotifHomeController {

    @Autowired
    private EnotifService enotifService;

    @GetMapping(value = "/list")
    public String getInternationalMarksList(Model model) {
        List<CEnotif> allOrOrderByGaznoDesc = enotifService.findAllOrOrderByGaznoDesc();
        model.addAttribute("enotifs", allOrOrderByGaznoDesc);
        model.addAttribute("enotifsCount", allOrOrderByGaznoDesc.size());
        return "home/international_marks/international_mark_publications";
    }

}
