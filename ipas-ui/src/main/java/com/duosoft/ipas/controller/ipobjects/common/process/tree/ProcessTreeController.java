package com.duosoft.ipas.controller.ipobjects.common.process.tree;

import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.process.CProcessEvent;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.ProcessEventType;
import bg.duosoft.ipas.util.process.ProcessEventUtils;
import bg.duosoft.ipas.util.process.ProcessTypeUtils;
import com.duosoft.ipas.util.ProcessUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/process/tree")
public class ProcessTreeController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private UserService userService;

    @PostMapping("/load-process-tree")
    public String loadProcessTree(Model model, HttpServletRequest request,
                                  @RequestParam Boolean isViewMode,
                                  @RequestParam String mainProcessId,
                                  @RequestParam String processEventId,
                                  @RequestParam(required = false) String sessionIdentifier,
                                  @RequestParam String processEventType) {
        CProcess cProcess = ProcessUtils.selectProcess(mainProcessId, processService, true);
        List<CProcessEvent> processEventList = cProcess.getProcessEventList();
        if (CollectionUtils.isEmpty(processEventList))
            throw new RuntimeException("Process Event List is emtpy ! " + mainProcessId);

        ProcessEventType type = Enum.valueOf(ProcessEventType.class, processEventType);
        CProcessEvent cProcessEvent = ProcessEventUtils.getSelectedProcessEvent(processEventId, processEventList, type);

        if (Objects.isNull(cProcessEvent))
            throw new RuntimeException("Cannot find process event - " + mainProcessId + ", " + processEventId);

        model.addAttribute("processEvent", cProcessEvent);
        model.addAttribute("isFullyLoad", true);
        model.addAttribute("isViewMode", isViewMode);
        setHasPermissionToViewRightProcessMenu(request, model, sessionIdentifier, cProcess);
        return "ipobjects/common/process/process_tree :: node";
    }

    private void setHasPermissionToViewRightProcessMenu(HttpServletRequest request, Model model, String sessionIdentifier, CProcess cProcess) {
        boolean isManualSubProcess = ProcessTypeUtils.isManualSubProcess(cProcess);
        if (isManualSubProcess) {
            model.addAttribute("hasPermissionToViewRightProcessMenu", ProcessUtils.hasPermissionsForEditManualSubProcessProcessDataFromProcessMenu(request, cProcess));
        } else {
            model.addAttribute("hasPermissionToViewRightProcessMenu", ProcessUtils.hasPermissionsForEditProcessDataFromProcessMenu(request, sessionIdentifier, cProcess,processService,userService));
        }
    }
}
