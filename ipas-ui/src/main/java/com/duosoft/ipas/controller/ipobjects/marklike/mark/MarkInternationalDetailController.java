package com.duosoft.ipas.controller.ipobjects.marklike.mark;


import bg.duosoft.ipas.core.model.mark.*;
import bg.duosoft.ipas.core.service.InternationalMarkService;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.enums.UserdocType;
import bg.duosoft.ipas.persistence.model.nonentity.InternationalMarkSimpleResult;
import bg.duosoft.ipas.util.json.JsonUtil;
import bg.duosoft.ipas.util.mark.InternationalMarkUtils;
import com.duosoft.ipas.controller.ipobjects.common.nice_class.NiceClassController;
import com.duosoft.ipas.util.json.MarkInternationalDetailsData;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import com.duosoft.ipas.webmodel.NiceListType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/mark")
public class MarkInternationalDetailController {

    @Autowired
    private ProcessService processService;
    @Autowired
    private MarkService markService;
    @Autowired
    private InternationalMarkService internationalMarkService;

    @PostMapping("/edit-panel-international-detail")
    public String editInternationalDetailPanel(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {
        CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
        if (!isCancel) {
            MarkInternationalDetailsData markDetailsData = JsonUtil.readJson(data, MarkInternationalDetailsData.class);

            CMadridApplicationData cMadridApplicationData = new CMadridApplicationData();
            cMadridApplicationData.setInternationalFileNumber(markDetailsData.getInternationalFileNumber());
            cMadridApplicationData.setIntFilingDate(markDetailsData.getIntFilingDate());
            mark.setMadridApplicationData(cMadridApplicationData);

            Pair<Optional<Integer>, Optional<String>> registrationNumberAndDup = InternationalMarkUtils.separateRegistrationNumberAndDup(markDetailsData.getRegistrationNumber());
            Integer registrationNbr = Objects.nonNull(registrationNumberAndDup) ? registrationNumberAndDup.getFirst().orElse(null) : null;
            String registrationDup = Objects.nonNull(registrationNumberAndDup) ? registrationNumberAndDup.getSecond().orElse(null) : null;

            if (Objects.nonNull(registrationNbr)) {
                if (mark.getMarkInternationalReplacement() == null) {
                    mark.setMarkInternationalReplacement(new CMarkInternationalReplacement());
                }

                CMarkInternationalReplacement internationalReplacementData = mark.getMarkInternationalReplacement();
                List<CNiceClass> niceClassList = NiceClassController.selectOriginalOrSessionNiceClasses(request, sessionIdentifier, markDetailsData.getIsAllServices(), markService, mark.getFile().getFileId(), NiceListType.OBJECT_INTL_REPLACEMENT_LIST);
                internationalReplacementData.setReplacementNiceClasses(niceClassList);
                internationalReplacementData.setIsAllServices(markDetailsData.getIsAllServices());
                internationalReplacementData.setRegistrationNumber(registrationNbr);
                internationalReplacementData.setRegistrationDup(registrationDup);
                internationalReplacementData.setReplacementFilingNumber(markDetailsData.getReplacementFilingNumber());
            } else {
                mark.setMarkInternationalReplacement(null);
            }
        }

        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, MarkSessionObjects.SESSION_MARK_INTL_REPLACEMENT_NICE_CLASSES);
        model.addAttribute("mark", mark);
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        model.addAttribute("internationalRegistrations", processService.selectSubUserdocPartialDataByUserdocTyp(mark.getFile().getProcessId(), UserdocType.MARK_INTERNATIONAL_REGISTRATION_REQUEST));
        CMarkInternationalReplacement internationalReplacement = mark.getMarkInternationalReplacement();
        model.addAttribute("internationalReplacementMarkDetails", Objects.nonNull(internationalReplacement) && Objects.nonNull(internationalReplacement.getReplacementFilingNumber()) ? internationalMarkService.selectInternationalRegistration(internationalReplacement.getReplacementFilingNumber()) : null);
        return "ipobjects/marklike/mark/details/international/mark_international_details_panel :: mark-international-details";
    }

    @GetMapping(value = "/autocomplete-replacement-regNumber", produces = "application/json")
    @ResponseBody
    public List<InternationalMarkSimpleResult> autocompleteRelationshipObject(@RequestParam String registrationNumber) {
        List<InternationalMarkSimpleResult> results = internationalMarkService.selectInternationalRegistrations(registrationNumber);
        return CollectionUtils.isEmpty(results) ? new ArrayList<>() : results;
    }
}
