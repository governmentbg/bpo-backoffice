package com.duosoft.ipas.controller.ipobjects.common.process;

import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.service.nomenclature.ProcessTypeService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.process.ProcessManualDueDateValidator;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.process.ProcessTypeUtils;
import com.duosoft.ipas.config.exception.ForbiddenException;
import com.duosoft.ipas.enums.GeneralPanel;
import com.duosoft.ipas.util.ProcessUtils;
import com.duosoft.ipas.util.RedirectUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/process/expiration-date")
public class ProcessExpirationDateController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProcessService processService;

    @Autowired
    private ProcessTypeService processTypeService;

    @Autowired
    private ProcessManualDueDateValidator processManualDueDateValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(DateUtils.DATE_FORMAT_DOT), true, 10));
    }

    @PostMapping("/validate")
    public String validateManualDueDate(Model model, @RequestParam String process, @RequestParam(required = false) Date manualDueDate) {
        CProcess cProcess = ProcessUtils.selectProcess(process, processService, false);
        cProcess.setDueDate(manualDueDate);

        List<ValidationError> errors = processManualDueDateValidator.validate(cProcess);
        model.addAttribute("validationErrors", errors);
        model.addAttribute("process", cProcess);
        return "ipobjects/common/process/process_panel :: manual-due-date";
    }

    @PostMapping("/change")
    public String changeManualDueDate(HttpServletRequest request,
                                      RedirectAttributes redirectAttributes,
                                      @RequestParam String process,
                                      @RequestParam Date manualDueDate,
                                      @RequestParam(required = false) String sessionIdentifier) {
        CProcess cProcess = ProcessUtils.selectProcess(process, processService, false);
        if (!ProcessUtils.hasPermissionsForChangeManualDueDate(cProcess)) {
            throw new ForbiddenException("Permissions denied !");
        }
        boolean isManualSubProcess = ProcessTypeUtils.isManualSubProcess(cProcess);
        if (isManualSubProcess) {
            changeExpirationDate(redirectAttributes, manualDueDate, cProcess);
            return RedirectUtils.redirectToManualSubProcessViewPage(cProcess.getProcessId());
        } else {
            String sessionObjectIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
            changeExpirationDate(redirectAttributes, manualDueDate, cProcess);
            return RedirectUtils.redirectToOpenExistingObject(redirectAttributes, sessionObjectIdentifier, false, GeneralPanel.Process.code());
        }
    }

    private void changeExpirationDate(RedirectAttributes redirectAttributes, @RequestParam Date manualDueDate, CProcess cProcess) {
        cProcess.setDueDate(manualDueDate);
        List<ValidationError> errors = updateProcess(cProcess);
        if (!CollectionUtils.isEmpty(errors)) {
            redirectAttributes.addFlashAttribute("validationErrors", errors);
        } else {
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("process.manual.due.date.success", new String[]{DateUtils.formatDate(manualDueDate)}, LocaleContextHolder.getLocale()));
        }
    }

    private List<ValidationError> updateProcess(CProcess cProcess) {
        try {
            processService.updateProcess(cProcess);
        } catch (IpasValidationException e) {
            return e.getErrors();
        }
        return null;
    }

}
