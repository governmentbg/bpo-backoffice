package com.duosoft.ipas.controller.ipobjects.common.process.action;

import bg.duosoft.ipas.core.model.action.CAction;
import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.action.CJournal;
import bg.duosoft.ipas.core.model.process.CProcessEvent;
import bg.duosoft.ipas.core.service.action.ActionService;
import bg.duosoft.ipas.core.service.action.JournalService;
import bg.duosoft.ipas.core.service.nomenclature.ProcessTypeService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.validation.action.ActionDateValidator;
import bg.duosoft.ipas.core.validation.action.ChangeActionJournalValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.json.JsonUtil;
import bg.duosoft.ipas.util.process.ProcessActionUtils;
import com.duosoft.ipas.enums.GeneralPanel;
import com.duosoft.ipas.util.ProcessUtils;
import com.duosoft.ipas.util.RedirectUtils;
import com.duosoft.ipas.util.json.EditActionData;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/process/edit-action")
public class ProcessEditActionController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private ProcessTypeService processTypeService;

    @Autowired
    private ActionDateValidator actionDateValidator;

    @Autowired
    private ChangeActionJournalValidator changeActionJournalValidator;

    @Autowired
    private ActionService actionService;

    @Autowired
    private JournalService journalService;

    @Autowired
    private MessageSource messageSource;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(DateUtils.DATE_FORMAT_DOT), true, 10));
    }

    @PostMapping("/open-modal")
    public String openEditActionModal(Model model, @RequestParam String actionIdString) {
        CActionId actionId = ProcessUtils.getActionIdFromString(actionIdString);
        CProcessEvent cProcessEvent = ProcessUtils.selectProcessEventByActionId(actionId, processService);
        model.addAttribute("cProcessEvent", cProcessEvent);
        model.addAttribute("openJournalsMap", journalService.selectOpenJournals());
        return "ipobjects/common/process/process_edit_modal :: edit-action";
    }

    @PostMapping("/validate-form")
    public String validateUpdateAction(Model model, @RequestParam String actionIdString, @RequestParam String data) {
        CActionId actionId = ProcessUtils.getActionIdFromString(actionIdString);
        EditActionData formData = JsonUtil.readJson(data, EditActionData.class);

        CAction cAction = actionService.selectAction(actionId);
        addFormDataToObject(formData, cAction);

        List<ValidationError> errors = new ArrayList<>();
        if (formData.isActionDateExist())
            addExistingErrorsToList(actionDateValidator, errors, cAction);
        if (formData.isJournalCodeExist())
            addExistingErrorsToList(changeActionJournalValidator, errors, cAction);

        CProcessEvent cProcessEvent = ProcessUtils.selectProcessEventByActionId(actionId, processService);
        model.addAttribute("cProcessEvent", cProcessEvent);
        model.addAttribute("validationErrors", errors);
        model.addAttribute("openJournalsMap", journalService.selectOpenJournals());
        return "ipobjects/common/process/process_edit_modal :: edit-action";

    }

    @PostMapping
    public String updateAction(HttpServletRequest request, RedirectAttributes redirectAttributes,
                               @RequestParam(required = false) String sessionIdentifier,
                               @RequestParam String actionIdString,
                               @RequestParam String data) {
        CActionId actionId = ProcessUtils.getActionIdFromString(actionIdString);
        String sessionObjectIdentifier = StringUtils.isEmpty(sessionIdentifier) ? null : HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);

        EditActionData formData = JsonUtil.readJson(data, EditActionData.class);
        CAction cAction = actionService.selectAction(actionId);
        addFormDataToObject(formData, cAction);

        String successMessage = messageSource.getMessage("action.update.success", new String[]{cAction.getActionType().getActionName()}, LocaleContextHolder.getLocale());
        return saveAndRedirect(redirectAttributes, sessionObjectIdentifier, cAction, successMessage);
    }

    private void addExistingErrorsToList(IpasValidator ipasValidator, List<ValidationError> errors, CAction cAction) {
        List<ValidationError> validationErrors = ipasValidator.validate(cAction);
        if (!CollectionUtils.isEmpty(validationErrors))
            errors.addAll(validationErrors);
    }

    private void addFormDataToObject(EditActionData formData, CAction cAction) {
        if (formData.isNotesExist())
            cAction.setNotes(formData.getNotes());
        if (formData.isNotes1Exist())
            cAction.setNotes1(formData.getNotes1());
        if (formData.isNotes2Exist())
            cAction.setNotes2(formData.getNotes2());
        if (formData.isNotes3Exist())
            cAction.setNotes3(formData.getNotes3());
        if (formData.isNotes4Exist())
            cAction.setNotes4(formData.getNotes4());
        if (formData.isNotes5Exist())
            cAction.setNotes5(formData.getNotes5());
        if (formData.isJournalCodeExist()) {
            cAction.setJournal(new CJournal());
            cAction.getJournal().setJournalCode(formData.getJournalCode());
        }
        if (formData.isActionDateExist()) {
            String originalDate = DateUtils.formatDate(cAction.getActionDate());
            String formDate = DateUtils.formatDate(formData.getActionDate());
            if (!originalDate.equals(formDate)){
                cAction.setActionDate(ProcessActionUtils.changeMidnightActionDate(formData.getActionDate(),cAction.getActionId().getProcessId(), actionService));
            }
        }
    }

    private String saveAndRedirect(RedirectAttributes redirectAttributes, String sessionObjectIdentifier, CAction cAction, String successMessage) {
        List<ValidationError> errors = updateAction(cAction);
        if (CollectionUtils.isEmpty(errors)) {
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            return redirectToViewPage(redirectAttributes, sessionObjectIdentifier, cAction, GeneralPanel.Process.code());
        } else {
            redirectAttributes.addFlashAttribute("validationErrors", errors);
            return redirectToViewPage(redirectAttributes, sessionObjectIdentifier, cAction, null);
        }
    }

    private String redirectToViewPage(RedirectAttributes redirectAttributes, String sessionObjectIdentifier, CAction cAction, String code) {
        boolean isManualSubProcess = processTypeService.isProcessTypeForManualSubProcess(cAction.getActionId().getProcessId().getProcessType());
        if (isManualSubProcess && StringUtils.isEmpty(sessionObjectIdentifier)) {
            return RedirectUtils.redirectToManualSubProcessViewPage(cAction.getActionId().getProcessId());
        } else {
            return RedirectUtils.redirectToOpenExistingObject(redirectAttributes, sessionObjectIdentifier, false, code);
        }
    }

    private List<ValidationError> updateAction(CAction cAction) {
        try {
            actionService.updateAction(cAction);
        } catch (IpasValidationException e) {
            log.error(e.getMessage(), e);
            return e.getErrors();
        }
        return null;
    }

}
