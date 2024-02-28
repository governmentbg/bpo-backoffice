package com.duosoft.ipas.controller.ipobjects.common.process.action;

import bg.duosoft.ipas.core.model.action.CActionType;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.document.CExtraData;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.offidoc.COffidocType;
import bg.duosoft.ipas.core.model.offidoc.COffidocTypeTemplate;
import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.process.CNextProcessAction;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.process.CProcessFreezing;
import bg.duosoft.ipas.core.model.process.CProcessInsertActionRequest;
import bg.duosoft.ipas.core.model.reception.CProcessType;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocExtraData;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPanel;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.UserdocExtraDataService;
import bg.duosoft.ipas.core.service.action.InsertActionService;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.nomenclature.*;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.userdoc.UserdocPersonService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.process.ProcessInsertActionValidator;
import bg.duosoft.ipas.enums.*;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.json.JsonUtil;
import bg.duosoft.ipas.util.process.ProcessActionUtils;
import bg.duosoft.ipas.util.process.ProcessTypeUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import bg.duosoft.ipas.util.userdoc.UserdocExtraDataUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import com.duosoft.ipas.enums.GeneralPanel;
import com.duosoft.ipas.util.ProcessUtils;
import com.duosoft.ipas.util.RedirectUtils;
import com.duosoft.ipas.util.json.ProcessActionFormData;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/process/insert-action")
public class ProcessInsertActionController {
    private static final String NEXT_PROCESS_SELECT_NOTE = "note";
    private static final String NEXT_PROCESS_SELECT_NORMAL = "normal";
    private static final String NEXT_PROCESS_SELECT_MANUAL_SUB_PROCESS = "msprocess";

    @Autowired
    private ProcessService processService;

    @Autowired
    private ActionTypeService actionTypeService;

    @Autowired
    private ProcessInsertActionValidator processActionValidator;

    @Autowired
    private StatusService statusService;

    @Autowired
    private UserdocService userdocService;

    @Autowired
    private UserdocExtraDataService userdocExtraDataService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private OffidocTypeService offidocTypeService;

    @Autowired
    private InsertActionService insertActionService;

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Autowired
    private DocService docService;

    @Autowired
    private ProcessTypeService processTypeService;

    @Autowired
    private UserdocPersonService userdocPersonService;

    @PostMapping
    public String insertNewAction(HttpServletRequest request,
                                  RedirectAttributes redirectAttributes,
                                  @RequestParam String process,
                                  @RequestParam String actionTyp,
                                  @RequestParam(required = false) String data,
                                  @RequestParam(required = false) String sessionIdentifier
    ) {
        CActionType cActionType = actionTypeService.findById(actionTyp);

        ProcessActionFormData formData = new ProcessActionFormData();
        if (!StringUtils.isEmpty(data)) {
            formData = JsonUtil.readJson(data, ProcessActionFormData.class);
        }

        CProcess cProcess = ProcessUtils.selectProcess(process, processService, false);
        CNextProcessAction nextProcessAction = processService.selectNextProcessActionBySelectedActionType(cProcess, actionTyp);
        if (NextProcessActionType.SPECIAL_RANDOM_TO_SPECIFIC == nextProcessAction.getProcessActionType()) {
            formData.setSpecialFinalStatus(nextProcessAction.getStatusCode());
        }
        if (Objects.isNull(formData.getActionDate())) {
            formData.setActionDate(new Date());
        }

        String scrollToPanel = null;
        try {
            CProcessInsertActionRequest insertActionRequest = createInsertProcessActionObject(actionTyp, cProcess.getProcessId(), cProcess.getResponsibleUser(), formData);
            insertActionService.insertAction(insertActionRequest);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("process.action.insert.success", new String[]{cActionType.getActionName()}, LocaleContextHolder.getLocale()));
            scrollToPanel = GeneralPanel.Process.code();
        } catch (IpasValidationException e) {
            log.error(e.getMessage(), e);
            redirectAttributes.addFlashAttribute("validationErrors", e.getErrors());
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("process.action.insert.error.validationErrors", new String[]{cActionType.getActionName()}, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("process.action.insert.error", new String[]{cActionType.getActionName()}, LocaleContextHolder.getLocale()));
        }

        boolean isManualSubProcess = ProcessTypeUtils.isManualSubProcess(cProcess);
        if (isManualSubProcess) {
            return RedirectUtils.redirectToManualSubProcessViewPage(cProcess.getProcessId());
        } else {
            String sessionObjectIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
            return RedirectUtils.redirectToOpenExistingObject(redirectAttributes, sessionObjectIdentifier, false, scrollToPanel);
        }
    }

    @PostMapping("/update-next-action-select")
    public String updateNextActionSelect(Model model, @RequestParam String type, @RequestParam String process) {
        CProcess cProcess = ProcessUtils.selectProcess(process, processService, false);
        model.addAttribute("process", cProcess);

        String returnPage = null;
        switch (type) {
            case NEXT_PROCESS_SELECT_MANUAL_SUB_PROCESS:
                if (ProcessTypeUtils.isIpObjectProcess(cProcess) || ProcessTypeUtils.isUserdocProcess(cProcess)) {
                    List<CProcessType> msprocessTypes = processTypeService.selectByRelatedToWcode(ProcessTypeUtils.MANUAL_SUB_PROCESS_TYPE_WCODE);
                    model.addAttribute("msprocessTypes", msprocessTypes);
                }
                returnPage = "ipobjects/common/process/process_next_action :: msprocess-select";
                break;
            case NEXT_PROCESS_SELECT_NORMAL:
            case NEXT_PROCESS_SELECT_NOTE:
                List<CNextProcessAction> nextProcessActions = processService.selectNextProcessActions(cProcess, SecurityUtils.hasRights(SecurityRole.ProcessAutomaticActionExecute));
                model.addAttribute("nextProcessActions", nextProcessActions);

                if (NEXT_PROCESS_SELECT_NORMAL.equals(type)) {
                    returnPage = "ipobjects/common/process/process_next_action :: next-normal-special-select";
                } else if (NEXT_PROCESS_SELECT_NOTE.equals(type)) {
                    returnPage = "ipobjects/common/process/process_next_action :: next-notification-select";
                }
                break;
            default:
                throw new RuntimeException("Invalid next process select type " + type);
        }
        return returnPage;
    }

    @PostMapping("/open-modal-check")
    @ResponseBody
    public boolean shouldOpenModalCheck(@RequestParam String process, @RequestParam String processActionType, @RequestParam String actionTyp) {
        CProcess cProcess = ProcessUtils.selectProcess(process, processService, false);
        CNextProcessAction nextProcessAction = processService.selectNextProcessActionBySelectedActionType(cProcess, actionTyp);

        COffidocType offidocTypeObject = selectGeneratedOffidocTypeObject(nextProcessAction);
        if (Objects.nonNull(offidocTypeObject)) {
            List<COffidocTypeTemplate> offidocTemplates = offidocTypeObject.getTemplates();
            if (!CollectionUtils.isEmpty(offidocTemplates) && offidocTemplates.size() > 1)
                return true;
        }

        List<CProcessFreezing> processFreezingList = cProcess.getProcessFreezingList();
        if (!CollectionUtils.isEmpty(processFreezingList)) {
            return true;
        }

        NextProcessActionType nextProcessActionType = NextProcessActionType.valueOf(processActionType);
        switch (nextProcessActionType) {
            case NORMAL:
            case SPECIAL_RANDOM_TO_SPECIFIC:
            case NOTE: {
                boolean calculateTermFromActionDate = nextProcessAction.getCalculateTermFromActionDate();
                if (calculateTermFromActionDate)
                    return true;

                boolean isContainNotesOrManualDueDate = nextProcessAction.getContainNotes() || nextProcessAction.getContainManualDueDate();
                if (isContainNotesOrManualDueDate)
                    return true;

                if (UserdocUtils.isStatusTriggerActivityForRecordalRegistration(nextProcessAction.getStatusCode(), cProcess, statusService, userdocTypesService))
                    return true;

                if (ProcessActionUtils.doesActionCauseIpObjectSplitting(nextProcessAction, cProcess, processService, userdocService)) {
                    return true;
                }

                if (UserdocUtils.isStatusTriggerActivityForRecordalInvalidation(nextProcessAction.getStatusCode(), cProcess, statusService, userdocTypesService)) {
                    if (UserdocUtils.isInvalidationUserdocRelatedToCurrectUserdoc(cProcess, processService, userdocTypesService)) {
                        return true;
                    }
                }
                break;
            }
            case SPECIAL_RANDOM_TO_RANDOM:
            case SPECIAL_SPECIFIC_TO_RANDOM:
                return true;
        }
        return false;
    }

    @PostMapping("/open-modal")
    public String openNewActionModal(HttpServletRequest request, Model model, @RequestParam String process, @RequestParam String actionTyp, @RequestParam(required = false) String sessionIdentifier) {
        CProcess cProcess = ProcessUtils.selectProcess(process, processService, false);
        CNextProcessAction nextProcessAction = processService.selectNextProcessActionBySelectedActionType(cProcess, actionTyp);
        COffidocType offidocTypeObject = selectGeneratedOffidocTypeObject(nextProcessAction);
        fillStatusListForRandomActions(model, cProcess, nextProcessAction);
        fillModelAttributesForRecordalUserdoc(model, cProcess, nextProcessAction);
        fillModelAttributesForObjectSplitting(model, nextProcessAction, cProcess);

        model.addAttribute("formData", StringUtils.isEmpty(sessionIdentifier) ? null : fillDefaultValuesData(request, model, sessionIdentifier));
        model.addAttribute("actionType", actionTypeService.findById(actionTyp));
        model.addAttribute("process", process);
        model.addAttribute("nextProcessAction", nextProcessAction);
        model.addAttribute("offidocTypeObject", offidocTypeObject);
        List<CProcessFreezing> processFreezingList = cProcess.getProcessFreezingList();
        if (!CollectionUtils.isEmpty(processFreezingList)) {
            model.addAttribute("processFreezingList", processFreezingList);
        }
        return "ipobjects/common/process/process_next_action_form_modal :: process-action-form";
    }

    private ProcessActionFormData fillDefaultValuesData(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier) {
        ProcessActionFormData defaultValuesData = new ProcessActionFormData();
        boolean isRecordalUserdoc = model.containsAttribute("isRecordalUserdoc");
        boolean isRecordalInvalidationUserdoc = model.containsAttribute("isRecordalInvalidationUserdoc");
        if (isRecordalUserdoc || isRecordalInvalidationUserdoc) {
            CUserdoc userdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
            if (Objects.nonNull(userdoc)) {
                if (isRecordalUserdoc) {
                    defaultValuesData.setRecordalDate(UserdocExtraDataUtils.selectDate(UserdocExtraDataTypeCode.EFFECTIVE_DATE.name(), userdoc.getUserdocExtraData()));
                }
                if (isRecordalInvalidationUserdoc) {
                    CDocumentId recordalUserdocId = UserdocUtils.selectRecordalUserdocIdByInvalidationUserdoc(userdoc);
                    CUserdocExtraData recordalInvalidationDate = userdocExtraDataService.selectById(recordalUserdocId, UserdocExtraDataTypeCode.INVALIDATION_DATE.name());
                    if (Objects.nonNull(recordalInvalidationDate)) {
                        defaultValuesData.setInvalidationDate(recordalInvalidationDate.getDateValue());
                    }
                }
            }
        }
        return defaultValuesData;
    }

    private void fillModelAttributesForRecordalUserdoc(Model model, CProcess cProcess, CNextProcessAction nextProcessAction) {
        boolean isUserdoc = ProcessUtils.isUserdocProcess(cProcess);
        if (isUserdoc) {
            boolean isStatusTriggerActivity = UserdocUtils.isStatusTriggerActivity(nextProcessAction.getStatusCode(), cProcess.getProcessId().getProcessType(), statusService);
            if (isStatusTriggerActivity) {
                CUserdocType cUserdocType = UserdocUtils.selectUserdocTypeFromDatabase(cProcess, userdocTypesService);
                CUserdocPanel recordal = UserdocUtils.selectRecordalPanel(cUserdocType.getPanels());
                if (Objects.nonNull(recordal)) {
                    model.addAttribute("isRecordalUserdoc", true);
                    addSpecificRecordalTypeAttributes(recordal, cProcess, model);
                } else {
                    boolean isRecordalInvalidation = UserdocUtils.isRecordalInvalidation(cUserdocType.getInvalidatedUserdocTypes());
                    if (isRecordalInvalidation) {
                        model.addAttribute("isRecordalInvalidationUserdoc", true);
                    }
                }

            }
        }
    }

    private void addSpecificRecordalTypeAttributes(CUserdocPanel recordal, CProcess cProcess, Model model) {
        addRecordalInformationMessage(recordal, cProcess, model);

        RecordalType recordalType = RecordalType.valueOf(recordal.getPanel());
        switch (recordalType) {
            case Change_representative:
                model.addAttribute("showTransferCARadioButtons", true);
                break;
        }
    }

    private void fillModelAttributesForObjectSplitting(Model model, CNextProcessAction nextProcessAction, CProcess process) {
        if (ProcessActionUtils.doesActionCauseIpObjectSplitting(nextProcessAction, process, processService, userdocService)) {
            model.addAttribute("actionCauseIpObjectSplitting", true);
        }
    }

    private void addRecordalInformationMessage(CUserdocPanel recordal, CProcess process, Model model) {
        String message;
        RecordalType recordalType = RecordalType.valueOf(recordal.getPanel());
        switch (recordalType) {
            case Transfer:
                message = messageSource.getMessage("recordal.transfer.insert.action.info.msg", null, LocaleContextHolder.getLocale());
                break;
            case Change_representative: {
                message = messageSource.getMessage("recordal.change.repr.insert.action.info.msg", null, LocaleContextHolder.getLocale());
                int newRepresentativesCount = userdocPersonService.countByRole(process.getProcessOriginData().getDocumentId(), UserdocPersonRole.NEW_REPRESENTATIVE);
                if (newRepresentativesCount == 0) {
                    message += messageSource.getMessage("insertAction.newRepresentatives.empty", null, LocaleContextHolder.getLocale());
                }
                break;
            }
            case Change_correspondence_address:
                message = messageSource.getMessage("recordal.change.ca.insert.action.info.msg", null, LocaleContextHolder.getLocale());
                break;
            default:
                message = messageSource.getMessage("recordal.authorize.info.msg", null, LocaleContextHolder.getLocale());
                break;
        }
        model.addAttribute("recordalInfoMessage", message);
    }

    private COffidocType selectGeneratedOffidocTypeObject(CNextProcessAction nextProcessAction) {
        String generatedOffidoc = nextProcessAction.getGeneratedOffidoc();
        if (!StringUtils.isEmpty(generatedOffidoc)) {
            COffidocType cOffidocType = offidocTypeService.selectById(generatedOffidoc);
            if (Objects.isNull(cOffidocType))
                throw new RuntimeException("Cannot find offidoc " + generatedOffidoc + " in table CF_OFFIDOC_TYPE");
            if (CollectionUtils.isEmpty(cOffidocType.getTemplates()))
                throw new RuntimeException("Template list is empty for office document: " + generatedOffidoc);
            return cOffidocType;
        }
        return null;
    }

    @PostMapping("/validate-form")
    public String validateProcessAction(Model model, @RequestParam String process, @RequestParam String actionTyp, @RequestParam String data) {
        CProcess cProcess = ProcessUtils.selectProcess(process, processService, false);
        CNextProcessAction nextProcessAction = processService.selectNextProcessActionBySelectedActionType(cProcess, actionTyp);
        ProcessActionFormData formData = JsonUtil.readJson(data, ProcessActionFormData.class);
        CProcessInsertActionRequest insertActionRequest = createInsertProcessActionObject(actionTyp, cProcess.getProcessId(), cProcess.getResponsibleUser(), formData);
        CActionType cActionType = actionTypeService.findById(actionTyp);
        model.addAttribute("actionType", cActionType);
        model.addAttribute("process", process);
        model.addAttribute("nextProcessAction", nextProcessAction);
        model.addAttribute("offidocTypeObject", selectGeneratedOffidocTypeObject(nextProcessAction));
        model.addAttribute("formData", formData);
        fillValidationErrorsModelAttribute(model, cProcess, nextProcessAction, formData, insertActionRequest, cActionType);
        fillStatusListForRandomActions(model, cProcess, nextProcessAction);
        fillModelAttributesForRecordalUserdoc(model, cProcess, nextProcessAction);
        fillModelAttributesForObjectSplitting(model, nextProcessAction, cProcess);
        return "ipobjects/common/process/process_next_action_form_modal :: process-action-form";
    }

    private void fillValidationErrorsModelAttribute(Model model, CProcess cProcess, CNextProcessAction nextProcessAction, ProcessActionFormData formData, CProcessInsertActionRequest insertActionRequest, CActionType cActionType) {
        List<ValidationError> validationErrors = new ArrayList<>();

        List<ValidationError> processActionValidatorErrors = processActionValidator.validate(insertActionRequest, cActionType, nextProcessAction, cProcess);
        if (!CollectionUtils.isEmpty(processActionValidatorErrors)) {
            validationErrors.addAll(processActionValidatorErrors);
        }

        if (ProcessActionUtils.doesActionCauseIpObjectSplitting(nextProcessAction, cProcess, processService, userdocService)) {
            String executionConfirmationText = formData.getExecutionConfirmationText();
            if (StringUtils.isEmpty(executionConfirmationText)) {
                validationErrors.add(ValidationError.builder().pointer("action.executionConfirmationText").messageCode("required.field").build());
            } else if (!DefaultValue.CONFIRMATION_TEXT.equalsIgnoreCase(executionConfirmationText)) {
                validationErrors.add(ValidationError.builder().pointer("action.executionConfirmationText").messageCode("wrong.confirmation.text").build());
            }
        }

        model.addAttribute("validationErrors", validationErrors);
    }

    private CProcessInsertActionRequest createInsertProcessActionObject(String actionTyp, CProcessId processId, CUser responsibleUser, ProcessActionFormData formData) {
        CProcessInsertActionRequest insertActionRequest = new CProcessInsertActionRequest();
        insertActionRequest.setProcessId(processId);
        insertActionRequest.setActionType(actionTyp);
        insertActionRequest.setActionDate(formData.getActionDate());
        insertActionRequest.setNotes1(formData.getNotes1());
        insertActionRequest.setNotes2(formData.getNotes2());
        insertActionRequest.setNotes3(formData.getNotes3());
        insertActionRequest.setNotes4(formData.getNotes4());
        insertActionRequest.setNotes5(formData.getNotes5());
        insertActionRequest.setNotes(null);
        insertActionRequest.setResponsibleUser(Objects.isNull(responsibleUser) ? null : responsibleUser.getUserId());//TODO Ask Svetlio
        insertActionRequest.setSpecialFinalStatus(formData.getSpecialFinalStatus());
        insertActionRequest.setManualDueDate(formData.getManualDueDate());
        insertActionRequest.setCaptureUser(SecurityUtils.getLoggedUserId());
        insertActionRequest.setCertificateReference(null);
        insertActionRequest.setOffidocTemplates(formData.getOffidocTemplates());
        insertActionRequest.setRecordalDate(formData.getRecordalDate());
        insertActionRequest.setInvalidationDate(formData.getInvalidationDate());
        insertActionRequest.setTransferCorrespondenceAddress(formData.getTransferCorrespondenceAddress());
        return insertActionRequest;
    }

    private List<CStatus> fillStatusListForRandomActions(Model model, CProcess cProcess, CNextProcessAction nextProcessAction) {
        List<CStatus> cStatuses = null;
        NextProcessActionType processActionType = nextProcessAction.getProcessActionType();
        if (processActionType == NextProcessActionType.SPECIAL_RANDOM_TO_RANDOM || processActionType == NextProcessActionType.SPECIAL_SPECIFIC_TO_RANDOM) {
            cStatuses = statusService.selectStatusesByProcessType(cProcess.getProcessId().getProcessType());
            model.addAttribute("statusList", cStatuses);
        }
        return cStatuses;
    }

}
