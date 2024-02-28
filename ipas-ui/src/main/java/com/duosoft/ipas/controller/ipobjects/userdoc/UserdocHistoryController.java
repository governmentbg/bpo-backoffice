package com.duosoft.ipas.controller.ipobjects.userdoc;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.logging.CFileLogChanges;
import bg.duosoft.ipas.core.model.logging.CUserdocLogChanges;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.service.logging.LogChangesService;
import com.duosoft.ipas.util.session.file.CFileSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/userdoc-history")
public class UserdocHistoryController {

    @Autowired
    private LogChangesService logChangesService;


    @PostMapping("/load-panel")
    public String loadHistoryPanel(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier) {
        CDocumentId documentId = getUsedocDocumentId(request, sessionIdentifier);
        List<CUserdocLogChanges> logChanges = logChangesService.getUserdocLogChanges(documentId, false);

        model.addAttribute("userdocLogChanges", logChanges);
        return "ipobjects/common/history/history_panel :: userdocHistoryDetails";
    }

    @PostMapping("/view-details")
    public String viewDetailsPanel(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier, @RequestParam int changeNumber) {
        CDocumentId documentId = getUsedocDocumentId(request, sessionIdentifier);
        CUserdocLogChanges logChange = logChangesService.getUserdocLogChange(documentId, changeNumber);


        model.addAttribute("logChange", logChange);
        return "ipobjects/common/history/history_info_modal :: history-info";
    }

    private CDocumentId getUsedocDocumentId(HttpServletRequest request, String sessionIdentifier) {
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        return sessionUserdoc.getDocumentId();
    }

}
