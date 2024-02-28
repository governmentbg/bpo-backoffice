package com.duosoft.ipas.controller.ipobjects.patentlike.spc;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.default_value.DefaultValueUtils;
import com.duosoft.ipas.controller.ipobjects.patentlike.common.PatentLikeController;
import de.danielbechler.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/spc")
public class SpcController extends PatentLikeController {

    @Autowired
    private DefaultValueUtils defaultValueUtils;
    @Autowired
    private StatusService statusService;

    @Override
    public boolean isValidFileType(String fileType) {
        return FileType.SPC.code().equals(fileType);
    }

    @Override
    public String getViewPage() {
        return "ipobjects/patentlike/spc/view";
    }

    @Override
    public void setModelAttributes(Model model, CPatent patent) {
        if (!Objects.isNull(patent.getFile().getRelationshipList()) && !Collections.isEmpty(patent.getFile().getRelationshipList())){
            CFileId fileId = patent.getFile().getRelationshipList().get(0).getFileId();
            CStatus status = statusService.getStatus(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
            model.addAttribute("spcMainPatentStatus",status);
        }
        model.addAttribute("defaultValues", defaultValueUtils.createSpcDefaultValuesObject(patent));
    }


}
