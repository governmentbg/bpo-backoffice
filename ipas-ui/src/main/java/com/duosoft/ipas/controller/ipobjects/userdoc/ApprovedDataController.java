package com.duosoft.ipas.controller.ipobjects.userdoc;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocApprovedData;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.json.JsonUtil;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import com.duosoft.ipas.controller.ipobjects.common.nice_class.NiceClassController;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import com.duosoft.ipas.webmodel.NiceListType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Raya
 * 04.09.2020
 */
@Slf4j
@Controller
@RequestMapping("/userdoc/approved")
public class ApprovedDataController {

    @Autowired
    protected MarkService markService;

    @PostMapping("/edit-panel")
    public String editApprovedPanel(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {
        CUserdoc userdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        if (!isCancel) {
            CUserdocApprovedData approvedData = JsonUtil.readJson(data, CUserdocApprovedData.class);
            updateApprovedData(approvedData, userdoc);
            updateApprovedNiceClasses(request, sessionIdentifier, userdoc, approvedData);
        }

        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, UserdocSessionObjects.SESSION_USERDOC_APPROVED_NICE_CLASSES);
        model.addAttribute("userdoc", userdoc);
        return "ipobjects/userdoc/approved/approved_panel :: approved";
    }

    private void updateApprovedData(CUserdocApprovedData approvedData, CUserdoc userdoc) {
        userdoc.setApprovedData(approvedData);
    }

    private void updateApprovedNiceClasses(HttpServletRequest request, String sessionIdentifier, CUserdoc sessionUserdoc, CUserdocApprovedData formData){
        CFileId cFileId = UserdocUtils.selectUserdocMainObject(sessionUserdoc.getUserdocParentData());
        FileType fileType = FileType.selectByCode(cFileId.getFileType());
        switch (fileType) {
            case MARK:
                setApprovedNiceClassesToSessionUserdoc(sessionUserdoc, request, sessionIdentifier, formData.getApprovedAllNice(), cFileId);
                break;
        }
    }

    private void setApprovedNiceClassesToSessionUserdoc(CUserdoc userdoc, HttpServletRequest request, String sessionIdentifier, Boolean isAllNiceClassesIncluded, CFileId markId) {
        List<CNiceClass> niceClassList = NiceClassController.selectOriginalOrSessionNiceClasses(request, sessionIdentifier, isAllNiceClassesIncluded, markService, markId, NiceListType.USERDOC_APPROVED);
        userdoc.setApprovedNiceClassList(niceClassList);
    }
}
