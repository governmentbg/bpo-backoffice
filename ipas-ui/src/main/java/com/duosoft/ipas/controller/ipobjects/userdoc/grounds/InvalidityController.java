package com.duosoft.ipas.controller.ipobjects.userdoc.grounds;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/invalidity")
public class InvalidityController extends LegalGroundCategoriesBaseController {
    @Override
    public String getViewPage() {
        return "ipobjects/userdoc/grounds/invalidity_panel :: invalidity";
    }

    @Override
    public void setModelAttributes(Model model, String sessionIdentifier) {
        model.addAttribute("hasMultipleRootGrouds",true);
    }

    @Override
    public String getPanelPointer() {
        return "invalidity";
    }
}
