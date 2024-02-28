package com.duosoft.ipas.controller.ipobjects.common.history;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.logging.CFileLogChanges;
import bg.duosoft.ipas.core.service.logging.LogChangesService;
import com.duosoft.ipas.util.session.file.CFileSessionUtils;
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
@RequestMapping("/file-history")
public class FileHistoryController {

    @Autowired
    private LogChangesService logChangesService;


    @PostMapping("/load-panel")
    public String loadHistoryPanel(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier) {
        CFileId fileId = getMainObjectFileId(request, sessionIdentifier);
        List<CFileLogChanges> fileLogChanges = logChangesService.getFileLogChanges(fileId, false);

//        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        model.addAttribute("fileLogChanges", fileLogChanges);
        return "ipobjects/common/history/history_panel :: historyDetails";
    }

    @PostMapping("/view-details")
    public String viewDetailsPanel(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier, @RequestParam int changeNumber) {
        CFileId fileId = getMainObjectFileId(request, sessionIdentifier);
        CFileLogChanges fileLogChange = logChangesService.getFileLogChange(fileId, changeNumber);


        model.addAttribute("logChange", fileLogChange);
        return "ipobjects/common/history/history_info_modal :: history-info";
    }

    private CFileId getMainObjectFileId(HttpServletRequest request, String sessionIdentifier) {
        CFile sessionFile = CFileSessionUtils.getSessionFile(request, sessionIdentifier);
        return sessionFile.getFileId();
    }

}
