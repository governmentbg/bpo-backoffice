package com.duosoft.ipas.controller.ipobjects.common.process.msprocess;

import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.process.CProcessEvent;
import bg.duosoft.ipas.core.service.action.ActionService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.util.process.ProcessTypeUtils;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/msprocess/delete")
public class ManualSubProcessDeleteController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private ActionService actionService;

    @Autowired
    private MessageSource messageSource;

    @PostMapping
    public String deleteManualSubProcess(HttpServletRequest request, RedirectAttributes redirectAttributes,
                                         @RequestParam String sessionIdentifier, @RequestParam String processIdString) {
        String sessionObjectIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
        CProcessId processId = ProcessUtils.getProcessIdFromString(processIdString);
        CProcess process = processService.selectProcess(processId, false);
        validateProcess(processIdString, process);

        boolean hasActions = actionService.isActionExistsForProcessId(processId);
        if (hasActions) {
            throw new RuntimeException("Cannot delete manual sub process, because there are actions inserted actions !");
        }

        try {
            processService.deleteProcess(processId);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("msprocess.delete.success", null, LocaleContextHolder.getLocale()));
            return RedirectUtils.redirectToOpenExistingObject(redirectAttributes, sessionObjectIdentifier, false, GeneralPanel.Process.code());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("msprocess.delete.error", null, LocaleContextHolder.getLocale()));
            return RedirectUtils.redirectToOpenExistingObject(redirectAttributes, sessionObjectIdentifier, false, null);
        }
    }

    @PostMapping("/validate")
    @ResponseBody
    public boolean validateDeleteManualSubProcess(@RequestParam String processIdString) {
        CProcessId processId = ProcessUtils.getProcessIdFromString(processIdString);
        CProcess process = processService.selectProcess(processId, false);
        validateProcess(processIdString, process);
        return actionService.isActionExistsForProcessId(processId);
    }

    private void validateProcess(String processIdString, CProcess process) {
        if (Objects.isNull(process))
            throw new RuntimeException("Cannot find manual sub process ! ID: " + processIdString);

        boolean isManualSubProcess = ProcessTypeUtils.isManualSubProcess(process);
        if (!isManualSubProcess)
            throw new RuntimeException("Select process id is not for manual sub process ! ID: " + processIdString);
    }

}
