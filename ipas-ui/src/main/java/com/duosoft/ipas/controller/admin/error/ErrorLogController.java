package com.duosoft.ipas.controller.admin.error;

import bg.duosoft.ipas.core.model.error.CErrorLog;
import bg.duosoft.ipas.core.service.ext.ErrorLogService;
import bg.duosoft.ipas.util.filter.ErrorLogFilter;
import bg.duosoft.ipas.util.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/error-log")
public class ErrorLogController {

    @Autowired
    private ErrorLogService errorLogService;

    @GetMapping("/list")
    public String selectList(Model model, ErrorLogFilter filter) {
        fillGenericAttributes(model, filter);
        return "admin/error/log/error_log";
    }

    @PostMapping("/update-table")
    public String updateTable(Model model, ErrorLogFilter filter) {
        fillGenericAttributes(model, filter);
        return "admin/error/log/error_log_table :: table";
    }

    @GetMapping("/view")
    public String view(Model model, @RequestParam Integer id, ErrorLogFilter filter) {
        CErrorLog cErrorLog = errorLogService.selectById(id);
        model.addAttribute("errorLog", cErrorLog);
        model.addAttribute("errorLogFilter", filter);
        return "admin/error/log/error_log_view";
    }

    @PostMapping("/open-modal")
    public String openResolveErrorModal(Model model, @RequestParam Integer id) {
        CErrorLog cErrorLog = errorLogService.selectById(id);
        model.addAttribute("errorLog", cErrorLog);
        return "admin/error/log/error_log_modal :: modal";
    }

    @PostMapping("/mark-as-resolved")
    public String markErrorAsResolved(Model model, @RequestParam Integer id, @RequestParam(required = false) String comment) {
        CErrorLog cErrorLog = errorLogService.selectById(id);
        cErrorLog.setDateResolved(new Date());
        cErrorLog.setUsernameResolved(SecurityUtils.getLoggedUsername());
        cErrorLog.setCommentResolved(comment);
        errorLogService.save(cErrorLog);
        return "redirect:/admin/error-log/view?id=" + id;
    }


    private void fillGenericAttributes(Model model, ErrorLogFilter filter) {
        List<CErrorLog> errorLogList = errorLogService.selectErrorLogs(filter);
        Integer errorLogCount = errorLogService.selectErrorLogsCount(filter);
        model.addAttribute("errorLogList", errorLogList);
        model.addAttribute("errorLogCount", errorLogCount);
        model.addAttribute("errorLogFilter", filter);
    }

    @ResponseBody
    @GetMapping("/count-unresolved")
    public Integer selectUnresolvedCount() {
        return errorLogService.countAllUnresolved();
    }
}
