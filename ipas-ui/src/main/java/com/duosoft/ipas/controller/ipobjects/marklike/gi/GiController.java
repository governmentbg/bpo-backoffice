package com.duosoft.ipas.controller.ipobjects.marklike.gi;

import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.enums.FileType;
import com.duosoft.ipas.controller.ipobjects.marklike.common.MarkLikeController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/geographical_indications")
public class GiController extends MarkLikeController {

    @Override
    public void validateFileType(String fileType) {
        if (!FileType.GEOGRAPHICAL_INDICATIONS.code().equals(fileType))
            throw new RuntimeException("Invalid geographical indication file type: " + fileType);
    }

    @Override
    public String getViewPage() {
        return "ipobjects/marklike/gi/view";
    }

    @Override
    public void setModelAttributes(Model model, CMark mark) {

    }
}
