package com.duosoft.ipas.controller.ipobjects.patentlike.spc;


import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.spc.CSpcExtended;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.util.json.SpcDetailsData;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
@RequestMapping("/spc/detail")
public class SpcDetailController {

    @PostMapping("/edit-panel-detail")
    public String editDetailPanel(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {

        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);

        if (!isCancel) {
            SpcDetailsData spcDetailsData = JsonUtil.readJson(data, SpcDetailsData.class);
            fillSpcDetailData(patent,spcDetailsData);
        }

        model.addAttribute("patent", patent);
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        return "ipobjects/patentlike/spc/spc_details_panel :: spc-details";
    }

    private void fillSpcDetailData(CPatent patent,SpcDetailsData spcDetailsData){
        patent.getTechnicalData().setMainAbstract(spcDetailsData.getMainAbstract());
        patent.getTechnicalData().setEnglishAbstract(spcDetailsData.getEnglishAbstract());
        if (Objects.isNull(patent.getSpcExtended())){
           patent.setSpcExtended(new CSpcExtended());
        }
        patent.getSpcExtended().setBgPermitNumber(spcDetailsData.getBgPermitNumber());
        patent.getSpcExtended().setBgPermitDate(spcDetailsData.getBgPermitDate());
        patent.getSpcExtended().setEuPermitNumber(spcDetailsData.getEuPermitNumber());
        patent.getSpcExtended().setEuPermitDate(spcDetailsData.getEuPermitDate());
        patent.getSpcExtended().setProductClaims(spcDetailsData.getProductClaims());
        patent.getSpcExtended().setEuNotificationDate(spcDetailsData.getEuNotificationDate());
        patent.getSpcExtended().setBgNotificationDate(spcDetailsData.getBgNotificationDate());
    }

}
