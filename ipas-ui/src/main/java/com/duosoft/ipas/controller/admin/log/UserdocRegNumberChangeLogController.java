package com.duosoft.ipas.controller.admin.log;

import bg.duosoft.ipas.core.service.userdoc.UserdocRegNumberChangeLogService;
import bg.duosoft.ipas.persistence.model.nonentity.SimpleUserdocRegNumberChangeLog;
import bg.duosoft.ipas.util.filter.UserdocRegNumberChangeLogFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin/userdoc-reg-number-change-log")
public class UserdocRegNumberChangeLogController {

    @Autowired
    private UserdocRegNumberChangeLogService userdocRegNumberChangeLogService;

    @GetMapping(value = "/list")
    public String getUserdocRegNumberChangeLogList(Model model, UserdocRegNumberChangeLogFilter filter) {
        fillGenericAttributes(model, filter, null);
        return "admin/log/userdoc_reg_number_change_log";
    }

    @RequestMapping(value = "/search")
    public String search(Model model, UserdocRegNumberChangeLogFilter filter) {
        fillGenericAttributes(model, filter, null);
        return "admin/log/userdoc_reg_number_change_log_table::table";
    }

    @RequestMapping(value = "/update-table")
    public String updateTable(Model model, @RequestParam Integer tableCount, UserdocRegNumberChangeLogFilter filter) {
        fillGenericAttributes(model, filter, tableCount);
        return "admin/log/userdoc_reg_number_change_log_table::table";
    }

    private void fillGenericAttributes(Model model, UserdocRegNumberChangeLogFilter filter, Integer tableCount) {
        if (Objects.isNull(filter.getPage())) {
            filter.setPage(UserdocRegNumberChangeLogFilter.DEFAULT_PAGE);
        }
        if (Objects.isNull(filter.getPageSize())) {
            filter.setPageSize(UserdocRegNumberChangeLogFilter.DEFAULT_PAGE_SIZE);
        }

        List<SimpleUserdocRegNumberChangeLog> userdocRegNumberChangeLogList = userdocRegNumberChangeLogService.getUserdocRegNumberChangeLogList(filter);
        Integer userdocRegNumberChangeLogCount;

        if (Objects.isNull(tableCount)) {
            userdocRegNumberChangeLogCount = userdocRegNumberChangeLogService.getUserdocRegNumberChangeLogCount(filter);
        } else {
            userdocRegNumberChangeLogCount = tableCount;
        }
        model.addAttribute("userdocRegNumberChangeLogList", userdocRegNumberChangeLogList);
        model.addAttribute("userdocRegNumberChangeLogCount", userdocRegNumberChangeLogCount);
        model.addAttribute("userdocRegNumberChangeLogFilter", filter);
    }
}
