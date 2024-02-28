package com.duosoft.ipas.controller.ipobjects.userdoc.recordal;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.default_value.DefaultValueUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import com.duosoft.ipas.controller.ipobjects.userdoc.ExtraDataBaseController;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/userdoc/renewal")
public class RenewalController extends ExtraDataBaseController {

    @Autowired
    private DefaultValueUtils defaultValueUtils;

    @Autowired
    private PatentService patentService;

    @Override
    public String getPanelPage() {
        return "ipobjects/userdoc/recordal/renewal/renewal_panel :: renewal";
    }

    @PostMapping("/edit-panel")
    public String editPanel(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        processExtraDataNiceClassesOrDesigns(request, isCancel, data, sessionIdentifier, sessionUserdoc);
        UserdocSessionUtils.removeNiceClassesAndSingleDesignsSessionObjects(request, sessionIdentifier);
        model.addAttribute("userdoc", sessionUserdoc);
        return getPanelPage();
    }

    @PostMapping("/calculate-new-expiration-date")
    @ResponseBody
    public String calculateNewExpirationDate(HttpServletRequest request, @RequestParam String sessionIdentifier) {
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        CFile file = UserdocUtils.selectUserdocMainObjectFile(sessionUserdoc, markService, patentService);
        if (Objects.isNull(file)) {
            return null;
        }

        Date renewalExpirationDate = defaultValueUtils.getRenewalExpirationDate(file);
        if (Objects.isNull(renewalExpirationDate)) {
            return null;
        }
        return DateUtils.formatDate(renewalExpirationDate);
    }


}
