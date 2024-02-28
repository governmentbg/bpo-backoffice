package com.duosoft.ipas.controller.ipobjects.common.process.msprocess;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.service.nomenclature.ProcessTypeService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.util.RedirectUtils;
import com.duosoft.ipas.util.json.ManualSubProcessData;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/msprocess")
public class ManualSubProcessController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private ProcessTypeService processTypeService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = {"/detail/{processType}/{processNumber}"}, method = RequestMethod.GET)
    public String view(Model model, HttpServletRequest request,
                       @PathVariable("processType") String processType,
                       @PathVariable("processNumber") Integer processNumber) {
        validateProcessType(processType);
        CProcessId processId = new CProcessId(processType, processNumber);

        CProcess manualSubProcess = selectManualSubProcessFromModel(model);
        if (Objects.isNull(manualSubProcess)) {
            manualSubProcess = processService.selectProcess(processId, false);
            if (Objects.isNull(manualSubProcess)) {
                throw new RuntimeException("Cannot find manual sub process with id = " + processId);
            }
        }

        Map<String, String> processTypeMap = processTypeService.getProcessTypeMap();
        CProcessParentData manualSubProcessParentData = processService.generateProcessParentHierarchy(processId);
        model.addAttribute("manualSubProcess", manualSubProcess);
        model.addAttribute("processTypeMap", processTypeMap);
        model.addAttribute("manualSubProcessParentData", manualSubProcessParentData);
        return "ipobjects/common/process/msprocess/view";
    }

    @PostMapping("/save")
    public String saveManualSubProcess(HttpServletRequest request,
                                       RedirectAttributes redirectAttributes,
                                       @RequestParam String data,
                                       @RequestParam(required = false) List<String> editedPanels) {

        ManualSubProcessData manualSubProcessData = JsonUtil.readJson(data, ManualSubProcessData.class);
        CProcessId processId = new CProcessId(manualSubProcessData.getProcessType(), manualSubProcessData.getProcessNumber());
        CProcess manualSubProcess = processService.selectProcess(processId, false);
        if (Objects.isNull(manualSubProcess)) {
            throw new RuntimeException("Cannot find manual sub process! ID: " + processId);
        }

        manualSubProcess.getProcessOriginData().setManualProcDescription(manualSubProcessData.getManualProcDescription());

        try {
            processService.updateProcess(manualSubProcess);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("edit.success.msprocess", null, LocaleContextHolder.getLocale()));
        } catch (IpasValidationException e) {
            redirectAttributes.addFlashAttribute("validationErrors", e.getErrors());
            redirectAttributes.addFlashAttribute("manualSubProcess", manualSubProcess);
            redirectAttributes.addFlashAttribute("editedPanels", editedPanels);
        }

        return RedirectUtils.redirectToManualSubProcessViewPage(processId);
    }

    private void validateProcessType(String processType) {
        boolean isProcessTypeForManualSubProcess = processTypeService.isProcessTypeForManualSubProcess(processType);
        if (!isProcessTypeForManualSubProcess) {
            throw new RuntimeException("Invalid process type for manual sub process! Process type: " + processType);
        }
    }

    private CProcess selectManualSubProcessFromModel(Model model) {
        return (CProcess) model.asMap().get("manualSubProcess");
    }

}
