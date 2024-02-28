package com.duosoft.ipas.controller.ipobjects.common.reception.create;

import bg.duosoft.abdocs.model.CreateDocEmailDto;
import bg.duosoft.abdocs.model.DocCorrespondents;
import bg.duosoft.abdocs.model.DocFile;
import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.ebddownload.CEbdPatent;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.miscellaneous.CAbdocsDocumentType;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.reception.*;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.core.model.util.TempID;
import bg.duosoft.ipas.core.service.dailylog.DailyLogService;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.core.service.nomenclature.FileTypeGroupService;
import bg.duosoft.ipas.core.service.nomenclature.FileTypeService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.reception.*;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.reception.ReceptionValidator;
import bg.duosoft.ipas.enums.EuPatentReceptionType;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.ReceptionType;
import bg.duosoft.ipas.enums.SubmissionType;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.integration.abdocs.converter.CorrespondentConverter;
import bg.duosoft.ipas.integration.abdocs.utils.CorrespondentUtils;
import bg.duosoft.ipas.integration.ebddownload.service.EbdPatentService;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.eupatent.EuPatentUtils;
import bg.duosoft.ipas.util.general.BasicUtils;
import bg.duosoft.ipas.util.json.JsonUtil;
import bg.duosoft.ipas.util.security.SecurityUtils;
import bg.duosoft.security.bpo.authorization.domain.UserDetails;
import com.duosoft.ipas.session.reception.model.ReceptionEmailSession;
import com.duosoft.ipas.session.reception.model.ReceptionEmailSessions;
import com.duosoft.ipas.util.PersonUtils;
import com.duosoft.ipas.util.ReceptionTypeUtils;
import com.duosoft.ipas.util.json.CreateReceptionData;
import com.duosoft.ipas.util.json.OriginalReceptionResult;
import com.duosoft.ipas.util.json.ReceptionWarnings;
import com.duosoft.ipas.util.reception.CreateReceptionUtils;
import com.duosoft.ipas.util.reception.ReceptionUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.reception.ReceptionSessionUtils;
import com.duosoft.ipas.webmodel.*;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/reception/create")
@PreAuthorize("hasAnyAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ReceptionCreator.code(),T(bg.duosoft.ipas.enums.security.SecurityRole).ReceptionCreatorFromExistingDocument.code())")
public class CreateReceptionController {
    private static final String SESSION_RECEPTION_FORM = "sessionReceptionForm";

    @Autowired
    private TempID tempID;

    @Autowired
    private PersonService personService;

    @Autowired
    private ReceptionTypeService receptionTypeService;

    @Autowired
    private SubmissionTypeService submissionTypeService;

    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private AbdocsService abdocsService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private DailyLogService dailyLogService;

    @Autowired
    private UserdocReceptionRelationService userdocReceptionRelationService;

    @Autowired
    private EbdPatentService ebdPatentService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private ConfigParamService configParamService;

    @Autowired
    private IpasValidatorCreator validatorCreator;

    @Autowired
    private ReceptionRequestService receptionRequestService;

    @Autowired
    private ReceptionUserdocRequestService receptionUserdocRequestService;

    @Autowired
    private OriginalExpectedService originalExpectedService;

    @Autowired
    private UserdocService userdocService;

    @Autowired
    private DocService docService;

    @Autowired
    private AbdocsDocumentTypeService abdocsDocumentTypeService;

    @Autowired
    private ReceptionEmailSessions receptionEmailSessions;

    @Autowired
    private FileTypeGroupService fileTypeGroupService;

    @Autowired
    private CorrespondentConverter correspondentConverter;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        CustomDateEditor customDateEditor = new CustomDateEditor(new SimpleDateFormat(DateUtils.DATE_FORMAT_DOT), true, 10);
        binder.registerCustomEditor(Date.class, customDateEditor);
    }


    private void authorityCheckOnGetReceptionForm(Integer existingDocumentId) {

        if (Objects.nonNull(existingDocumentId) && !SecurityUtils.hasRights(SecurityRole.ReceptionCreatorFromExistingDocument.code())) {
            throw new RuntimeException("Missing rights !");
        }

        if (Objects.isNull(existingDocumentId) && !SecurityUtils.hasRights(SecurityRole.ReceptionCreator.code())) {
            throw new RuntimeException("Missing rights !");
        }

    }


    @GetMapping
    public String getReceptionForm(Model model, HttpServletRequest request,
                                   @RequestParam(value = "docflowDocumentId", required = false) Integer docflowDocumentId,
                                   @RequestParam(value = "emailId", required = false) Integer emailId,
                                   @RequestParam(value = "existingDocumentId", required = false) Integer existingDocumentId) {
        authorityCheckOnGetReceptionForm(existingDocumentId);

        if (emailId != null && docflowDocumentId != null) {
            throw new RuntimeException("Either emailId or docflowDocumentId should be empty!");
        }

        if (Objects.nonNull(emailId)) {
            ReceptionEmailSession activeReceptionEmailSession = selectActiveReceptionEmailSession(emailId);
            if (Objects.nonNull(activeReceptionEmailSession) && !activeReceptionEmailSession.getUserId().equals(SecurityUtils.getLoggedUserId())) {
                model.addAttribute("activeReceptionEmailSession", activeReceptionEmailSession);
                return "ipobjects/common/reception/email/blocked_email";
            }
        }

        ReceptionForm existingReceptionForm = (ReceptionForm) model.asMap().get("receptionForm");
        if (Objects.isNull(existingReceptionForm)) {
            ReceptionSessionUtils.removeSessionReceptions(request);
            String receptionSessionKey = HttpSessionUtils.generateIdentifier(SESSION_RECEPTION_FORM);
            ReceptionForm receptionForm = createInitialReceptionFоrm(docflowDocumentId, emailId, existingDocumentId);
            HttpSessionUtils.setSessionAttribute(receptionSessionKey, receptionForm, request);
            ReceptionUtils.addReceptionPanelBasicModelAttributes(model, receptionForm, receptionSessionKey, submissionTypeService, receptionTypeService, dailyLogService, fileTypeGroupService);
        } else {
            String receptionSessionKey = (String) model.asMap().get("receptionSessionKey");
            ReceptionUtils.addReceptionPanelBasicModelAttributes(model, existingReceptionForm, receptionSessionKey, submissionTypeService, receptionTypeService, dailyLogService, fileTypeGroupService);
            HttpSessionUtils.setSessionAttribute(receptionSessionKey, existingReceptionForm, request);
        }

        if (Objects.nonNull(emailId)){
            model.addAttribute("receptionFromEmail",true);
            model.addAttribute("receptionExistedRecord",false);
        }else{
            model.addAttribute("receptionExistedRecord",true);
            model.addAttribute("receptionFromEmail",false);
        }

        return "ipobjects/common/reception/reception_form";
    }

    private ReceptionEmailSession selectActiveReceptionEmailSession(Integer emailId) {
        if (Objects.isNull(emailId))
            return null;

        return receptionEmailSessions.getReceptionEmailSessionList().stream()
                .filter(receptionEmailSession -> receptionEmailSession.getEmailId() == emailId)
                .findFirst()
                .orElse(null);
    }

    @PostMapping("/change-reception-type")
    public String changeReceptionType(Model model, HttpServletRequest request, @RequestParam String sessionIdentifier, @RequestParam String data) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);

        CreateReceptionData formData = JsonUtil.readJson(data, CreateReceptionData.class);
        ReceptionForm receptionForm = ReceptionSessionUtils.setFormDataToSessionReception(request, sessionIdentifier, formData, receptionTypeService);
        if (receptionForm.getReceptionType().equals(ReceptionType.USERDOC.code())) {
            receptionForm.setUserdoc(new ReceptionUserdocForm());
            receptionForm.getUserdoc().setFileTypeGroup(FileType.MARK.code()); // Default
        }
        if (receptionForm.getReceptionType().equals(ReceptionType.EU_PATENT.code())) {// Default
            receptionForm.setEuPatent(new ReceptionEuPatentForm());
            receptionForm.getEuPatent().setType(EuPatentReceptionType.VALIDATION.code());
            receptionForm.setName(null);
            receptionForm.setOwnershipData(new COwnershipData());
        }
        if (receptionForm.getReceptionType().equals(ReceptionType.MARK.code())) {
            receptionForm.getMark().setFigurativeMark(false); // Default
        }

        receptionForm.setAdditionalUserdocs(new ArrayList<>());// Clear additional userdocs
        ReceptionUtils.addReceptionPanelBasicModelAttributes(model, receptionForm, sessionIdentifier, submissionTypeService, receptionTypeService, dailyLogService, fileTypeGroupService);
        return "ipobjects/common/reception/reception_panel :: panel";
    }

    @PostMapping("/remove-attachment")
    public String removeDocflowAttachment(Model model, HttpServletRequest request, @RequestParam String sessionIdentifier, @RequestParam Integer attSeqId) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);

        ReceptionForm sessionReceptionForm = HttpSessionUtils.getSessionAttribute(sessionIdentifier, request);

        List<ReceptionDocflowAttachment> att = sessionReceptionForm.getAttachments();
        if (att != null) {
            att.stream().filter(r -> r.getAttSeqId() == attSeqId).forEach(r -> r.setTransferAttachment(false));
        }

        model.addAttribute("hasAttachmentsFrame", true);
        model.addAttribute("attachments", att);
        return "ipobjects/common/reception/reception_attachments :: attachments";

    }

    @PostMapping("/reload-attachments")
    public String reloadDocflowAttachments(Model model, HttpServletRequest request, @RequestParam String sessionIdentifier) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);

        ReceptionForm sessionReceptionForm = HttpSessionUtils.getSessionAttribute(sessionIdentifier, request);

        List<ReceptionDocflowAttachment> att = sessionReceptionForm.getAttachments();
        if (att != null) {
            att.stream().forEach(r -> r.setTransferAttachment(true));
        }

        model.addAttribute("hasAttachmentsFrame", true);
        model.addAttribute("attachments", att);
        return "ipobjects/common/reception/reception_attachments :: attachments";

    }

    @PostMapping("/change-submission-type")
    public String changeSubmissionType(Model model, HttpServletRequest request, @RequestParam String sessionIdentifier, @RequestParam String data) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);

        CreateReceptionData formData = JsonUtil.readJson(data, CreateReceptionData.class);
        formData.setOriginalExpected(ReceptionUtils.getOriginalExpectedDefaultValue(formData.getSubmissionType()));

        ReceptionForm receptionForm = ReceptionSessionUtils.setFormDataToSessionReception(request, sessionIdentifier, formData, receptionTypeService);
        ReceptionUtils.addReceptionPanelBasicModelAttributes(model, receptionForm, sessionIdentifier, submissionTypeService, receptionTypeService, dailyLogService, fileTypeGroupService);
        if (Objects.nonNull(receptionForm.getUserdoc())) {
            model.addAttribute("userdocReceptionRelations", CreateReceptionUtils.getUserdocTypes(receptionForm, userdocService, receptionTypeService, userdocReceptionRelationService));
            model.addAttribute("relatedUserdocObjectDetails", CreateReceptionUtils.fillRelatedUserdocObjectDetails(receptionForm, fileService, userdocService, docService));
        }
        return "ipobjects/common/reception/reception_panel :: panel";
    }


    @PostMapping("/is-original")
    @ResponseBody
    public OriginalReceptionResult checkOriginal(HttpServletRequest request, @RequestParam String sessionIdentifier, @RequestParam String data) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);

        CreateReceptionData formData = JsonUtil.readJson(data, CreateReceptionData.class);
        ReceptionForm receptionForm = ReceptionSessionUtils.setFormDataToSessionReception(request, sessionIdentifier, formData, receptionTypeService);

        IpasValidator<CReception> validator = validatorCreator.create(true, ReceptionValidator.class);
        List<ValidationError> validationErrors = validator.validate(toCReception(receptionForm));
        if (!CollectionUtils.isEmpty(validationErrors))
            return OriginalReceptionResult.notOriginalReceptionRequest();

        return ReceptionUtils.searchOriginalExpectedReceptionRequest(receptionForm, ebdPatentService, fileService, receptionTypeService, receptionRequestService, receptionUserdocRequestService, messageSource, docService, request);
    }

    @PostMapping("/check-data")
    @ResponseBody
    public ReceptionWarnings checkData(HttpServletRequest request, @RequestParam String sessionIdentifier, @RequestParam String data) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);

        CreateReceptionData formData = JsonUtil.readJson(data, CreateReceptionData.class);
        ReceptionForm receptionForm = ReceptionSessionUtils.setFormDataToSessionReception(request, sessionIdentifier, formData, receptionTypeService);

        ReceptionWarnings receptionWarnings = new ReceptionWarnings(new ArrayList<>());
        List<String> warnings = receptionWarnings.getWarnings();

        if (ReceptionTypeUtils.isEuPatent(receptionForm))
            fillEuPatentWarnings(receptionForm, warnings, messageSource, ebdPatentService, fileService, processService, configParamService);

        return receptionWarnings;
    }

    @PostMapping
    public String createReception(Model model, RedirectAttributes redirectAttributes, HttpServletRequest request,
                                  @RequestParam String sessionIdentifier,
                                  @ModelAttribute ReceptionForm receptionForm,
                                  @RequestParam(required = false) Integer originalRequestId,
                                  @RequestParam(required = false) boolean continueWithSameData) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);

        //TODO We should think about this. I need access to roles in ipas-persistence
        checkEntryDate(receptionForm);

        ReceptionForm sessionReceptionForm = HttpSessionUtils.getSessionAttribute(sessionIdentifier, request);
        receptionForm.setOwnershipData(sessionReceptionForm.getOwnershipData());
        receptionForm.setRepresentationData(sessionReceptionForm.getRepresentationData());
        receptionForm.setDocflowDocument(sessionReceptionForm.getDocflowDocument());
        receptionForm.setDocflowEmailDocument(sessionReceptionForm.getDocflowEmailDocument());
        if (Objects.nonNull(sessionReceptionForm.getInitialDocument())) {
            receptionForm.setInitialDocument(sessionReceptionForm.getInitialDocument());
            if (receptionForm.getReceptionType().equals(ReceptionType.ACP.code())){
                receptionForm.setName(sessionReceptionForm.getInitialDocument().getDescription());
            }
        }


        checkActiveReceptionEmailSession(receptionForm);

        boolean isAbdocsConnectionOK = abdocsService.isConnectionOK();
        if (isAbdocsConnectionOK) {
            receptionForm.setEntryDate(toExactEntryDate(receptionForm.getEntryDate()));
            if (Objects.nonNull(originalRequestId)) {
                String returnUrl = updateExistingReception(model, redirectAttributes, receptionForm, originalRequestId, sessionIdentifier, continueWithSameData);
                if (Objects.nonNull(returnUrl))
                    return returnUrl;
            } else {
                String returnUrl = createNewReception(model, redirectAttributes, sessionIdentifier, receptionForm, continueWithSameData);
                if (Objects.nonNull(returnUrl))
                    return returnUrl;
            }
        } else
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("abdocs.connection.error", null, LocaleContextHolder.getLocale()));

        return createRedirectReceptionUrl(receptionForm);
    }

    private Date toExactEntryDate(Date entryDateWithoutHoursMinutesAndSeconds) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime entryDateLocalDateTime = DateUtils.convertToLocalDatTime(entryDateWithoutHoursMinutesAndSeconds);
        if (DateUtils.isDateEqualsToCurrentDate(entryDateWithoutHoursMinutesAndSeconds)) {
            LocalDateTime entryDateWithHoursMinutesAndSeconds = LocalDateTime.of(
                    entryDateLocalDateTime.getYear(),
                    entryDateLocalDateTime.getMonth(),
                    entryDateLocalDateTime.getDayOfMonth(),
                    now.getHour(),
                    now.getMinute(),
                    now.getSecond());
            return DateUtils.convertToDate(entryDateWithHoursMinutesAndSeconds);
        }
        return entryDateWithoutHoursMinutesAndSeconds;
    }

    private void checkEntryDate(@ModelAttribute ReceptionForm receptionForm) {
        if (!SecurityUtils.hasRights(SecurityRole.ReceptionEntryDateChange)) {
            Date entryDate = receptionForm.getEntryDate();
            Date workingDate = dailyLogService.getWorkingDate();
            if (!entryDate.equals(workingDate))
                throw new RuntimeException("Missing rights for change entry date !");
        }
    }

    private void checkActiveReceptionEmailSession(ReceptionForm receptionForm) {
        ReceptionDocflowEmailDocumentForm docflowEmailDocument = receptionForm.getDocflowEmailDocument();
        if (Objects.nonNull(docflowEmailDocument)) {
            int emailId = docflowEmailDocument.getEmailId();
            ReceptionEmailSession activeReceptionEmailSession = selectActiveReceptionEmailSession(emailId);
            if (Objects.isNull(activeReceptionEmailSession) || !activeReceptionEmailSession.getUserId().equals(SecurityUtils.getLoggedUserId())) {
                throw new RuntimeException("Reception email session error !");
            }
        }
    }

    private String createNewReception(Model model, RedirectAttributes redirectAttributes, String sessionIdentifier, ReceptionForm receptionForm, boolean continueWithSameData) {
        CReceptionResponse response = createReception(redirectAttributes, sessionIdentifier, receptionForm);
        if (response != null && Objects.nonNull(response.getDocflowDocumentId()))
            return selectSuccessPage(model, redirectAttributes, receptionForm, sessionIdentifier, continueWithSameData, response.getDocflowDocumentId());

        return null;
    }

    private String updateExistingReception(Model model, RedirectAttributes redirectAttributes, ReceptionForm receptionForm, Integer originalRequestId, String sessionIdentifier, boolean continueWithSameData) {
        Integer abdocsDocumentId = originalExpectedService.updateOriginalExpectedOnReception(toCReception(receptionForm), originalRequestId);
        if (Objects.nonNull(abdocsDocumentId))
            return selectSuccessPage(model, redirectAttributes, receptionForm, sessionIdentifier, continueWithSameData, abdocsDocumentId);
        else
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("reception.update.original.expected.error", null, LocaleContextHolder.getLocale()));

        return null;
    }

    private String selectSuccessPage(Model model, RedirectAttributes redirectAttributes, ReceptionForm receptionForm, String sessionIdentifier, boolean continueWithSameData, Integer abdocsDocumentId) {
        String url = abdocsService.generateViewDocumentUrl(abdocsDocumentId);
        if (continueWithSameData) {
            setRedirectAttributes(redirectAttributes, sessionIdentifier, receptionForm, null);
            Document document = abdocsService.selectDocumentById(abdocsDocumentId);
            CReceptionType cReceptionType = receptionTypeService.selectById(receptionForm.getReceptionType());
            redirectAttributes.addFlashAttribute("continueWithSameData", true);
            redirectAttributes.addFlashAttribute("successRegistrationReceptionType", cReceptionType);
            if (!ReceptionTypeUtils.isUserdoc(receptionForm)) {
                CAbdocsDocumentType abdocsDocumentType = abdocsDocumentTypeService.selectByType(cReceptionType.getFileType());
                if (Objects.nonNull(abdocsDocumentId)) {
                    redirectAttributes.addFlashAttribute("documentTypeName", abdocsDocumentType.getName());
                }
            }
            redirectAttributes.addFlashAttribute("document", document);
            redirectAttributes.addFlashAttribute("url", url);
            if (ReceptionTypeUtils.isEuPatent(receptionForm)) {
                Document euPatentDocument = abdocsService.selectDocumentById(document.getParentId());
                redirectAttributes.addFlashAttribute("euPatentDocument", euPatentDocument);
            }
            return createRedirectOnSuccess(receptionForm, document.getRegUri(), redirectAttributes);
        } else {
            model.addAttribute("url", url);
            return "base/redirect :: redirect";
        }
    }

    private String createRedirectOnSuccess(ReceptionForm form, String regUri, RedirectAttributes redirectAttributes) {
        if (Objects.nonNull(form.getInitialDocument()) && !StringUtils.isEmpty(regUri)) {
            if (Objects.nonNull(form.getUserdoc())) {
                CDocumentId cDocumentId = docService.selectDocumentIdByExternalSystemId(regUri);
                redirectAttributes.addAttribute("filingNumber", cDocumentId.createFilingNumber());
            } else if (Objects.nonNull(form.getEuPatent())) {
                List<String> fileTypes = new ArrayList<>();
                fileTypes.add(FileType.EU_PATENT.code());
                List<CFile> allByFileNbrAndFileType = fileService.findAllByFileNbrAndFileType(form.getEuPatent().getObjectNumber(), fileTypes);
                redirectAttributes.addAttribute("filingNumber", allByFileNbrAndFileType.get(0).getFileId().createFilingNumber());
            } else {
                redirectAttributes.addAttribute("filingNumber", regUri);
            }
            return "redirect:/ipobject/view";
        }
        return createRedirectReceptionUrl(form);
    }

    private String createRedirectReceptionUrl(ReceptionForm form) {
        String res = "redirect:/reception/create";
        if (form.getDocflowDocument() != null) {
            res += "?docflowDocumentId=" + form.getDocflowDocument().getDocumentId();
        }
        else if (form.getDocflowEmailDocument() != null) {
            res += "?emailId=" + form.getDocflowEmailDocument().getEmailId();
        }
        else if (form.getInitialDocument() != null) {
            res += "?existingDocumentId=" + form.getInitialDocument().getDocumentId();
        }
        return res;
    }


    private void setRedirectAttributes(RedirectAttributes redirectAttributes, String sessionIdentifier, ReceptionForm receptionForm, IpasValidationException exception) {
        redirectAttributes.addFlashAttribute("receptionForm", receptionForm);
        redirectAttributes.addFlashAttribute("receptionSessionKey", sessionIdentifier);
        if (Objects.nonNull(receptionForm.getUserdoc())) {
            redirectAttributes.addFlashAttribute("userdocReceptionRelations", CreateReceptionUtils.getUserdocTypes(receptionForm, userdocService, receptionTypeService, userdocReceptionRelationService));
            redirectAttributes.addFlashAttribute("relatedUserdocObjectDetails", CreateReceptionUtils.fillRelatedUserdocObjectDetails(receptionForm, fileService, userdocService, docService));
        }
        if (Objects.nonNull(exception))
            redirectAttributes.addFlashAttribute("validationErrors", exception.getErrors());
    }

    private CReceptionResponse createReception(RedirectAttributes redirectAttributes, @RequestParam String sessionIdentifier, @ModelAttribute ReceptionForm receptionForm) {
        try {
            CReception creception = toCReception(receptionForm);
            /*if (creception.isEuPatentRequest()) {
                return receptionService.createEuPatentReception(creception);
            } else if (creception.isUserdocRequest()) {
                return receptionService.createUserdocReception(creception);
            } else {
                return receptionService.createFileReception(creception);
            }*/
            CReceptionResponse res = receptionService.createReception(creception);
            if (receptionForm.getDocflowEmailDocument() != null) {
                try {
                    abdocsService.updateEmailStatusAsRead(receptionForm.getDocflowEmailDocument().getEmailId(), res.getDocflowDocumentId());
                } catch (Exception e) {
                    log.error("Cannot mark email " + receptionForm.getDocflowEmailDocument().getEmailId() + " as read", e);
                }
            }
            return res;
        } catch (IpasValidationException e) {
            setRedirectAttributes(redirectAttributes, sessionIdentifier, receptionForm, e);
            return null;
        }
    }

    private CReception toCReception(ReceptionForm form) {
        CReception res = new CReception();
        List<ReceptionDocflowAttachment> receptionDocflowAttachments = null;
        res.setRegisterInDocflowSystem(true);
        res.setEntryDate(DateUtils.convertToDate(DateUtils.convertToLocalDate(form.getEntryDate())));
        res.setOriginalExpected(form.getOriginalExpected());
        res.setOwnershipData(form.getOwnershipData());
        if (Objects.nonNull(form.getInitialDocument())) {
            res.setInitialDocumentId(form.getInitialDocument().getDocumentId());
            res.setRegisterAsAdministrator(true);
            receptionDocflowAttachments = form.getInitialDocument().getAttachments();
        }
        res.setRepresentationData(form.getRepresentationData());
        res.setSubmissionType(form.getSubmissionType());
        res.setNotes(form.getNotes());
        if (ReceptionTypeUtils.isUserdoc(form)) {
            String objectNumber = form.getUserdoc().getObjectNumber();
            CReceptionUserdoc ud = new CReceptionUserdoc();
            if (!StringUtils.isEmpty(objectNumber)) {
                if (BasicUtils.isNumberOfUserdoc(objectNumber)) {
                    ud.setDocumentId(BasicUtils.createCDocumentId(objectNumber));
                } else {
                    ud.setFileId(BasicUtils.createCFileId(objectNumber));
                }
            }
            ud.setUserdocType(form.getUserdoc().getUserdocType());
            res.setUserdoc(ud);
        } else if (ReceptionTypeUtils.isEuPatent(form)) {
            CReceptionEuPatent eup = new CReceptionEuPatent();
            eup.setObjectNumber(form.getEuPatent().getObjectNumber());
            eup.setUserdocType(form.getEuPatent().getType());
            res.setEuPatent(eup);
        }
        if (!ReceptionTypeUtils.isUserdoc(form)) {
            res.setFile(new CReceptionFile());
            res.getFile().setApplicationType(receptionTypeService.selectById(form.getReceptionType()).getApplTyp());
            if (ReceptionTypeUtils.isMark(form) && Objects.equals(true, form.getMark().getFigurativeMark())) {
                res.getFile().setEmptyTitle(true);
            } else {
                res.getFile().setTitle(form.getName());
            }

        }
        if (form.getDocflowDocument() != null && !CollectionUtils.isEmpty(form.getDocflowDocument().getAttachments())) {
            receptionDocflowAttachments = form.getDocflowDocument().getAttachments();
        }
        if (Objects.nonNull(form.getDocflowEmailDocument())) {
            res.setDocflowEmailId(form.getDocflowEmailDocument().getEmailId());
            if (!CollectionUtils.isEmpty(form.getDocflowEmailDocument().getAttachments())) {
                receptionDocflowAttachments = form.getDocflowEmailDocument().getAttachments();
            }
        }
        if (!CollectionUtils.isEmpty(receptionDocflowAttachments)) {
            List<CAttachment> atts = receptionDocflowAttachments.stream().filter(ReceptionDocflowAttachment::isTransferAttachment).map(r -> new CAttachment(r.getFileName(), abdocsService.downloadFile(r.getUuid().toString(), r.getFileName(), r.getDatabaseId()).getContent(), r.getDescription())).collect(Collectors.toList());
            res.setAttachments(atts);
        }
        if (form.getDocflowEmailDocument() != null) {
            String notes = Arrays.asList(res.getNotes(), "emailId:" + form.getDocflowEmailDocument().getEmailId())
                    .stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("\n"));
            res.setNotes(notes);
        }

        List<String> additionalUserdocs = form.getAdditionalUserdocs();
        if (!CollectionUtils.isEmpty(additionalUserdocs)) {
            res.setUserdocReceptions(new ArrayList<>());
            for (String userdocType : additionalUserdocs) {
                res.getUserdocReceptions().add(convertAdditionalUserdocToCReception(form, userdocType));
            }
        }

        return res;
    }

    private CReception convertAdditionalUserdocToCReception(ReceptionForm form, String userdocType) {
        CReceptionUserdoc cReceptionUserdoc = new CReceptionUserdoc();
        cReceptionUserdoc.setUserdocType(userdocType);

        CReception subReception = new CReception();
        subReception.setUserdoc(cReceptionUserdoc);
        subReception.setRegisterReceptionRequest(false);
        subReception.setRegisterInDocflowSystem(true);
        subReception.setEntryDate(DateUtils.convertToDate(DateUtils.convertToLocalDate(form.getEntryDate())));
        subReception.setOriginalExpected(form.getOriginalExpected());
        subReception.setOwnershipData(form.getOwnershipData());
        subReception.setRepresentationData(form.getRepresentationData());
        subReception.setSubmissionType(form.getSubmissionType());
        subReception.setNotes(form.getNotes());
        return subReception;
    }

    private void fillInitialReceptionOnExistingDocumentId(Integer existingDocumentId, ReceptionForm receptionForm) {
        Document abdocsDocument = abdocsService.selectDocumentById(existingDocumentId);
        if (Objects.isNull(abdocsDocument))
            throw new RuntimeException("Abdocs document with id=" + existingDocumentId + " not found!");

        receptionForm.setReceptionType(ReceptionType.ACP.code());
        receptionForm.setInitialDocument(new InitialDocumentForm());
        receptionForm.getInitialDocument().setDocumentId(existingDocumentId);



        if (!CollectionUtils.isEmpty(abdocsDocument.getDocFiles())){
            receptionForm.getInitialDocument().setAttachments(_createReceptionReceptionDocflowAttachments(abdocsDocument.getDocFiles()));
        }

        if (Objects.nonNull(abdocsDocument.getRegDate())) {
            receptionForm.setEntryDate(abdocsDocument.getRegDate());
        }
        if (Objects.nonNull(abdocsDocument.getDocSourceTypeId())) {
            receptionForm.setSubmissionType(abdocsDocument.getDocSourceTypeId());
        }
        if (!CollectionUtils.isEmpty(abdocsDocument.getDocCorrespondents())) {
            receptionForm.setOwnershipData(correspondentConverter.convertToCOwnershipData(abdocsDocument.getDocCorrespondents()));
            PersonUtils.fillOwnershipDataPersonIds(receptionForm.getOwnershipData(), personService, tempID, false);
        }
        StringBuilder notesBuilder = new StringBuilder();
        if (Objects.nonNull(abdocsDocument.getRegUri())) {
            receptionForm.getInitialDocument().setDescription("АНП от " + abdocsDocument.getRegUri());
            notesBuilder.append("Номер: " + abdocsDocument.getRegUri() + ". ");
            receptionForm.getInitialDocument().setRegNumber(abdocsDocument.getRegUri());
        }
        if (Objects.nonNull(abdocsDocument.getDocSubject())) {
            notesBuilder.append("Относно: " + abdocsDocument.getDocSubject() + ".");
            receptionForm.getInitialDocument().setSubject(abdocsDocument.getDocSubject());
        }
        receptionForm.setNotes(notesBuilder.toString());

    }

    private ReceptionForm createInitialReceptionFоrm(Integer docflowDocumentId, Integer emailId, Integer existingDocumentId) {
        ReceptionForm receptionForm = new ReceptionForm();
        receptionForm.setReceptionType(ReceptionType.MARK.code());
        receptionForm.setMark(new ReceptionMarkForm(false));
        receptionForm.setSubmissionType(SubmissionType.COUNTER.code());
        receptionForm.setOriginalExpected(false);
        receptionForm.setEntryDate(dailyLogService.getWorkingDate());
        if (Objects.nonNull(existingDocumentId)) {
            fillInitialReceptionOnExistingDocumentId(existingDocumentId, receptionForm);
        }
        if (docflowDocumentId != null) {
            Document abdocsDocument = abdocsService.selectDocumentById(docflowDocumentId);
            ReceptionDocflowDocumentForm docflowDocumentForm = new ReceptionDocflowDocumentForm(abdocsDocument.getDocId(), abdocsDocument.getDocSubject(), abdocsDocument.getRegUri(), CorrespondentUtils.getFirstEmailAddress(abdocsDocument.getDocCorrespondents()), _createReceptionReceptionDocflowAttachments(abdocsDocument.getDocFiles()));
            receptionForm.setDocflowDocument(docflowDocumentForm);
            receptionForm.setNotes(abdocsDocument.getRegUri());
            receptionForm.setSubmissionType(abdocsDocument.getDocSourceTypeId());
            receptionForm.setEntryDate(abdocsDocument.getRegDate());
        }
        if (emailId != null) {
            CreateDocEmailDto mail = abdocsService.readEmail(emailId);
            String emailAddress = Objects.nonNull(mail.getCorrespondentContact()) ? mail.getCorrespondentContact().getEmail() : null;

            Integer submissionType = mail.getDocSourceTypeId();
            if (Objects.nonNull(submissionType)) {
                receptionForm.setSubmissionType(SubmissionType.selectByCode(submissionType).code());
            } else {
                receptionForm.setSubmissionType(SubmissionType.EMAIL.code());
            }

            String notes = "Относно : " + mail.getDocSubject() + "\nИмейл: " + emailAddress;
            receptionForm.setNotes(notes);
            receptionForm.setDocflowEmailDocument(new ReceptionDocflowEmailDocumentForm(emailId, mail.getDocSubject(), _createReceptionReceptionDocflowAttachments(mail.getDocFiles()), emailAddress));
            if (Objects.nonNull(mail.getRegDate())) {
                receptionForm.setEntryDate(mail.getRegDate());
            }
        }
        receptionForm.setOriginalExpected(ReceptionUtils.getOriginalExpectedDefaultValue(receptionForm.getSubmissionType()));
        receptionForm.setAdditionalUserdocs(new ArrayList<>());
        return receptionForm;
    }

    private List<ReceptionDocflowAttachment> _createReceptionReceptionDocflowAttachments(List<DocFile> docFiles) {
        List<ReceptionDocflowAttachment> receptionFormAttachments = null;
        if (docFiles != null) {
            receptionFormAttachments = new ArrayList<>();
            int id = 1;
            for (DocFile df : docFiles) {
                receptionFormAttachments.add(new ReceptionDocflowAttachment(df.getKey(), df.getName(), df.getDescription(), df.getDbId(), true, id++));
            }
        }
        return receptionFormAttachments;
    }

    public static void fillEuPatentWarnings(ReceptionForm receptionForm, List<String> warnings, MessageSource messageSource,
                                            EbdPatentService ebdPatentService, FileService fileService, ProcessService processService, ConfigParamService configParamService) {
        ReceptionEuPatentForm euPatent = receptionForm.getEuPatent();
        if (!StringUtils.isEmpty(euPatent.getObjectNumber())) {
            CEbdPatent cEbdPatent = ebdPatentService.selectByFileNumber(euPatent.getObjectNumber());
            if (cEbdPatent.isWithdrawn())
                warnings.add(messageSource.getMessage("eupatent.withdrawn.warning", null, LocaleContextHolder.getLocale()));

            CFile euPatentIpasFile = EuPatentUtils.selectEuPatentIpasFile(fileService, cEbdPatent);
            if (Objects.nonNull(euPatentIpasFile)) {
                CProcess cProcess = processService.selectProcess(euPatentIpasFile.getProcessId(), true);
                boolean isEuPatentValidated = EuPatentUtils.isEuPatentValidated(cProcess, configParamService);
                if (!isEuPatentValidated && !euPatent.getType().equals(EuPatentReceptionType.VALIDATION_AFTER_MODIFICATION.code()))
                    warnings.add(messageSource.getMessage("eupatent.ipas.existing", null, LocaleContextHolder.getLocale()));
            }

        }
    }
}
