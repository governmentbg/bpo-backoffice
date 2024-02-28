package com.duosoft.ipas.controller.ipobjects.marklike.common;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.mark.CMarkInternationalReplacement;
import bg.duosoft.ipas.core.model.process.CProcessSimpleData;
import bg.duosoft.ipas.core.service.InternationalMarkService;
import bg.duosoft.ipas.core.service.efiling.IpObjectEfilingDataService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.impl.logging.diff.DiffGenerator;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.nomenclature.*;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.reception.ReceptionInitializationService;
import bg.duosoft.ipas.core.service.reception.ReceptionRequestService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.UserdocType;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.integration.efuserchange.service.EfUserChangeService;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.default_value.DefaultValueUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import bg.duosoft.logging.annotation.LogExecutionTime;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.config.exception.ForbiddenException;
import com.duosoft.ipas.enums.MarkPanel;
import com.duosoft.ipas.util.ApplicationTypeUtils;
import com.duosoft.ipas.util.RedirectUtils;
import com.duosoft.ipas.util.reception.ReceptionUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.mark.MarkInitPanelSessionObjectsUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@LogExecutionTime
public abstract class MarkLikeController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MarkService markService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private FileTypeService fileTypeService;

    @Autowired
    private ExtFileTypeService extFileTypeService;

    @Autowired
    private SignTypeService signTypeService;

    @Autowired
    private UsageRuleService usageRuleService;

    @Autowired
    private LawService lawService;

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private FileService fileService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private ReceptionInitializationService receptionInitializationService;

    @Autowired
    private RelationshipTypeService relationshipTypeService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private ReceptionRequestService receptionRequestService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private AcpCheckReasonService acpCheckReasonService;

    @Autowired
    private AcpCheckResultService acpCheckResultService;

    @Autowired
    private AcpAdministrativePenaltyTypeService acpAdministrativePenaltyTypeService;

    @Autowired
    private AcpAdministrativePenaltyPaymentStatusService acpAdministrativePenaltyPaymentStatusService;

    @Autowired
    private IpObjectEfilingDataService ipObjectEfilingDataService;

    @Autowired
    private EfUserChangeService efUserChangeService;

    @Autowired
    private InternationalMarkService internationalMarkService;


    @Autowired
    private DefaultValueUtils defaultValueUtils;

    public abstract void validateFileType(String fileType);

    public abstract String getViewPage();

    public abstract void setModelAttributes(Model model, CMark mark);

    @InitBinder
    @LogExecutionTime(disabled = true)
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(DateUtils.DATE_FORMAT_DOT), true, 10));
    }

    @RequestMapping(value = {"/detail/{fileSeq}/{fileTyp}/{fileSer}/{fileNbr}"}, method = RequestMethod.GET)
    public String getMarkDetails(Model model,
                                 @PathVariable("fileSeq") String fileSeq,
                                 @PathVariable("fileTyp") String fileTyp,
                                 @PathVariable("fileSer") Integer fileSer,
                                 @PathVariable("fileNbr") Integer fileNbr,
                                 @RequestParam(required = false) boolean reception,
                                 HttpServletRequest request) {
        validateFileType(fileTyp);
        checkPermissions(fileSeq, fileTyp, fileSer, fileNbr);
        CMark mark = selectMarkFromModel(model);
        if (Objects.isNull(mark)) {
            mark = selectMarkFromDatabase(fileSeq, fileTyp, fileSer, fileNbr, reception);
            String sessionIdentifier = HttpSessionUtils.findExistingMainSessionObjectIdentifier(request, mark.getFile().getFileId().createFilingNumber());
            if (StringUtils.isEmpty(sessionIdentifier)) {
                Integer maxSessionObjects = yamlConfig.getMaxSessionObjects();
                if (SessionObjectUtils.hasFreeSpaceForSessionObject(request, maxSessionObjects))
                    setSessionMark(model, request, mark);
                else
                    return removeSessionObjectsPage(model, request, maxSessionObjects);
            } else {
                String sessionIdentifierWithoutPrefix = HttpSessionUtils.getFilingNumberAndUniqueIdentifierFromSessionObject(sessionIdentifier);
                if (isMarkEdited(request, mark, sessionIdentifierWithoutPrefix))
                    return openExistingObjectPage(model, sessionIdentifier);
                else {
                    MarkSessionUtils.removeAllMarkSessionObjects(request, sessionIdentifierWithoutPrefix);
                    setSessionMark(model, request, mark);
                }
            }
        }

        setBaseModelAttributes(model, fileSeq, fileTyp, fileSer, fileNbr, mark);
        setModelAttributes(model, mark);
        CProcessSimpleData processSimpleData = mark.getFile().getProcessSimpleData();
        processService.setResponsibleUserChangeAsRead(processSimpleData.getProcessId().getProcessType(), processSimpleData.getProcessId().getProcessNbr(),
                processSimpleData.getResponsibleUser() != null ? processSimpleData.getResponsibleUser().getUserId() : null);
        return getViewPage();
    }

    protected void checkPermissions(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr) {
        if (SecurityUtils.hasRights(SecurityRole.MarkViewAll))
            return;

        CFileId fileId = new CFileId(fileSeq, fileTyp, fileSer, fileNbr);
        if (SecurityUtils.hasRights(SecurityRole.MarkViewOwn) && SecurityUtils.isLoggedUserResponsibleUser(fileId, processService))
            return;

        throw new ForbiddenException();
    }

    @PostMapping("/save-mark")
    public String saveMark(HttpServletRequest request,
                           RedirectAttributes redirectAttributes,
                           @RequestParam String sessionIdentifier,
                           @RequestParam(required = false) List<String> editedPanels) {
        CMark sessionMark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
        CFileId fileId = sessionMark.getFile().getFileId();
        String markEfilingDataOldOwner = Objects.isNull(sessionMark.getMarkEFilingData()) ? null : ipObjectEfilingDataService.selectById(fileId).getLogUserName();
        try {
            if (sessionMark.isReception()) {
                markService.insertMark(sessionMark);
            } else {
                markService.updateMark(sessionMark);
                if (Objects.nonNull(markEfilingDataOldOwner)){
                    boolean validNotifyAction = efUserChangeService.notifyUserChanged(markEfilingDataOldOwner, sessionMark.getMarkEFilingData().getLogUserName(), fileId.createFilingNumber());
                    if (!validNotifyAction) {
                        redirectAttributes.addFlashAttribute("warningMessage", messageSource.getMessage("efuserchange.notify.warning", null, LocaleContextHolder.getLocale()));
                    }
                }
            }
            MarkSessionUtils.removeAllMarkSessionObjects(request, sessionIdentifier);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("edit.success." + FileType.selectByCode(fileId.getFileType()).name(), null, LocaleContextHolder.getLocale()));
        } catch (IpasValidationException e) {
            redirectAttributes.addFlashAttribute("validationErrors", e.getErrors());
            redirectAttributes.addFlashAttribute("mark", sessionMark);
            redirectAttributes.addFlashAttribute("sessionObjectIdentifier", sessionIdentifier);
            redirectAttributes.addFlashAttribute("editedPanels", editedPanels);
        }

        return RedirectUtils.redirectToObjectViewPage(fileId, false);
    }

    @PostMapping("/init-panel-session-objects")
    @ResponseStatus(value = HttpStatus.OK)
    public void initPanelSessionObjects(HttpServletRequest request, @RequestParam String panel, @RequestParam String sessionIdentifier) {
        CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
        MarkPanel markPanel = MarkPanel.selectByCode(panel);
        switch (markPanel) {
            case MainData:
                MarkInitPanelSessionObjectsUtils.initMainDataPanelSessionObjects(request, sessionIdentifier, mark);
                break;
            case AcpAffectedObjectsData:
                MarkInitPanelSessionObjectsUtils.initAcpAffectedObjectsPanelSessionObjects(request, sessionIdentifier, mark);
                break;
            case AcpViolationPlacesData:
                MarkInitPanelSessionObjectsUtils.initAcpViolationPlacesPanelSessionObjects(request, sessionIdentifier, mark);
                break;
            case AcpTakenItemsData:
                MarkInitPanelSessionObjectsUtils.initAcpTakenItemsPanelSessionObjects(request, sessionIdentifier, mark);
                break;
            case AcpCheckData:
                MarkInitPanelSessionObjectsUtils.initAcpCheckPanelSessionObjects(request, sessionIdentifier, mark);
                break;
            case Persons:
                MarkInitPanelSessionObjectsUtils.initPersonPanelSessionObjects(request, sessionIdentifier, mark);
                break;
            case NiceClasses:
                MarkInitPanelSessionObjectsUtils.initNiceClassesPanelSessionObjects(request, sessionIdentifier, mark);
                break;
            case Claims:
                MarkInitPanelSessionObjectsUtils.initClaimsPanelSessionObjects(request, sessionIdentifier, mark);
                break;
            case InternationalData:
                MarkInitPanelSessionObjectsUtils.initInternationalDataPanelSessionObjects(request, sessionIdentifier, mark);
                break;
        }

    }

    private void setBaseModelAttributes(Model model, String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr, CMark mark) {
        model.addAttribute("acpCheckReasonList", acpCheckReasonService.findAllByApplicationType(mark.getFile().getFilingData().getApplicationType()));
        model.addAttribute("acpCheckResultList", acpCheckResultService.findAll());
        model.addAttribute("acpAdministrativePenaltyTypeList", acpAdministrativePenaltyTypeService.findAll());
        model.addAttribute("acpAdministrativePenaltyPaymentStatusListList", acpAdministrativePenaltyPaymentStatusService.findAll());
        model.addAttribute("signTypesMap", signTypeService.getSignTypesMap());
        model.addAttribute("usageRules", usageRuleService.findAll());
        model.addAttribute("applicationTypesMap", ApplicationTypeUtils.getApplicationTypeMap(applicationTypeService, mark.getFile()));
        model.addAttribute("applicationSubTypes", applicationTypeService.findAllApplicationSubTypesByApplTyp(mark.getFile().getFilingData().getApplicationType()));
        model.addAttribute("fileTypesMap", fileTypeService.getFileTypesMap());
        model.addAttribute("extFileTypes", extFileTypeService.getFileTypesAsMapAndOrdered());
        model.addAttribute("lawMap", lawService.getLawMap());
        model.addAttribute("relationshipTypeMap", relationshipTypeService.selectAll());
        model.addAttribute("receptionRequest", receptionRequestService.selectReceptionByFileId(fileSeq, fileTyp, fileSer, fileNbr));
        model.addAttribute("countryMap", countryService.getCountryMap());
        model.addAttribute("defaultValues", defaultValueUtils.createMarkLikeDefaultValuesObject(mark));
        model.addAttribute("statusMap", statusService.getStatusMap());
        model.addAttribute("internationalRegistrations", processService.selectSubUserdocPartialDataByUserdocTyp(mark.getFile().getProcessId(), UserdocType.MARK_INTERNATIONAL_REGISTRATION_REQUEST));
        CMarkInternationalReplacement internationalReplacement = mark.getMarkInternationalReplacement();
        model.addAttribute("internationalReplacementMarkDetails", Objects.nonNull(internationalReplacement) && Objects.nonNull(internationalReplacement.getReplacementFilingNumber()) ? internationalMarkService.selectInternationalRegistration(internationalReplacement.getReplacementFilingNumber()) : null);
    }

    private CMark selectMarkFromModel(Model model) {
        return (CMark) model.asMap().get("mark");
    }

    private CMark selectMarkFromDatabase(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr, boolean reception) {
        CMark mark;
        if (reception) {
            mark = ReceptionUtils.getReceptionMark(fileSeq, fileTyp, fileSer, fileNbr, markService, fileService, receptionRequestService, receptionInitializationService);
        } else {
            mark = markService.findMark(fileSeq, fileTyp, fileSer, fileNbr, false);
            if (Objects.isNull(mark))
                throw new RuntimeException("Cannot find mark !");
        }
        return mark;
    }

    private boolean isMarkEdited(HttpServletRequest request, CMark mark, String sessionIdentifierWithoutPrefix) {
        CMark sessionMark = MarkSessionUtils.getSessionMark(request, sessionIdentifierWithoutPrefix);
        return DiffGenerator.create(mark, sessionMark).isChanged();
    }

    private void setSessionMark(Model model, HttpServletRequest request, CMark mark) {
        String sessionIdentifier = HttpSessionUtils.generateIdentifier(mark.getFile().getFileId());
        HttpSessionUtils.setSessionAttribute(MarkSessionObjects.SESSION_MARK, sessionIdentifier, mark, request);
        model.addAttribute("mark", mark);
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

}
