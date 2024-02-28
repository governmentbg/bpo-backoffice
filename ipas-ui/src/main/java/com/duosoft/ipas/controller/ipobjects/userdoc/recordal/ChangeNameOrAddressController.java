package com.duosoft.ipas.controller.ipobjects.userdoc.recordal;

import com.duosoft.ipas.controller.ipobjects.userdoc.ExtraDataBaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/userdoc/change_name_or_address")
public class ChangeNameOrAddressController extends ExtraDataBaseController {

    @Override
    public String getPanelPage() {
        return "ipobjects/userdoc/recordal/change_name_or_address/change_name_or_address_panel :: change-name-or-address";
    }

}
