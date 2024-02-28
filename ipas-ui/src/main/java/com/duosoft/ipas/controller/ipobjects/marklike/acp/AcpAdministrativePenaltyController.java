package com.duosoft.ipas.controller.ipobjects.marklike.acp;

import bg.duosoft.ipas.core.model.acp.CAcpAdministrativePenalty;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.service.nomenclature.AcpAdministrativePenaltyPaymentStatusService;
import bg.duosoft.ipas.core.service.nomenclature.AcpAdministrativePenaltyTypeService;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.util.AcpAdministrativePenaltyUtils;
import com.duosoft.ipas.util.json.AcpAdministrativeData;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.bcel.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/acp/administrative-penalty")
public class AcpAdministrativePenaltyController {

    private final String fieldsPage = "ipobjects/marklike/acp/acp-administrative-penalty/penalty_fields_panel :: fields";
    private final String panelPage = "ipobjects/marklike/acp/acp-administrative-penalty/penalty_main_panel :: main_panel";

    @Autowired
    private AcpAdministrativePenaltyTypeService acpAdministrativePenaltyTypeService;

    @Autowired
    private AcpAdministrativePenaltyPaymentStatusService acpAdministrativePenaltyPaymentStatusService;


    @PostMapping("/reload-fields")
    public String reloadFields(HttpServletRequest request, Model model, @RequestParam(required = false) String data) {
        AcpAdministrativeData acpAdministrativeData = JsonUtil.readJson(data, AcpAdministrativeData.class);
        model.addAttribute("penalty", convertJsonAcpToCObject(acpAdministrativeData));
        model.addAttribute("acpAdministrativePenaltyTypeList", acpAdministrativePenaltyTypeService.findAll());
        model.addAttribute("acpAdministrativePenaltyPaymentStatusListList", acpAdministrativePenaltyPaymentStatusService.findAll());
        return fieldsPage;
    }

    @PostMapping("/edit")
    public String edit(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam String sessionIdentifier, @RequestParam(required = false) String data) {
        CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
        if (!isCancel) {
            AcpAdministrativeData acpAdministrativeData = JsonUtil.readJson(data, AcpAdministrativeData.class);
            mark.setAcpAdministrativePenalty(convertJsonAcpToCObject(acpAdministrativeData));
        }
        model.addAttribute("penalty", mark.getAcpAdministrativePenalty());
        model.addAttribute("acpAdministrativePenaltyTypeList", acpAdministrativePenaltyTypeService.findAll());
        model.addAttribute("acpAdministrativePenaltyPaymentStatusListList", acpAdministrativePenaltyPaymentStatusService.findAll());
        return panelPage;
    }

    private CAcpAdministrativePenalty convertJsonAcpToCObject(AcpAdministrativeData jsonAcp) {
        CAcpAdministrativePenalty cAcpAdministrativePenalty = new CAcpAdministrativePenalty();
        jsonAcp.convertAmounts();

        if (Objects.nonNull(jsonAcp.getPenaltyType())) {
            cAcpAdministrativePenalty.setPenaltyType(acpAdministrativePenaltyTypeService.findById(jsonAcp.getPenaltyType()));
        }

        if (Objects.nonNull(jsonAcp.getPaymentStatus()) && AcpAdministrativePenaltyUtils.showAmountSection(cAcpAdministrativePenalty)) {
            cAcpAdministrativePenalty.setPaymentStatus(acpAdministrativePenaltyPaymentStatusService.findById(jsonAcp.getPaymentStatus()));
        }

        if (AcpAdministrativePenaltyUtils.showAmountSection(cAcpAdministrativePenalty)) {
            cAcpAdministrativePenalty.setAmount(jsonAcp.getAmountAsDecimal());
        }

        if (AcpAdministrativePenaltyUtils.showPartiallyPaidAmount(cAcpAdministrativePenalty)) {
            cAcpAdministrativePenalty.setPartiallyPaidAmount(jsonAcp.getPartiallyPaidAmountAsDecimal());
        }

        if (AcpAdministrativePenaltyUtils.showNotificationDate(cAcpAdministrativePenalty)) {
            cAcpAdministrativePenalty.setNotificationDate(jsonAcp.getNotificationDate());
        }

        if (AcpAdministrativePenaltyUtils.showOtherTypeDescription(cAcpAdministrativePenalty)) {
            cAcpAdministrativePenalty.setOtherTypeDescription(jsonAcp.getOtherTypeDescription());
        }

        return cAcpAdministrativePenalty;
    }


}
