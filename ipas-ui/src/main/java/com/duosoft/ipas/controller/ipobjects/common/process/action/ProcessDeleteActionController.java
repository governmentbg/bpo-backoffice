package com.duosoft.ipas.controller.ipobjects.common.process.action;

import bg.duosoft.abdocs.model.DocStatus;
import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.action.CAction;
import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.file.CFileRecordal;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.process.CActionProcessEvent;
import bg.duosoft.ipas.core.model.process.CProcessEvent;
import bg.duosoft.ipas.core.service.action.ActionService;
import bg.duosoft.ipas.core.service.file.FileRecordalService;
import bg.duosoft.ipas.core.service.nomenclature.ProcessTypeService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.util.security.SecurityUtils;
import com.duosoft.ipas.enums.GeneralPanel;
import com.duosoft.ipas.util.ProcessUtils;
import com.duosoft.ipas.util.RedirectUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/process/delete-action")
public class ProcessDeleteActionController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private ProcessTypeService processTypeService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ActionService actionService;

    @Autowired
    private FileRecordalService fileRecordalService;

    @Autowired
    private AbdocsService abdocsServiceAdmin;

    @PostMapping("/open-modal")
    public String openDeleteActionModal(Model model, @RequestParam String actionIdString) {
        CActionId actionId = ProcessUtils.getActionIdFromString(actionIdString);
        CProcessEvent cProcessEvent = ProcessUtils.selectProcessEventByActionId(actionId, processService);
        model.addAttribute("cProcessEvent", cProcessEvent);

        boolean isRecordalExists = addRecordalToModel(model, actionId, cProcessEvent);
        if (isRecordalExists) {
            CFileRecordal recordal = (CFileRecordal) model.asMap().get("recordal");
            if (Objects.nonNull(recordal.getInvalidationDate())) {
                return "ipobjects/common/process/process_delete_modal :: recordal-invalidation";
            }
        }

        CActionProcessEvent eventAction = cProcessEvent.getEventAction();
        if (Objects.nonNull(eventAction)) {
            COffidoc generatedOffidoc = eventAction.getGeneratedOffidoc();
            if (Objects.nonNull(generatedOffidoc) && generatedOffidoc.getHasChildren()) {
                model.addAttribute("message", messageSource.getMessage("delete.action.upper.offidoc.process.error", null, LocaleContextHolder.getLocale()));
                return "ipobjects/common/process/process_delete_modal :: delete-action-warning-modal";
            }
            if (Objects.nonNull(generatedOffidoc)) {
                Integer abdocsDocumentId = generatedOffidoc.getAbdocsDocumentId();
                if (Objects.nonNull(abdocsDocumentId)) {
                    Document document = abdocsServiceAdmin.selectDocumentById(abdocsDocumentId);
                    if (Objects.nonNull(document)) {
                        DocStatus docStatus = document.getDocStatus();
                        if (Objects.nonNull(docStatus) && docStatus != DocStatus.Draft) {
                            model.addAttribute("message", messageSource.getMessage("delete.action.abdocs.status.error", null, LocaleContextHolder.getLocale()));
                            return "ipobjects/common/process/process_delete_modal :: delete-action-warning-modal";
                        }
                    }
                }
            }
        }

        return "ipobjects/common/process/process_delete_modal :: delete-action";
    }

    @PostMapping("/validate-form")
    public String validateDeleteAction(Model model, @RequestParam String actionIdString, @RequestParam(required = false) String deleteReason) {
        CActionId actionId = ProcessUtils.getActionIdFromString(actionIdString);

        List<ValidationError> errors = new ArrayList<>();
        if (StringUtils.isEmpty(deleteReason))
            errors.add(ValidationError.builder().pointer("action.deleteReason").messageCode("required.field").build());

        CProcessEvent cProcessEvent = ProcessUtils.selectProcessEventByActionId(actionId, processService);
        model.addAttribute("cProcessEvent", cProcessEvent);
        model.addAttribute("validationErrors", errors);
        addRecordalToModel(model, actionId, cProcessEvent);
        return "ipobjects/common/process/process_delete_modal :: delete-action";
    }

    private boolean addRecordalToModel(Model model, CActionId actionId, CProcessEvent cProcessEvent) {
        if (Objects.nonNull(cProcessEvent)) {
            CFileRecordal recordal = fileRecordalService.selectRecordalByActionId(actionId);
            if (Objects.nonNull(recordal)) {
                model.addAttribute("recordal", recordal);
                return true;
            }
        }
        return false;
    }

    @PostMapping
    public String deleteAction(HttpServletRequest request, RedirectAttributes redirectAttributes,
                               @RequestParam(required = false) String sessionIdentifier,
                               @RequestParam String actionIdString,
                               @RequestParam(required = false) String deleteReason) {
        String sessionObjectIdentifier = StringUtils.isEmpty(sessionIdentifier) ? null : HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
        CActionId actionId = ProcessUtils.getActionIdFromString(actionIdString);
        CAction cAction = actionService.selectAction(actionId);
        if (Objects.isNull(cAction))
            throw new RuntimeException("Action not found !");

        boolean isDeleted = actionService.deleteAction(actionId, SecurityUtils.getLoggedUserId(), deleteReason);
        if (isDeleted) {
            String successMessage = messageSource.getMessage("action.delete.success", new String[]{cAction.getActionType().getActionName()}, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            return redirectToViewPage(redirectAttributes, sessionObjectIdentifier, cAction, GeneralPanel.Process.code());
        } else {
            redirectAttributes.addFlashAttribute("validationErrors", Collections.singletonList(ValidationError.builder().pointer("deleteAction").messageCode("action.delete.error").build()));
            return redirectToViewPage(redirectAttributes, sessionObjectIdentifier, cAction, null);
        }

    }

    private String redirectToViewPage(RedirectAttributes redirectAttributes, String sessionObjectIdentifier, CAction cAction, String scrollToPanel) {
        boolean isManualSubProcess = processTypeService.isProcessTypeForManualSubProcess(cAction.getActionId().getProcessId().getProcessType());
        if (isManualSubProcess && StringUtils.isEmpty(sessionObjectIdentifier)) {
            return RedirectUtils.redirectToManualSubProcessViewPage(cAction.getActionId().getProcessId());
        } else {
            return RedirectUtils.redirectToOpenExistingObject(redirectAttributes, sessionObjectIdentifier, false, scrollToPanel);
        }
    }

}
