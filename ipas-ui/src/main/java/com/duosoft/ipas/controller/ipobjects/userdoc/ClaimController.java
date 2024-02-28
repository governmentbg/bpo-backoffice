package com.duosoft.ipas.controller.ipobjects.userdoc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/userdoc/claim")
public class ClaimController extends ExtraDataBaseController {

    @Override
    public String getPanelPage() {
        return "ipobjects/userdoc/claim/claim_panel :: claim";
    }

}
