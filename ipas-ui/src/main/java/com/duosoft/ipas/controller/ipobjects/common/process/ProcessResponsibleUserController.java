package com.duosoft.ipas.controller.ipobjects.common.process;

import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.process.ProcessResponsibleUserValidator;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.process.ProcessTypeUtils;
import com.duosoft.ipas.enums.GeneralPanel;
import com.duosoft.ipas.util.ProcessUtils;
import com.duosoft.ipas.util.RedirectUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/process/responsible-user")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ProcessChangeResponsibleUser.code())")
public class ProcessResponsibleUserController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProcessService processService;

    @Autowired
    private ProcessResponsibleUserValidator processResponsibleUserValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(DateUtils.DATE_FORMAT_DOT), true, 10));
    }

    @PostMapping("/validate")
    public String validateResponsibleUser(Model model, @RequestParam String process, @RequestParam(required = false) Integer userId, @RequestParam(required = false) String userName) {
        CProcess cProcess = ProcessUtils.selectProcess(process, processService, true);
        List<ValidationError> errors = processResponsibleUserValidator.validate(cProcess, Objects.isNull(userId) ? null : new CUser(userName, userId, null));
        model.addAttribute("validationErrors", errors);
        model.addAttribute("process", cProcess);
        return "ipobjects/common/process/process_panel :: responsible-user";
    }

    @PostMapping("/change")
    public String changeResponsibleUser(HttpServletRequest request,
                                        RedirectAttributes redirectAttributes,
                                        @RequestParam String process,
                                        @RequestParam Integer userId,
                                        @RequestParam String userName,
                                        @RequestParam(required = false) String sessionIdentifier) {
        CProcess cProcess = ProcessUtils.selectProcess(process, processService, false);
        boolean isManualSubProcess = ProcessTypeUtils.isManualSubProcess(cProcess);
        if (isManualSubProcess) {
            changeResponsibleUser(redirectAttributes, userId, userName, cProcess);
            return RedirectUtils.redirectToManualSubProcessViewPage(cProcess.getProcessId());
        } else {
            String sessionObjectIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
            changeResponsibleUser(redirectAttributes, userId, userName, cProcess);
            return RedirectUtils.redirectToOpenExistingObject(redirectAttributes, sessionObjectIdentifier, false, GeneralPanel.Process.code());
        }
    }

    private void changeResponsibleUser(RedirectAttributes redirectAttributes, @RequestParam Integer userId, @RequestParam String userName, CProcess cProcess) {
        if (Objects.isNull(userId)) {
            cProcess.setResponsibleUser(null);
        } else {
            CUser user = new CUser();
            user.setUserId(userId);
            cProcess.setResponsibleUser(user);
        }

        List<ValidationError> errors = updateProcess(cProcess);
        if (!CollectionUtils.isEmpty(errors)) {
            redirectAttributes.addFlashAttribute("validationErrors", errors);
        } else {
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("process.responsible.user.success", new String[]{userName}, LocaleContextHolder.getLocale()));
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
