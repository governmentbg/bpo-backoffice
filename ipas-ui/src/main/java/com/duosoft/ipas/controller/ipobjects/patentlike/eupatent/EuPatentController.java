package com.duosoft.ipas.controller.ipobjects.patentlike.eupatent;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.service.action.ActionService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.default_value.DefaultValueUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import com.duosoft.ipas.controller.ipobjects.patentlike.common.PatentLikeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/eupatent")
public class EuPatentController extends PatentLikeController {

    @Autowired
    private DefaultValueUtils defaultValueUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private ActionService actionService;

    @Override
    public boolean isValidFileType(String fileType) {
        return FileType.EU_PATENT.code().equals(fileType);
    }

    @Override
    public String getViewPage() {
        return "ipobjects/patentlike/eupatent/view";
    }

    @Override
    public void setModelAttributes(Model model, CPatent patent) {
        model.addAttribute("defaultValues", defaultValueUtils.createPatentLikeDefaultValuesObject(patent));
        model.addAttribute("hasSecretDataRights", SecurityUtils.hasSecretDataRights(patent.getFile().getProcessSimpleData().getResponsibleUser(),
                patent.getFile().getFileId(),actionService,userService));
    }



}
