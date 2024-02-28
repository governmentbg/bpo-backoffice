package com.duosoft.ipas.controller.ipobjects.marklike.acp;

import bg.duosoft.ipas.core.model.acp.*;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.service.nomenclature.AcpCheckReasonService;
import bg.duosoft.ipas.core.service.nomenclature.AcpCheckResultService;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.util.json.AcpCheckData;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/acp/check-data")
public class AcpCheckDataController {

    private final String panelPage = "ipobjects/marklike/acp/acp-check-data/check_data_main_panel :: main_panel";
    private final String tablePage = "ipobjects/marklike/acp/acp-check-data/check_reasons_table :: table";

    @Autowired
    private AcpCheckReasonService acpCheckReasonService;

    @Autowired
    private AcpCheckResultService acpCheckResultService;


    @PostMapping("/add-reason")
    public String addReason(HttpServletRequest request, Model model, @RequestParam Integer id, @RequestParam String sessionIdentifier) {
        List<CAcpCheckReason> reasons = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ACP_CHECK_DATA, sessionIdentifier, request);
        CAcpCheckReasonNomenclature reasonNomenclature = acpCheckReasonService.findById(id);
        CAcpCheckReason cAcpCheckReason = new CAcpCheckReason(reasonNomenclature);

        if (!reasons.contains(cAcpCheckReason)) {
            reasons.add(cAcpCheckReason);
        }

        model.addAttribute("acpCheckReasons", reasons);
        return tablePage;
    }

    @PostMapping("/delete-reason")
    public String deleteReason(HttpServletRequest request, Model model,
                               @RequestParam String sessionIdentifier,
                               @RequestParam Integer id) {
        List<CAcpCheckReason> reasons = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ACP_CHECK_DATA, sessionIdentifier, request);
        reasons.removeIf(r -> r.getAcpCheckReason().getId().equals(id));
        model.addAttribute("acpCheckReasons", reasons);
        return tablePage;
    }

    @PostMapping("/edit")
    public String edit(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam String sessionIdentifier, @RequestParam(required = false) String data) {
        CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);

        if (!isCancel) {
            AcpCheckData jsonData = JsonUtil.readJson(data, AcpCheckData.class);
            List<CAcpCheckReason> reasons = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ACP_CHECK_DATA, sessionIdentifier, request);
            mark.setAcpCheckReasons(reasons);
            mark.setAcpCheckData(new CAcpCheckData());
            mark.getAcpCheckData().setCheckDate(jsonData.getCheckDate());
            if (Objects.nonNull(jsonData.getResultId())) {
                mark.getAcpCheckData().setAcpCheckResult(acpCheckResultService.findById(jsonData.getResultId()));
            }
        }
        model.addAttribute("acpCheckReasons", mark.getAcpCheckReasons());
        model.addAttribute("acpCheckData", mark.getAcpCheckData());
        model.addAttribute("acpCheckReasonList", acpCheckReasonService.findAllByApplicationType(mark.getFile().getFilingData().getApplicationType()));
        model.addAttribute("acpCheckResultList", acpCheckResultService.findAll());
        MarkSessionUtils.removeAcpCheckPanelSessionObjects(request, sessionIdentifier);
        return panelPage;
    }


}
