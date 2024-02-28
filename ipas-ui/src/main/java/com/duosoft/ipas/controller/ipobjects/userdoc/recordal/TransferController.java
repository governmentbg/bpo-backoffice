package com.duosoft.ipas.controller.ipobjects.userdoc.recordal;

import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import com.duosoft.ipas.controller.ipobjects.userdoc.ExtraDataBaseController;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/userdoc/transfer")
public class TransferController extends ExtraDataBaseController {

    @PostMapping("/edit-panel")
    public String editPanel(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        processExtraDataNiceClassesOrDesigns(request, isCancel, data, sessionIdentifier, sessionUserdoc);
        UserdocSessionUtils.removeNiceClassesAndSingleDesignsSessionObjects(request, sessionIdentifier);
        model.addAttribute("userdoc", sessionUserdoc);
        return getPanelPage();
    }

    @Override
    public String getPanelPage() {
        return "ipobjects/userdoc/recordal/transfer/transfer_panel :: transfer";
    }

}
