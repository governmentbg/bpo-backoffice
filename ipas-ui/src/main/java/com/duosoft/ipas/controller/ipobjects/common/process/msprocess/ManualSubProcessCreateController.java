package com.duosoft.ipas.controller.ipobjects.common.process.msprocess;

import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.service.process.ProcessService;
import com.duosoft.ipas.enums.GeneralPanel;
import com.duosoft.ipas.util.ProcessUtils;
import com.duosoft.ipas.util.RedirectUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/msprocess/create")
public class ManualSubProcessCreateController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private MessageSource messageSource;

    @PostMapping
    public String createManualSubProcess(HttpServletRequest request, RedirectAttributes redirectAttributes, @RequestParam String sessionIdentifier, @RequestParam String processIdString, @RequestParam String msProcessType) {
        String sessionObjectIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
        CProcessId processId = ProcessUtils.getProcessIdFromString(processIdString);
        CProcess process = processService.selectProcess(processId, false);
        if (Objects.isNull(process)) {
            throw new RuntimeException("Cannot find process with id = " + processId);
        }
        try {
            processService.createManualSubProcess(process, msProcessType);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("msprocess.create.success", null, LocaleContextHolder.getLocale()));
            return RedirectUtils.redirectToOpenExistingObject(redirectAttributes, sessionObjectIdentifier, false, GeneralPanel.Process.code());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("msprocess.create.error", null, LocaleContextHolder.getLocale()));
            return RedirectUtils.redirectToOpenExistingObject(redirectAttributes, sessionObjectIdentifier, false, null);
        }
    }

}
