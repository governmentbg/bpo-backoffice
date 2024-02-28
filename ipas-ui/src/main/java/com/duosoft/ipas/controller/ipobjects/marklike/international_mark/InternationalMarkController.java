package com.duosoft.ipas.controller.ipobjects.marklike.international_mark;

import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.enums.FileType;
import com.duosoft.ipas.controller.ipobjects.marklike.common.MarkLikeController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/international_mark")
public class InternationalMarkController extends MarkLikeController {
    @Override
    public void validateFileType(String fileType) {
        if (!FileType.INTERNATIONAL_MARK_I.code().equals(fileType) && !FileType.INTERNATIONAL_MARK_R.code().equals(fileType) && !FileType.INTERNATIONAL_MARK_B.code().equals(fileType))
            throw new RuntimeException("Invalid international mark file type: " + fileType);
    }

    @Override
    public String getViewPage() {
        return "ipobjects/marklike/international_trademark/view";
    }

    @Override
    public void setModelAttributes(Model model, CMark mark) {

    }
}
