package com.duosoft.ipas.controller.admin.log;

import bg.duosoft.ipas.core.model.action.CAutomaticActionLogResult;
import bg.duosoft.ipas.core.service.action.AutomaticActionLogService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigurationService;
import bg.duosoft.ipas.util.filter.AutomaticActionsLogFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * User: ggeorgiev
 * Date: 05.12.2022
 * Time: 15:12
 */
@Controller
@RequestMapping("/admin/automatic-actions-log")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).AdminModule.code())")
public class AutomaticActionsLogController {
    @Autowired
    private AutomaticActionLogService automaticActionLogService;
    @Autowired
    private ConfigurationService configurationService;
    @GetMapping(value = "/list")
    public String getAutomaticActionsLogList(Model model) {
        return "admin/log/automatic_actions_log";
    }
    @RequestMapping(value = "/search")
    public String search(Model model, AutomaticActionsLogFilter filter) {
        List<CAutomaticActionLogResult> res = automaticActionLogService.getAutomaticActionLogs(filter);
        model.addAttribute("automaticActionsLogList", res);
        model.addAttribute("automaticActionsFilter", filter);

        return "admin/log/automatic_actions_log_table::table";
    }
    @RequestMapping(value = "/delete24h")
    @ResponseBody
    public String delete() {
        configurationService.deleteConfiguration(ConfigurationService.AUTOMATIC_ACTION_24H);
        return "done";
    }
}
