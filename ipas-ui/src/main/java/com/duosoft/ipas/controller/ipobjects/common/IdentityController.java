package com.duosoft.ipas.controller.ipobjects.common;

import bg.duosoft.ipas.core.model.CApplicationSubType;
import bg.duosoft.ipas.core.model.efiling.CEFilingData;
import bg.duosoft.ipas.core.model.file.*;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CPatentDetails;
import bg.duosoft.ipas.core.service.nomenclature.ApplicationTypeService;
import bg.duosoft.ipas.core.service.nomenclature.LawService;
import bg.duosoft.ipas.core.service.nomenclature.RelationshipTypeService;
import bg.duosoft.ipas.core.service.reception.ReceptionRequestService;
import bg.duosoft.ipas.enums.ApplSubTyp;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.default_value.DefaultValueUtils;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.util.ApplicationTypeUtils;
import com.duosoft.ipas.util.IdentityPanelRedirectHelper;
import com.duosoft.ipas.util.IdentityPanelUtils;
import com.duosoft.ipas.util.RedirectUtils;
import com.duosoft.ipas.util.json.IdentityData;
import com.duosoft.ipas.util.session.efiling.CEFilingDataSessionUtils;
import com.duosoft.ipas.util.session.enotif.CEnotifDataSessionUtils;
import com.duosoft.ipas.util.session.extended_relationship.CExtendedRelationshipSessionUtils;
import com.duosoft.ipas.util.session.file.CFileSessionUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/identity")
public class IdentityController {

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private LawService lawService;

    @Autowired
    private RelationshipTypeService relationshipTypeService;

    @Autowired
    private ReceptionRequestService receptionRequestService;

    @Autowired
    private DefaultValueUtils defaultValueUtils;


    @PostMapping("/edit-panel-identity")
    public String editIdentityPanel(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes, @RequestParam Boolean isCancel,
                                    @RequestParam(required = false) List<String> editedPanels, @RequestParam(required = false) Boolean reloadPageParam,
                                    @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {
        CFile file = CFileSessionUtils.getSessionFile(request, sessionIdentifier);
        CRelationshipExtended sessionRelationshipExtended = CExtendedRelationshipSessionUtils.getSessionRelationshipExtended(request, sessionIdentifier);
        CEFilingData sessionEFilingData = CEFilingDataSessionUtils.getSessionEFilingData(request, sessionIdentifier);
        if (!isCancel) {
            IdentityPanelRedirectHelper identityPanelRedirectHelper = updateSessionObject(data, file, sessionEFilingData, request, sessionIdentifier);
            if (identityPanelRedirectHelper.isMarkRedirect()) {
                return RedirectUtils.redirectToMarkPage(redirectAttributes, MarkSessionUtils.getSessionMark(request, sessionIdentifier), sessionIdentifier, editedPanels);
            }
        }

        model.addAttribute("applicationTypesMap", ApplicationTypeUtils.getApplicationTypeMap(request, applicationTypeService, sessionIdentifier));
        model.addAttribute("applicationSubTypes", applicationTypeService.findAllApplicationSubTypesByApplTyp(file.getFilingData().getApplicationType()));
        model.addAttribute("lawMap", lawService.getLawMap());
        model.addAttribute("relationshipTypeMap", relationshipTypeService.selectAll());
        model.addAttribute("receptionRequest", receptionRequestService.selectReceptionByFileId(file.getFileId().getFileSeq(), file.getFileId().getFileType(), file.getFileId().getFileSeries(), file.getFileId().getFileNbr()));
        model.addAttribute("file", file);
        model.addAttribute("efilingData", sessionEFilingData);
        model.addAttribute("enotifs", CEnotifDataSessionUtils.getEnotifsData(request, sessionIdentifier));
        model.addAttribute("relationshipExtended", sessionRelationshipExtended);
        model.addAttribute("defaultValues", defaultValueUtils.createBaseDefaultValuesObject(file, sessionRelationshipExtended));
        setPatentSpecificModelAttributes(file, request, sessionIdentifier, model);
        return "ipobjects/common/identity/identity_panel :: identity-data";
    }


    @PostMapping("/update-application-subtype")
    public String updateApplicationSubtype(HttpServletRequest request, Model model, @RequestParam String applicationType, @RequestParam String sessionIdentifier) {
        CFile file = CFileSessionUtils.getSessionFile(request, sessionIdentifier);
        List<CApplicationSubType> applicationSubTypes = applicationTypeService.findAllApplicationSubTypesByApplTyp(applicationType);
        model.addAttribute("applicationSubTypes", applicationSubTypes);
        model.addAttribute("file", file);
        return "ipobjects/common/identity/identity_panel :: subtype-select";
    }

    private IdentityPanelRedirectHelper actionsIfRedirectExist(IdentityData identityData, CFile file, HttpServletRequest request, String sessionIdentifier) {
        IdentityPanelRedirectHelper identityPanelRedirectHelper = new IdentityPanelRedirectHelper();

        String fileType = file.getFileId().getFileType();
        if (fileType.equals(FileType.MARK.code()) || fileType.equals(FileType.ACP.code())) {
            identityPanelRedirectHelper.setMarkRedirect(true);
            CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
            if (!identityData.getApplicationSubtype().equals(ApplSubTyp.NATIONAL_MARK_SUB_TYPE_COLLECTIVE)
                    && !identityData.getApplicationSubtype().equals(ApplSubTyp.NATIONAL_MARK_SUB_TYPE_CERTIFIED)
                    && !identityData.getApplicationSubtype().equals(ApplSubTyp.DIVIDED_NATIONAL_MARK_SUB_TYPE_COLLECTIVE)
                    && !identityData.getApplicationSubtype().equals(ApplSubTyp.DIVIDED_NATIONAL_MARK_SUB_TYPE_CERTIFIED)) {
                mark.setUsageRules(new ArrayList<>());
            }
        }
        return identityPanelRedirectHelper;
    }

    private IdentityPanelRedirectHelper updateSessionObject(String data, CFile file, CEFilingData sessionEFilingData, HttpServletRequest request, String sessionIdentifier) {
        IdentityData identityData = JsonUtil.readJson(data, IdentityData.class);
        IdentityPanelRedirectHelper identityPanelRedirectHelper = actionsIfRedirectExist(identityData, file, request, sessionIdentifier);
        CFilingData filingData = file.getFilingData();
        filingData.setApplicationType(identityData.getApplicationType());
        filingData.setApplicationSubtype(identityData.getApplicationSubtype());
        filingData.setFilingDate(identityData.getFilingDate());
        filingData.setPublicationTyp(identityData.getPublicationTyp());
        filingData.getReceptionDocument().setIndFaxReception(identityData.getIndFaxReception());
        file.setNotes(identityData.getNotes());

        CRegistrationData registrationData = file.getRegistrationData();
        if (Objects.isNull(registrationData))
            registrationData = new CRegistrationData();

        registrationData.setRegistrationDate(identityData.getRegistrationDate());
        registrationData.setEntitlementDate(identityData.getEntitlementDate());
        registrationData.setExpirationDate(identityData.getExpirationDate());

        CRegistrationId registrationId = registrationData.getRegistrationId();
        if (Objects.isNull(registrationId))
            registrationId = new CRegistrationId();

        if (IdentityPanelUtils.showRegistrationDupFragment(file.getFileId())){
            registrationId.setRegistrationDup(identityData.getRegistrationDup());
        }

        registrationId.setRegistrationNbr(identityData.getRegistrationNbr());

        if (StringUtils.hasText(identityData.getEfilingDataLoginName())) {
            sessionEFilingData.setLogUserName(identityData.getEfilingDataLoginName());
        }
        updatePatentSpecificData(file, identityData, request, sessionIdentifier);
        return identityPanelRedirectHelper;
    }


    private void updatePatentSpecificData(CFile file, IdentityData identityData, HttpServletRequest request, String sessionIdentifier) {
        if (file.getFileId().getFileType().equals(FileType.PATENT.code()) || file.getFileId().getFileType().equals(FileType.EU_PATENT.code()) || file.getFileId().getFileType().equals(FileType.UTILITY_MODEL.code())) {
            CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
            if (Objects.isNull(patent.getPatentDetails()) && Objects.isNull(identityData.getNotInForceDate())) {
                return;
            }
            if (Objects.isNull(patent.getPatentDetails())) {
                patent.setPatentDetails(new CPatentDetails());
            }
            patent.getPatentDetails().setNotInForceDate(identityData.getNotInForceDate());
        }
    }


    private void setPatentSpecificModelAttributes(CFile file, HttpServletRequest request, String sessionIdentifier, Model model) {
        if (file.getFileId().getFileType().equals(FileType.PATENT.code()) || file.getFileId().getFileType().equals(FileType.EU_PATENT.code()) || file.getFileId().getFileType().equals(FileType.UTILITY_MODEL.code())) {
            CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
            model.addAttribute("notInForceDate", Objects.isNull(patent.getPatentDetails()) ? null : patent.getPatentDetails().getNotInForceDate());
        }
    }

}
