package com.duosoft.ipas.controller.ipobjects.common.payments;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.util.security.SecurityUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/userdoc-payments")
public class UserdocPaymentsController extends PaymentsController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProcessService processService;


    protected void addEditEnabledToModel(HttpServletRequest request, Model model, String sessionIdentifier) {
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        model.addAttribute("editEnabled", SecurityUtils.hasUserdocPaymentsRights(sessionUserdoc.getDocumentId(), processService));
    }


    protected String getMainObjectFilingNumber(HttpServletRequest request, String sessionIdentifier) {
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        CFileId fileId = processService.selectTopProcessFileId(sessionUserdoc.getProcessSimpleData().getProcessId());
        return fileId.createFilingNumber();
    }

}
