package com.duosoft.ipas.controller.ipobjects.common.reception.create;

import bg.duosoft.ipas.core.service.dailylog.DailyLogService;
import bg.duosoft.ipas.core.service.nomenclature.FileTypeGroupService;
import bg.duosoft.ipas.core.service.nomenclature.FileTypeService;
import bg.duosoft.ipas.core.service.reception.ReceptionTypeService;
import bg.duosoft.ipas.core.service.reception.SubmissionTypeService;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.util.json.CreateReceptionData;
import com.duosoft.ipas.util.reception.ReceptionUtils;
import com.duosoft.ipas.util.session.reception.ReceptionSessionUtils;
import com.duosoft.ipas.webmodel.ReceptionForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/reception/create")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ReceptionCreator.code())")
public class MarkReceptionController {

    @Autowired
    private ReceptionTypeService receptionTypeService;

    @Autowired
    private SubmissionTypeService submissionTypeService;

    @Autowired
    private DailyLogService dailyLogService;
    @Autowired
    private FileTypeGroupService fileTypeGroupService;

    @PostMapping("/change-figurative-mark")
    public String changeFigurativeMark(Model model, HttpServletRequest request, @RequestParam String sessionIdentifier, @RequestParam String data) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);
        CreateReceptionData formData = JsonUtil.readJson(data, CreateReceptionData.class);
        ReceptionForm receptionForm = ReceptionSessionUtils.setFormDataToSessionReception(request, sessionIdentifier, formData,receptionTypeService);
        ReceptionUtils.addReceptionPanelBasicModelAttributes(model, receptionForm, sessionIdentifier, submissionTypeService, receptionTypeService, dailyLogService, fileTypeGroupService);
        return "ipobjects/common/reception/reception_panel :: panel";
    }


}
