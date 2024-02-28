package com.duosoft.ipas.controller.ipobjects.patentlike.common;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.process.CProcessSimpleData;
import bg.duosoft.ipas.core.service.PublicationService;
import bg.duosoft.ipas.core.service.efiling.IpObjectEfilingDataService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.nomenclature.*;
import bg.duosoft.ipas.core.service.patent.PatentAttachmentService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.reception.ReceptionInitializationService;
import bg.duosoft.ipas.core.service.reception.ReceptionRequestService;
import bg.duosoft.ipas.core.service.search.IpoSearchService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.integration.efuserchange.service.EfUserChangeService;
import bg.duosoft.ipas.util.security.SecurityUtils;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.config.exception.ForbiddenException;
import com.duosoft.ipas.enums.PatentPanel;
import com.duosoft.ipas.util.*;
import com.duosoft.ipas.util.reception.ReceptionUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.patent.PatentInitPanelSessionObjectsUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public abstract class PatentLikeController {

    @Autowired
    private PatentService patentService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private LawService lawService;

    @Autowired
    private ReceptionInitializationService receptionInitializationService;

    @Autowired
    private PublicationService publicationService;

    @Autowired
    private PatentAttachmentService patentAttachmentService;

    @Autowired
    private FileTypeService fileTypeService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private RelationshipTypeService relationshipTypeService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private ReceptionRequestService receptionRequestService;

    @Autowired
    private AttachmentTypeService attachmentTypeService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    protected IpoSearchService searchService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private IpObjectEfilingDataService ipObjectEfilingDataService;

    @Autowired
    private EfUserChangeService efUserChangeService;

    public abstract boolean isValidFileType(String fileType);

    public abstract String getViewPage();

    public abstract void setModelAttributes(Model model, CPatent patent);


    @RequestMapping(value = "/detail/{fileSeq}/{fileTyp}/{fileSer}/{fileNbr}")
    public String getPatentLikeObjectDetails(Model model,
                                             @RequestParam(required = false) boolean reception,
                                             @PathVariable("fileSeq") String fileSeq,
                                             @PathVariable("fileTyp") String fileTyp,
                                             @PathVariable("fileSer") Integer fileSer,
                                             @PathVariable("fileNbr") Integer fileNbr, HttpServletRequest request) {
        if (!isValidFileType(fileTyp))
            throw new RuntimeException("Invalid file type: " + fileTyp);

        checkPermissions(fileSeq, fileTyp, fileSer, fileNbr);
        CPatent patent = selectPatentFromModel(model);
        if (Objects.isNull(patent)) {
            patent = selectPatentFromDatabase(fileSeq, fileTyp, fileSer, fileNbr, reception);
            PatentDrawingUtils.initCPatentDrawings(patent);
            String sessionIdentifier = HttpSessionUtils.findExistingMainSessionObjectIdentifier(request, patent.getFile().getFileId().createFilingNumber());
            if (StringUtils.isEmpty(sessionIdentifier)) {
                Integer maxSessionObjects = yamlConfig.getMaxSessionObjects();
                if (SessionObjectUtils.hasFreeSpaceForSessionObject(request, maxSessionObjects)) {
                    setSessionPatent(model, request, patent);
                } else
                    return removeSessionObjectsPage(model, request, maxSessionObjects);
            } else {
                String sessionIdentifierWithoutPrefix = HttpSessionUtils.getFilingNumberAndUniqueIdentifierFromSessionObject(sessionIdentifier);
                if (isPatentEdited(request, sessionIdentifierWithoutPrefix))
                    return openExistingObjectPage(model, sessionIdentifier);
                else {
                    PatentSessionUtils.removeAllPatentSessionObjects(request, sessionIdentifierWithoutPrefix);
                    setSessionPatent(model, request, patent);
                }
            }
        }

        setBaseModelAttributes(model, fileSeq, fileTyp, fileSer, fileNbr, patent);
        setModelAttributes(model, patent);
        CProcessSimpleData processSimpleData = patent.getFile().getProcessSimpleData();
        processService.setResponsibleUserChangeAsRead(processSimpleData.getProcessId().getProcessType(), processSimpleData.getProcessId().getProcessNbr(),
                processSimpleData.getResponsibleUser() != null ? processSimpleData.getResponsibleUser().getUserId() : null);
        return getViewPage();
    }

    @PostMapping("/save-patent")
    public String savePatent(HttpServletRequest request,
                             RedirectAttributes redirectAttributes,
                             @RequestParam String sessionIdentifier,
                             @RequestParam(required = false) List<String> editedPanels) {
        CPatent sessionPatent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        CFileId fileId = sessionPatent.getFile().getFileId();
        String patentEfilingDataOldOwner = Objects.isNull(sessionPatent.getPatentEFilingData()) ? null : ipObjectEfilingDataService.selectById(fileId).getLogUserName();
        CPatent cloneSessionPatent = (CPatent) SerializationUtils.clone(sessionPatent);
        PatentDrawingUtils.loadCPatentEmptyDrawings(cloneSessionPatent, patentService);
        PatentAttachmentUtils.loadCPatentEmptyAttachments(cloneSessionPatent, patentAttachmentService);
        try {
            if (cloneSessionPatent.isReception()) {
                patentService.insertPatent(cloneSessionPatent);
            } else {
                patentService.updatePatent(cloneSessionPatent);
                if (PatentSpcUtils.hasSpcObjects(cloneSessionPatent)) {
                    redirectAttributes.addFlashAttribute("warningMessage", messageSource.getMessage("edit.warning." + FileType.selectByCode(fileId.getFileType()).name(), null, LocaleContextHolder.getLocale()));
                }
                if (Objects.nonNull(patentEfilingDataOldOwner)) {
                    boolean validNotifyAction = efUserChangeService.notifyUserChanged(patentEfilingDataOldOwner, sessionPatent.getPatentEFilingData().getLogUserName(), fileId.createFilingNumber());
                    if (!validNotifyAction) {
                        redirectAttributes.addFlashAttribute("warningMessage", messageSource.getMessage("efuserchange.notify.warning", null, LocaleContextHolder.getLocale()));
                    }
                }
            }
            PatentSessionUtils.removeAllPatentSessionObjects(request, sessionIdentifier);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("edit.success." + FileType.selectByCode(fileId.getFileType()).name(), null, LocaleContextHolder.getLocale()));
        } catch (IpasValidationException e) {
            redirectAttributes.addFlashAttribute("validationErrors", e.getErrors());
            redirectAttributes.addFlashAttribute("patent", sessionPatent);
            redirectAttributes.addFlashAttribute("sessionObjectIdentifier", sessionIdentifier);
            redirectAttributes.addFlashAttribute("editedPanels", editedPanels);
        }
        return RedirectUtils.redirectToObjectViewPage(fileId, false);
    }

    @PostMapping("/init-panel-session-objects")
    @ResponseStatus(value = HttpStatus.OK)
    public void initPanelSessionObjects(HttpServletRequest request, @RequestParam String panel, @RequestParam String sessionIdentifier) {
        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        PatentPanel patentPanel = PatentPanel.selectByCode(panel);
        switch (patentPanel) {
            case MainData:
                PatentInitPanelSessionObjectsUtils.initMainDataPanelSessionObjects(request, sessionIdentifier, patent);
                break;
            case PlantMainData:
                PatentInitPanelSessionObjectsUtils.initMainDataPanelSessionObjects(request, sessionIdentifier, patent);
                break;
            case ClaimsData:
                PatentInitPanelSessionObjectsUtils.initClaimsPanelSessionObjects(request, sessionIdentifier, patent);
                break;
            case CitationsData:
                PatentInitPanelSessionObjectsUtils.initCitationsPanelSessionObjects(request, sessionIdentifier, patent);
                break;
            case DesignDrawingsData:
                PatentInitPanelSessionObjectsUtils.initDesignDrawingsPanelSessionObjects(request, sessionIdentifier);
                break;
            case PublishedDrawingsData:
                PatentInitPanelSessionObjectsUtils.initPublishedDrawingsPanelSessionObjects(request, sessionIdentifier, patent);
                break;
            case Persons:
                PatentInitPanelSessionObjectsUtils.initPersonPanelSessionObjects(request, sessionIdentifier, patent);
                break;
            case IpcData:
                PatentInitPanelSessionObjectsUtils.initIpcDataPanelSessionObjects(request, sessionIdentifier, patent);
                break;
            case CpcData:
                PatentInitPanelSessionObjectsUtils.initCpcDataPanelSessionObjects(request, sessionIdentifier, patent);
                break;
            case RightsData:
                PatentInitPanelSessionObjectsUtils.initRightsDataPanelSessionObjects(request, sessionIdentifier, patent);
                break;
        }
    }


    private CPatent selectPatentFromModel(Model model) {
        return (CPatent) model.asMap().get("patent");
    }

    private CPatent selectPatentFromDatabase(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr, boolean reception) {
        CPatent patent;
        if (reception) {
            patent = ReceptionUtils.getReceptionPatent(fileSeq, fileTyp, fileSer, fileNbr, patentService, fileService, receptionRequestService, receptionInitializationService);
        } else {
            patent = patentService.findPatent(fileSeq, fileTyp, fileSer, fileNbr, false);
            if (Objects.isNull(patent))
                throw new RuntimeException("Cannot find patent !");
        }
        return patent;
    }


    private void setSessionPatent(Model model, HttpServletRequest request, CPatent patent) {
        String sessionIdentifier = HttpSessionUtils.generateIdentifier(patent.getFile().getFileId());
        HttpSessionUtils.setSessionAttribute(PatentSessionObject.SESSION_PATENT, sessionIdentifier, patent, request);
        PatentSessionUtils.setSessionAttributePatentHash(request, sessionIdentifier);
        model.addAttribute("patent", patent);
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
    }

    private String removeSessionObjectsPage(Model model, HttpServletRequest request, Integer maxSessionObjects) {
        List<String> sessionObjects = HttpSessionUtils.getMainSessionObjects(request);
        model.addAttribute("sessionObjects", sessionObjects);
        model.addAttribute("maxSessionObjects", maxSessionObjects);
        return "ipobjects/common/session/max_session_objects";
    }

    private boolean isPatentEdited(HttpServletRequest request, String sessionIdentifierWithoutPrefix) {
        return PatentLikeObjectsUtils.isSessionPatentEdited(request, sessionIdentifierWithoutPrefix);
    }

    private String openExistingObjectPage(Model model, String sessionIdentifier) {
        model.addAttribute("existingSessionIdentifier", sessionIdentifier);
        return "ipobjects/common/session/open_object";
    }

    private void setBaseModelAttributes(Model model, String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr, CPatent patent) {
        model.addAttribute("fileTypesMap", fileTypeService.getFileTypesMap());
        model.addAttribute("applicationTypesMap", ApplicationTypeUtils.getApplicationTypeMap(applicationTypeService, patent.getFile()));
        model.addAttribute("relationshipTypeMap", relationshipTypeService.selectAll());
        model.addAttribute("applicationSubTypes", applicationTypeService.findAllApplicationSubTypesByApplTyp(patent.getFile().getFilingData().getApplicationType()));
        model.addAttribute("lawMap", lawService.getLawMap());
        model.addAttribute("receptionRequest", receptionRequestService.selectReceptionByFileId(fileSeq, fileTyp, fileSer, fileNbr));
        model.addAttribute("countryMap", countryService.getCountryMap());
        model.addAttribute("statusMap", statusService.getStatusMap());
        model.addAttribute("attachmentTypes", attachmentTypeService.findAllByTypeAndOrder(fileTyp));

    }

    private void checkPermissions(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr) {
        if (SecurityUtils.hasRights(SecurityRole.PatentViewAll))
            return;

        CFileId fileId = new CFileId(fileSeq, fileTyp, fileSer, fileNbr);
        if (SecurityUtils.hasRights(SecurityRole.PatentViewOwn) && SecurityUtils.isLoggedUserResponsibleUser(fileId, processService))
            return;

        throw new ForbiddenException();
    }

}
