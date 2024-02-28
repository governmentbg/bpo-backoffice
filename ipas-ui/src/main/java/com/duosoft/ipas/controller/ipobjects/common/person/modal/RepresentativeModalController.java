package com.duosoft.ipas.controller.ipobjects.common.person.modal;

import bg.duosoft.ipas.enums.RepresentativeType;
import com.duosoft.ipas.webmodel.structure.AttorneyDataWebModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/person/representative-modal")
public class RepresentativeModalController {

    @PostMapping("/open")
    public String open(Model model, @RequestParam Integer personKind, @RequestParam boolean onlyActive) {
        model.addAttribute("personKind", personKind);
        model.addAttribute("onlyActive", onlyActive);
        model.addAttribute("attorneyInitialData", new AttorneyDataWebModel());
        return "ipobjects/common/person/modal/agent_modal :: agent-form-modal";
    }

    @PostMapping("/update-conent")
    public String updateContent(Model model, @RequestParam Integer personKind, @RequestParam boolean onlyActive, @RequestParam String representativeType) {
        model.addAttribute("personKind", personKind);
        model.addAttribute("representativeType", representativeType);
        RepresentativeType representativeTypeEnum = Enum.valueOf(RepresentativeType.class, representativeType);
        switch (representativeTypeEnum) {
            case NATURAL_PERSON:
                model.addAttribute("onlyActive", onlyActive);
                return "ipobjects/common/person/modal/agent_modal :: natural-person-form";
            case PARTNERSHIP:
                model.addAttribute("onlyActive", onlyActive);
                return "ipobjects/common/person/modal/agent_modal :: partnership-form";
            case LAWYER:
                return "ipobjects/common/person/modal/agent_modal :: lawyer-form";
            case LAWYER_COMPANY:
                return "ipobjects/common/person/modal/agent_modal :: lawyer-company-form";
            case LAWYER_PARTNERSHIP:
                return "ipobjects/common/person/modal/agent_modal :: lawyer-partnership-form";
            case TEMP_SERVICE_PERSON:
                return "ipobjects/common/person/modal/agent_modal :: temp-service-person-form";
            default:
                throw new RuntimeException("Invalid representativeType! Value: " + representativeType);
        }
    }

}
