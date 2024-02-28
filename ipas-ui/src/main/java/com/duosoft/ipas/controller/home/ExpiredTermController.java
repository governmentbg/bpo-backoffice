package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.enums.ExpiredTermsPanelConfig;
import bg.duosoft.ipas.util.filter.ExpiredTermFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/expired-term")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).IpObjectsViewExpiredTermList.code())")
public class ExpiredTermController extends ExpiredTermBaseController{
    @Override
    protected void fillGenericAttributesInitActions(Model model, ExpiredTermFilter filter) {
        filter.setPanelType(ExpiredTermsPanelConfig.ALL.code());
        model.addAttribute("expiredTermsParentUrl","expired-term");
    }
}
