package com.duosoft.ipas.controller.ipobjects.marklike.acp;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.util.security.SecurityUtils;
import com.duosoft.ipas.config.exception.ForbiddenException;
import com.duosoft.ipas.controller.ipobjects.marklike.common.MarkLikeController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/acp")
public class AcpController extends MarkLikeController {

    @Autowired
    private ProcessService processService;

    @Override
    public void validateFileType(String fileType) {
        if (!FileType.ACP.code().equals(fileType))
            throw new RuntimeException("Invalid acp file type: " + fileType);
    }

    @Override
    public String getViewPage() {
        return "ipobjects/marklike/acp/view";
    }

    @Override
    public void setModelAttributes(Model model, CMark mark) {
    }

    @Override
    public void checkPermissions(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr) {
        if (SecurityUtils.hasRights(SecurityRole.AcpViewAll))
            return;

        CFileId fileId = new CFileId(fileSeq, fileTyp, fileSer, fileNbr);
        if (SecurityUtils.hasRights(SecurityRole.AcpViewOwn) && SecurityUtils.isLoggedUserResponsibleUser(fileId, processService))
            return;

        throw new ForbiddenException();
    }
}
