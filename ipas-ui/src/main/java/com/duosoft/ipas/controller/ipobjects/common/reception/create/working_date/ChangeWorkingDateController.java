package com.duosoft.ipas.controller.ipobjects.common.reception.create.working_date;

import bg.duosoft.ipas.core.service.dailylog.DailyLogService;
import bg.duosoft.ipas.core.service.dailylog.DailyLogServiceException;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.working_date.IpasWorkingDateValidator;
import bg.duosoft.ipas.services.core.IpasServiceException;
import bg.duosoft.ipas.util.date.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/reception/create/ipas-working-date")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).IpasWorkingDateChange.code())")
public class ChangeWorkingDateController {

    @Autowired
    private DailyLogService dailyLogService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private IpasValidatorCreator validatorCreator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        CustomDateEditor customDateEditor = new CustomDateEditor(new SimpleDateFormat(DateUtils.DATE_FORMAT_DOT), true, 10);
        binder.registerCustomEditor(Date.class, customDateEditor);
    }

    @PostMapping("/change")
    public String changeIpasWorkingDate(RedirectAttributes redirectAttributes, @RequestParam Date newWorkingDate) {
        try {
            dailyLogService.changeWorkingDate(newWorkingDate);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("change.working.date.successful", new String[]{DateUtils.formatDate(newWorkingDate)}, LocaleContextHolder.getLocale()));
        } catch (DailyLogServiceException e) {
            log.error(e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("change.working.date.error", null, LocaleContextHolder.getLocale()));
        }
        return "redirect:/reception/create";
    }

    @GetMapping("/performCloseDailyLogChecks")
    @ResponseBody
    public String performCloseDailyLogChecks() {
        String error = null;
        try {
            dailyLogService.performCloseDailyLogChecks();
        } catch (DailyLogServiceException e) {
            log.error(e.getMessage(), e);
            error = ExceptionUtils.getFullStackTrace(e);
        }
        return "done..." + (error == null ? "" : "Error:" + error);
    }

    @PostMapping("/validate")
    public String validateIpasWorkingDate(Model model, @RequestParam(required = false) Date newWorkingDate) {
        IpasValidator<Date> validator = validatorCreator.create(false, IpasWorkingDateValidator.class);
        List<ValidationError> errors = validator.validate(newWorkingDate);
        model.addAttribute("validationErrors", errors);
        model.addAttribute("workingDate", CollectionUtils.isEmpty(errors) ? newWorkingDate : dailyLogService.getWorkingDate());
        return "ipobjects/common/reception/workingdate/working_date_modal :: modal";
    }
}
