package com.duosoft.ipas.controller.ipobjects.userdoc;

import bg.duosoft.abdocs.exception.AbdocsMissingDocumentException;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.document.CDocumentSeqId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.process.CProcessSimpleData;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.service.efiling.IpObjectEfilingDataService;
import bg.duosoft.ipas.core.service.efiling.UserdocEfilingDataService;
import bg.duosoft.ipas.core.service.impl.logging.diff.DiffGenerator;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.reception.ReceptionUserdocRequestService;
import bg.duosoft.ipas.core.service.userdoc.UserdocRootGroundService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.integration.efuserchange.service.EfUserChangeService;
import bg.duosoft.ipas.util.security.SecurityUtils;
import bg.duosoft.ipas.util.userdoc.UserdocGroundsUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.config.exception.ForbiddenException;
import com.duosoft.ipas.enums.UserdocPanel;
import com.duosoft.ipas.util.RedirectUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocInitPanelSessionObjectsUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/userdoc")
public class UserdocController {

    @Autowired
    private UserdocService userdocService;

    @Autowired
    private UserdocRootGroundService userdocRootGroundService;

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private ReceptionUserdocRequestService receptionUserdocRequestService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserdocEfilingDataService userdocEfilingDataService;

    @Autowired
    private EfUserChangeService efUserChangeService;

    @RequestMapping(value = {"/detail/{docOri}/{docLog}/{docSer}/{docNbr}"}, method = RequestMethod.GET)
    public String getUserdocDetails(Model model, HttpServletRequest request,
                                    @PathVariable String docOri,
                                    @PathVariable String docLog,
                                    @PathVariable Integer docSer,
                                    @PathVariable Integer docNbr) {
        checkPermissions(docOri, docLog, docSer, docNbr);
        CUserdoc userdoc = selectUserdocFromModel(model);
        if (Objects.isNull(userdoc)) {
            userdoc = selectUserdocFromDatabase(docOri, docLog, docSer, docNbr);
            String sessionIdentifier = HttpSessionUtils.findExistingMainSessionObjectIdentifier(request, new CDocumentId(docOri, docLog, docSer, docNbr).createFilingNumber());
            if (StringUtils.isEmpty(sessionIdentifier)) {
                Integer maxSessionObjects = yamlConfig.getMaxSessionObjects();
                if (SessionObjectUtils.hasFreeSpaceForSessionObject(request, maxSessionObjects))
                    setSessionUserdoc(model, request, userdoc);
                else
                    return removeSessionObjectsPage(model, request, maxSessionObjects);
            } else {
                String sessionIdentifierWithoutPrefix = HttpSessionUtils.getFilingNumberAndUniqueIdentifierFromSessionObject(sessionIdentifier);
                if (isUserdocEdited(request, userdoc, sessionIdentifierWithoutPrefix))
                    return openExistingObjectPage(model, sessionIdentifier);
                else {
                    UserdocSessionUtils.removeAllUserdocSessionObjects(request, sessionIdentifierWithoutPrefix);
                    setSessionUserdoc(model, request, userdoc);
                }
            }
        }
        setBaseModelAttributes(model, userdoc);
        CProcessSimpleData processSimpleData = userdoc.getProcessSimpleData();
        processService.setResponsibleUserChangeAsRead(processSimpleData.getProcessId().getProcessType(), processSimpleData.getProcessId().getProcessNbr(),
                processSimpleData.getResponsibleUser() != null ? processSimpleData.getResponsibleUser().getUserId() : null);
        return "ipobjects/userdoc/view";
    }


    private String getApplicationNumberOnNotifyUserChange(CUserdoc userdoc) {
        String externalSystemId = userdoc.getDocument().getExternalSystemId();
        if (Objects.isNull(externalSystemId)){
            CDocumentId documentId = userdoc.getDocument().getDocumentId();
            CDocumentSeqId documentSeqId = userdoc.getDocument().getDocumentSeqId();
            return documentId.getDocOrigin()+"/"+ documentId.getDocLog()+"/"+documentSeqId.getDocSeqSeries()+"/"+documentSeqId.getDocSeqNbr();
        }
        return externalSystemId;
    }

    @PostMapping("/save-userdoc")
    public String saveUserDoc(HttpServletRequest request,
                              RedirectAttributes redirectAttributes,
                              @RequestParam String sessionIdentifier,
                              @RequestParam(required = false) List<String> editedPanels) {
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        String userdocEfilingDataOldOwner = Objects.isNull(sessionUserdoc.getUserdocEFilingData()) ? null : userdocEfilingDataService.selectById(sessionUserdoc.getDocumentId()).getLogUserName();

        boolean exceptionExist = false;
        try {
            CUserdoc cloneSessionUserdoc = (CUserdoc) SerializationUtils.clone(sessionUserdoc);
            UserdocGroundsUtils.loadUserdocGroundEmptyNameData(cloneSessionUserdoc, userdocRootGroundService);
            UserdocUtils.removePersonsWithRolesWhichIsNotInConfiguration(cloneSessionUserdoc, cloneSessionUserdoc.getUserdocType());// This is important for records with wrong data in database
            userdocService.updateUserdoc(cloneSessionUserdoc, true);
            if (Objects.nonNull(userdocEfilingDataOldOwner)) {
                boolean validNotifyAction = efUserChangeService.notifyUserChanged(userdocEfilingDataOldOwner, sessionUserdoc.getUserdocEFilingData().getLogUserName(), getApplicationNumberOnNotifyUserChange(sessionUserdoc));
                if (!validNotifyAction) {
                    redirectAttributes.addFlashAttribute("warningMessage", messageSource.getMessage("efuserchange.notify.warning", null, LocaleContextHolder.getLocale()));
                }
            }
            UserdocSessionUtils.removeAllUserdocSessionObjects(request, sessionIdentifier);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("edit.success.USERDOC", null, LocaleContextHolder.getLocale()));
        } catch (IpasValidationException e) {
            exceptionExist = true;
            redirectAttributes.addFlashAttribute("validationErrors", e.getErrors());
        } catch (AbdocsMissingDocumentException mde) {
            exceptionExist = true;
            if (StringUtils.isEmpty(mde.getRegistrationNumber())) {
                redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("abdocs.not.registered.userdoc", new String[]{sessionUserdoc.getDocumentId().createFilingNumber()}, LocaleContextHolder.getLocale()));
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("abdocs.missing.document.userdoc", new String[]{mde.getRegistrationNumber()}, LocaleContextHolder.getLocale()));
            }
        } catch (Exception ex) {
            exceptionExist = true;
            log.error(ex.getMessage(), ex);
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("edit.error.USERDOC", null, LocaleContextHolder.getLocale()));
        }

        if (exceptionExist) {
            redirectAttributes.addFlashAttribute("userdoc", sessionUserdoc);
            redirectAttributes.addFlashAttribute("sessionObjectIdentifier", sessionIdentifier);
            redirectAttributes.addFlashAttribute("editedPanels", editedPanels);
        }

        return RedirectUtils.redirectToObjectViewPage(UserdocUtils.convertDocumentIdToString(sessionUserdoc.getDocumentId()));
    }

    @PostMapping("/init-panel-session-objects")
    @ResponseStatus(value = HttpStatus.OK)
    public void initPanelSessionObjects(HttpServletRequest request, @RequestParam String panel, @RequestParam String sessionIdentifier) {
        CUserdoc userdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        UserdocPanel panelEnum = UserdocPanel.selectByCode(panel);
        switch (panelEnum) {
            case ReviewerSquad:
                UserdocInitPanelSessionObjectsUtils.initReviewerSquadSessionObjects(request, sessionIdentifier, userdoc);
                break;
            case CourtAppeals:
                UserdocInitPanelSessionObjectsUtils.initCourtAppealsSessionObjects(request, sessionIdentifier, userdoc);
                break;
            case UserdocMainData:
                UserdocInitPanelSessionObjectsUtils.initMainDataPanelSessionObjects(request, sessionIdentifier, userdoc);
                break;
            case Persons:
                UserdocInitPanelSessionObjectsUtils.initPersonPanelSessionObjects(request, sessionIdentifier, userdoc);
                break;
            case Transfer:
                UserdocInitPanelSessionObjectsUtils.initTransferPanelSessionObjects(request, sessionIdentifier, userdoc);
                break;
            case Withdrawal:
                UserdocInitPanelSessionObjectsUtils.initWithdrawalPanelSessionObjects(request, sessionIdentifier, userdoc);
                break;
            case Licenses:
                UserdocInitPanelSessionObjectsUtils.initLicensePanelSessionObjects(request, sessionIdentifier, userdoc);
                break;
            case Renewal:
                UserdocInitPanelSessionObjectsUtils.initRenewalPanelSessionObjects(request, sessionIdentifier, userdoc);
                break;
            case ServiceScope:
                UserdocInitPanelSessionObjectsUtils.initServiceScopePanelSessionObjects(request, sessionIdentifier, userdoc);
                break;
            case Approved:
                UserdocInitPanelSessionObjectsUtils.initApprovedPanelSessionObjects(request, sessionIdentifier, userdoc);
        }
    }

    private CUserdoc selectUserdocFromModel(Model model) {
        return (CUserdoc) model.asMap().get("userdoc");
    }

    private CUserdoc selectUserdocFromDatabase(String docOri, String docLog, Integer docSer, Integer docNbr) {
        CUserdoc userdoc = userdocService.findUserdoc(docOri, docLog, docSer, docNbr);
        if (Objects.isNull(userdoc))
            throw new RuntimeException("Cannot find userdoc !");
        return userdoc;
    }

    private void setSessionUserdoc(Model model, HttpServletRequest request, CUserdoc userdoc) {
        CDocumentId userdocId = userdoc.getDocumentId();
        String sessionIdentifier = HttpSessionUtils.generateIdentifier(new CFileId(userdocId.getDocOrigin(), userdocId.getDocLog(), userdocId.getDocSeries(), userdocId.getDocNbr()));
        HttpSessionUtils.setSessionAttribute(UserdocSessionObjects.SESSION_USERDOC, sessionIdentifier, userdoc, request);
        model.addAttribute("userdoc", userdoc);
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
    }

    private String removeSessionObjectsPage(Model model, HttpServletRequest request, Integer maxSessionObjects) {
        List<String> sessionObjects = HttpSessionUtils.getMainSessionObjects(request);
        model.addAttribute("sessionObjects", sessionObjects);
        model.addAttribute("maxSessionObjects", maxSessionObjects);
        return "ipobjects/common/session/max_session_objects";
    }

    private String openExistingObjectPage(Model model, String sessionIdentifier) {
        model.addAttribute("existingSessionIdentifier", sessionIdentifier);
        return "ipobjects/common/session/open_object";
    }

    private boolean isUserdocEdited(HttpServletRequest request, CUserdoc userdoc, String sessionIdentifierWithoutPrefix) {
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifierWithoutPrefix);
        return DiffGenerator.create(userdoc, sessionUserdoc).isChanged();
    }

    private void setBaseModelAttributes(Model model, CUserdoc userdoc) {
        CDocumentId documentId = userdoc.getDocumentId();
        model.addAttribute("groundsVersion", yamlConfig.getUserDocsLegalGroundTypesVersion());
        model.addAttribute("userdocTypeMap", userdocTypesService.selectAllowedUserdocTypesForChange(userdoc));
        model.addAttribute("receptionUserdocRequest", receptionUserdocRequestService.selectReceptionByDocumentId(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr()));
        model.addAttribute("statusMap", statusService.getStatusMap());
    }

    private void checkPermissions(String docOri, String docLog, Integer docSer, Integer docNbr) {
        if (SecurityUtils.hasRights(SecurityRole.UserdocViewAll))
            return;

        CDocumentId documentId = new CDocumentId(docOri, docLog, docSer, docNbr);
        if (SecurityUtils.hasRights(SecurityRole.UserdocViewOwn) && SecurityUtils.isLoggedUserResponsibleUser(documentId, processService))
            return;

        throw new ForbiddenException();
    }

}
