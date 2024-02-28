package com.duosoft.ipas.controller.ipobjects.marklike.mark;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CFilingData;
import bg.duosoft.ipas.core.model.mark.*;
import bg.duosoft.ipas.core.service.mark.MarkUsageRuleService;
import bg.duosoft.ipas.core.service.nomenclature.SignTypeService;
import bg.duosoft.ipas.core.service.nomenclature.UsageRuleService;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.AttachmentType;
import bg.duosoft.ipas.enums.MarkSignType;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.attachment.AttachmentUtils;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.json.JsonUtil;
import bg.duosoft.ipas.util.mark.InternationalMarkUtils;
import bg.duosoft.ipas.util.mark.MarkSignDataAttachmentUtils;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.MarkUsageRulesUtils;
import com.duosoft.ipas.util.json.MarkDetailsData;
import bg.duosoft.ipas.util.mark.MarkSignTypeUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mark")
public class MarkDetailController {

    @Autowired
    private SignTypeService signTypeService;

    @Autowired
    private UsageRuleService usageRuleService;

    @Autowired
    private MarkUsageRuleService markUsageRuleService;


    private final String markUsageRulesFragment = "ipobjects/marklike/mark/details/mark_usage_rules :: mark-usage-rules";


    @RequestMapping(value = "/usage-rule-content")
    public void downloadMarkUsageFile(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam String sessionIdentifier,
                                      @RequestParam Integer id,
                                      @RequestParam Integer usageRuleTypeId) {

        List<CMarkUsageRule> sessionUsageRules = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_USAGE_RULES, sessionIdentifier, request);
        CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
        CFileId fileId = mark.getFile().getFileId();
        if (CollectionUtils.isEmpty(sessionUsageRules)) {
            sessionUsageRules = mark.getUsageRules();
        }

        CMarkUsageRule cMarkUsageRule = sessionUsageRules.stream()
                .filter(r -> r.getId().equals(id) && r.getUsageRule().getId().equals(usageRuleTypeId))
                .findFirst()
                .orElse(null);
        if (!cMarkUsageRule.isContentLoaded()) {
            cMarkUsageRule = markUsageRuleService.findMarkUsageRule(id, usageRuleTypeId, fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr(), true);
        }
        AttachmentUtils.writeData(response, cMarkUsageRule.getContent(), MediaType.APPLICATION_PDF.toString(), cMarkUsageRule.getName());
    }


    @PostMapping("/delete-usage-rule")
    public String deleteUsageRule(HttpServletRequest request, Model model,
                                  @RequestParam String sessionIdentifier,
                                  @RequestParam Integer id,
                                  @RequestParam Integer usageRuleTypeId) {
        List<CMarkUsageRule> sessionUsageRules = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_USAGE_RULES, sessionIdentifier, request);
        sessionUsageRules.removeIf(r -> r.getId().equals(id) && r.getUsageRule().getId().equals(usageRuleTypeId));
        model.addAttribute("panelPrefix", CoreUtils.getUrlPrefixByFileType(MarkSessionUtils.getSessionMark(request, sessionIdentifier).getFile().getFileId().getFileType()));
        model.addAttribute("markUsageRules", sessionUsageRules);
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        return markUsageRulesFragment;
    }


    @PostMapping("/upload-usage-rule")
    public String uploadUsageRule(HttpServletRequest request, Model model,
                                  @RequestParam String sessionIdentifier) {

        List<CMarkUsageRule> sessionUsageRules = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_USAGE_RULES, sessionIdentifier, request);
        CMarkUsageRule sessionNewUsageRule = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_NEW_USAGE_RULE, sessionIdentifier, request);
        sessionUsageRules.add(sessionNewUsageRule);
        model.addAttribute("panelPrefix", CoreUtils.getUrlPrefixByFileType(MarkSessionUtils.getSessionMark(request, sessionIdentifier).getFile().getFileId().getFileType()));
        model.addAttribute("markUsageRules", sessionUsageRules);
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, MarkSessionObjects.SESSION_MARK_NEW_USAGE_RULE);
        return markUsageRulesFragment;
    }


    @RequestMapping(value = "/validate-usage-rule")
    public String validateUsageRule(Model model, HttpServletRequest request, @RequestParam(value = "uploadUsageRule") MultipartFile uploadFile,
                                    @RequestParam String sessionIdentifier,
                                    @RequestParam Integer usageRuleType) throws IOException {
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, MarkSessionObjects.SESSION_MARK_NEW_USAGE_RULE);
        List<CMarkUsageRule> sessionUsageRules = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_USAGE_RULES, sessionIdentifier, request);
        CMarkUsageRule newUsageRule = new CMarkUsageRule();

        //it is not validated
        List<ValidationError> errors = new ArrayList<>();

        List<CMarkUsageRule> usageRulesByType = sessionUsageRules.stream().filter(r -> r.getUsageRule().getId().equals(usageRuleType)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(usageRulesByType)) {
            CMarkUsageRule cUsageRuleRuleWithMaxId = usageRulesByType.stream()
                    .max(Comparator.comparing(r -> r.getId())).orElse(null);
            newUsageRule.setId(cUsageRuleRuleWithMaxId.getId() + DefaultValue.INCREMENT_VALUE);
        } else {
            newUsageRule.setId(DefaultValue.INCREMENT_VALUE);
        }
        newUsageRule.setName(uploadFile.getOriginalFilename());
        newUsageRule.setContent(uploadFile.getBytes());
        newUsageRule.setUsageRule(usageRuleService.findById(usageRuleType));
        newUsageRule.setDateCreated(DateUtils.convertToDate(LocalDate.now()));
        newUsageRule.setContentLoaded(true);

        if (CollectionUtils.isEmpty(errors)) {
            HttpSessionUtils.setSessionAttribute(MarkSessionObjects.SESSION_MARK_NEW_USAGE_RULE, sessionIdentifier, newUsageRule, request);
        }

//        model.addAttribute("errors", errors);
//        model.addAttribute("id", "attachmentData");
        return "base/validation :: validation-message";

    }


    @PostMapping("/edit-panel-detail")
    public String editDetailPanel(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {
        CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
        if (!isCancel) {
            MarkDetailsData markDetailsData = JsonUtil.readJson(data, MarkDetailsData.class);
            CSignData signData = getSignData(mark);
            signData.setSignType(getMarkDetailsSignType(markDetailsData));
            fillMarkName(signData, markDetailsData.getMarkName());
            MarkSignType signType = signData.getSignType();
            if (MarkSignTypeUtils.isMarkContainAttachments(signType)) {
                fillMarkAttachments(request, mark, sessionIdentifier);
                removeWrongAttachments(mark, signType);
                List<CMarkAttachment> attachments = mark.getSignData().getAttachments();
                if (!CollectionUtils.isEmpty(attachments)) {
                    CMarkAttachment markAttachment = MarkSignDataAttachmentUtils.selectFirstImageAttachment(signData);
                    if (Objects.nonNull(markAttachment)) {
                        markAttachment.setColourDescription(markDetailsData.getColourDescription());
                    }
                }
            } else {
                mark.getSignData().setAttachments(new ArrayList<>());
            }

            getLimitationData(mark).setDisclaimer(markDetailsData.getDisclaimer());
            fillTransliterationAndTranslationData(mark.getFile(), signData, markDetailsData);
            mark.setDescription(markDetailsData.getDescription());
            mark.setUsageRules(HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_USAGE_RULES, sessionIdentifier, request));
        }

        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        model.addAttribute("signTypesMap", signTypeService.getSignTypesMap());
        model.addAttribute("usageRules", usageRuleService.findAll());
        model.addAttribute("mark", mark);
        MarkSessionUtils.removeMarkDetailPanelSessionObjects(request, sessionIdentifier);
        return "ipobjects/marklike/mark/details/mark_details_panel :: mark-details";
    }

    private void removeWrongAttachments(CMark mark, MarkSignType signType) {
        if (!MarkSignTypeUtils.isMarkContainImages(signType)) {
            mark.getSignData().getAttachments().removeIf(markAttachment -> markAttachment.getAttachmentType() == AttachmentType.IMAGE);
        }
        if (!MarkSignTypeUtils.isMarkContainAudio(signType)) {
            mark.getSignData().getAttachments().removeIf(markAttachment -> markAttachment.getAttachmentType() == AttachmentType.AUDIO);
        }
        if (!MarkSignTypeUtils.isMarkContainVideo(signType)) {
            mark.getSignData().getAttachments().removeIf(markAttachment -> markAttachment.getAttachmentType() == AttachmentType.VIDEO);
        }
    }

    @PostMapping("/update-mark-details")
    public String updateMarkDetails(HttpServletRequest request, Model model, @RequestParam String data, @RequestParam String sessionIdentifier) {
        MarkDetailsData markDetailsData = JsonUtil.readJson(data, MarkDetailsData.class);
        CMark sessionMark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
        CMark tempMark = createTemporaryMark(markDetailsData, sessionMark, request, sessionIdentifier);
        fillMarkName(tempMark.getSignData(), sessionMark.getSignData().getMarkName());
        fillTransliterationAndTranslationData(tempMark.getFile(), tempMark.getSignData(), markDetailsData);
        fillMarkAttachments(request, tempMark, sessionIdentifier);
        model.addAttribute("mark", tempMark);
        model.addAttribute("signTypesMap", signTypeService.getSignTypesMap());
        model.addAttribute("usageRules", usageRuleService.findAll());
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        return "ipobjects/marklike/mark/details/mark_details_panel :: mark-details";
    }

    private void fillMarkName(CSignData signData, String markName) {
        MarkSignType signType = signData.getSignType();
        if (MarkSignTypeUtils.isMarkContainName(signType))
            signData.setMarkName(markName);
        else
            signData.setMarkName(null);
    }

    private void fillTransliterationAndTranslationData(CFile file, CSignData signData, MarkDetailsData markDetailsData) {
        if (InternationalMarkUtils.isInternationalMark(file.getFileId())) {
            signData.setMarkTranslation(markDetailsData.getMarkTranslation());
            signData.setMarkTransliteration(markDetailsData.getMarkTransliteration());
        }
    }

    private void fillMarkAttachments(HttpServletRequest request, CMark mark, String sessionIdentifier) {
        List<CMarkAttachment> sessionMarkAttachments = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ATTACHMENTS, sessionIdentifier, request);
        CSignData signData = mark.getSignData();
        signData.setAttachments(sessionMarkAttachments);
    }

    private CSignData getSignData(CMark mark) {
        CSignData signData = mark.getSignData();
        if (Objects.isNull(signData)) {
            signData = new CSignData();
            mark.setSignData(signData);
        }
        return signData;
    }

    private CLimitationData getLimitationData(CMark mark) {
        CLimitationData limitationData = mark.getLimitationData();
        if (Objects.isNull(limitationData)) {
            limitationData = new CLimitationData();
            mark.setLimitationData(limitationData);
        }
        return limitationData;
    }

    private MarkSignType getMarkDetailsSignType(MarkDetailsData markDetailsData) {
        String signType = markDetailsData.getSignType();
        if (StringUtils.isEmpty(signType))
            throw new RuntimeException("Sign type cannot be empty !");

        return MarkSignType.selectByCode(signType);
    }

    private CMark createTemporaryMark(MarkDetailsData markDetailsData, CMark sessionMark, HttpServletRequest request, String sessionIdentifier) {
        CMark tempMark = new CMark();
        tempMark.setFile(new CFile());
        tempMark.getFile().setFilingData(new CFilingData());
        tempMark.getFile().getFilingData().setApplicationSubtype(sessionMark.getFile().getFilingData().getApplicationSubtype());
        tempMark.getFile().setFileId(sessionMark.getFile().getFileId());
        tempMark.setRenewalData(new CRenewalData());
        tempMark.setSignData(new CSignData());
        tempMark.getSignData().setSignType(MarkSignType.selectByCode(markDetailsData.getSignType()));
        tempMark.setLimitationData(new CLimitationData());
        tempMark.getLimitationData().setDisclaimer(markDetailsData.getDisclaimer());
        tempMark.setDescription(markDetailsData.getDescription());
        tempMark.setUsageRules(HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_USAGE_RULES, sessionIdentifier, request));
        return tempMark;
    }

}
