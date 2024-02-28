package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.enums.WaitingTermsPanelConfig;
import bg.duosoft.ipas.util.filter.WaitingTermFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/waiting-term-zmr")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).IpObjectsViewWaitingTermZmrList.code())")
public class WaitingTermsZmrController extends WaitingTermsBaseController{
    @Override
    protected void fillGenericAttributesInitActions(Model model, WaitingTermFilter filter) {
        filter.setPanelType(WaitingTermsPanelConfig.ZMR.code());
        model.addAttribute("waitingTermsParentUrl","waiting-term-zmr");
    }
}
