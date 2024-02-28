package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.filter.MyObjectsFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/imark-my-objects")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).HomePanelsZmAndZmr.code())")
public class IMarkMyObjectsController extends MyObjectsBaseController {
    @Override
    protected void fillGenericAttributesInitActions(Model model, MyObjectsFilter filter) {
        filter.setFileTypes(new ArrayList<>());
        filter.getFileTypes().add(FileType.INTERNATIONAL_MARK_I.code());
        filter.getFileTypes().add(FileType.INTERNATIONAL_MARK_R.code());
        filter.getFileTypes().add(FileType.INTERNATIONAL_MARK_B.code());
        model.addAttribute("myObjectsParentUrl","imark-my-objects");
        model.addAttribute("iMarkMyObjects", true);
    }
}
