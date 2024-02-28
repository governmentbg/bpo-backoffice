package com.duosoft.ipas.controller.ipobjects.common.process;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CFileRecordal;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.process.CActionProcessEvent;
import bg.duosoft.ipas.core.model.process.CNextProcessAction;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.process.CProcessEvent;
import bg.duosoft.ipas.core.model.reception.CProcessType;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.service.file.FileRecordalService;
import bg.duosoft.ipas.core.service.impl.file.FileServiceImpl;
import bg.duosoft.ipas.core.service.impl.logging.diff.DiffGenerator;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.nomenclature.ProcessTypeService;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.offidoc.OffidocService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.reception.ReceptionInitializationService;
import bg.duosoft.ipas.core.service.reception.ReceptionRequestService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.process.ProcessTypeUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.PatentLikeObjectsUtils;
import com.duosoft.ipas.util.ProcessUtils;
import com.duosoft.ipas.util.reception.ReceptionUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import com.duosoft.ipas.util.session.offidoc.OffidocSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
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

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/process")
@Slf4j
public class ProcessController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProcessService processService;

    @Autowired
    private ProcessTypeService processTypeService;

    @Autowired
    private MarkService markService;

    @Autowired
    private PatentService patentService;

    @Autowired
    private UserdocService userdocService;

    @Autowired
    private OffidocService offidocService;

    @Autowired
    private FileServiceImpl fileService;

    @Autowired
    private ReceptionInitializationService receptionInitializationService;

    @Autowired
    private ReceptionRequestService receptionRequestService;

    @Autowired
    private FileRecordalService fileRecordalService;

    @Autowired
    private UserService userService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private UserdocTypesService userdocTypesService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(DateUtils.DATE_FORMAT_DOT), true, 10));
    }

    @PostMapping("/load-panel")
    public String loadProcessPanel(HttpServletRequest request, Model model, @RequestParam(required = false) String sessionIdentifier, @RequestParam String processIdString) {
        boolean isEditEnabled;
        boolean isReception = false;

        CProcess process = processService.selectProcess(ProcessUtils.getProcessIdFromString(processIdString), true);
        boolean isManualSubProcess = ProcessTypeUtils.isManualSubProcess(process);
        if (isManualSubProcess) {
            isEditEnabled = ProcessUtils.hasPermissionsToEditManualSubProcessProcess(request, process);
        } else {
            SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
            switch (sessionObjectType) {
                case MARK:
                    isReception = MarkSessionUtils.getSessionMark(request, sessionIdentifier).isReception();
                    break;
                case PATENT:
                    isReception = PatentSessionUtils.getSessionPatent(request, sessionIdentifier).isReception();
                    break;
            }
            isEditEnabled = ProcessUtils.hasPermissionsToEditProcess(request, sessionIdentifier, process,processService,userService);
        }
        model.addAttribute("isReception", isReception);
        model.addAttribute("process", process);
        model.addAttribute("processTypeMap", processTypeService.getProcessTypeMap());
        model.addAttribute("editEnabled", isEditEnabled);
        return "ipobjects/common/process/process_panel :: process-panel";
    }

    @PostMapping("/update-panel-content")
    public String editPanelProcess(HttpServletRequest request, Model model, @RequestParam String process, @RequestParam Boolean isEdit, @RequestParam boolean isReception, @RequestParam(required = false) String sessionIdentifier) {
        boolean isObjectChanged = false;
        CProcessId processId = ProcessUtils.getProcessIdFromString(process);
        boolean isManualSubProcess = processTypeService.isProcessTypeForManualSubProcess(Objects.requireNonNull(processId).getProcessType());
        if (!isManualSubProcess && isEdit) {
            SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
            switch (sessionObjectType) {
                case MARK:
                    CMark sessionMark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
                    CMark originalMark = getOriginalMark(sessionMark);
                    isObjectChanged = DiffGenerator.create(originalMark, sessionMark).isChanged();
                    break;
                case PATENT:
                    isObjectChanged = PatentLikeObjectsUtils.isSessionPatentEdited(request, sessionIdentifier);
                    break;
                case USERDOC:
                    CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
                    CUserdoc originalUserdoc = getOriginalUserdoc(sessionUserdoc);
                    isObjectChanged = DiffGenerator.create(originalUserdoc, sessionUserdoc).isChanged();
                    break;
                case OFFIDOC:
                    COffidoc sessionOffidoc = OffidocSessionUtils.getSessionOffidoc(request, sessionIdentifier);
                    COffidoc originalOffidoc = getOriginalOffidoc(sessionOffidoc);
                    isObjectChanged = DiffGenerator.create(originalOffidoc, sessionOffidoc).isChanged();
                    break;
            }
        }

        if (!isObjectChanged) {
            CProcess cProcess = ProcessUtils.selectProcess(process, processService, true);
            List<CNextProcessAction> nextProcessActions = processService.selectNextProcessActions(cProcess, SecurityUtils.hasRights(SecurityRole.ProcessAutomaticActionExecute));
            model.addAttribute("process", cProcess);
            model.addAttribute("nextProcessActions", nextProcessActions);
            model.addAttribute("isReception", isReception);
            model.addAttribute("processTypeMap", processTypeService.getProcessTypeMap());
            setHasPermissionToViewRightProcessMenu(request, model, sessionIdentifier, isManualSubProcess, cProcess);
            if (ProcessTypeUtils.isIpObjectProcess(cProcess) || ProcessTypeUtils.isUserdocProcess(cProcess)) {
                List<CProcessType> msprocessTypes = processTypeService.selectByRelatedToWcode(ProcessTypeUtils.MANUAL_SUB_PROCESS_TYPE_WCODE);
                model.addAttribute("msprocessTypes", msprocessTypes);
            }

            if (ProcessTypeUtils.isUserdocProcess(cProcess)) {
                checkRecordalExistence(model, cProcess);
            }
            return "ipobjects/common/process/process_panel :: process";
        }

        String message = messageSource.getMessage("process.edit.mode.error", null, LocaleContextHolder.getLocale());
        model.addAttribute("message", message);
        return "base/modal/error :: process-error-modal";
    }

    private void checkRecordalExistence(Model model, CProcess cProcess) {
        String statusCode = cProcess.getStatus().getStatusId().getStatusCode();
        boolean statusTriggerActivity = UserdocUtils.isStatusTriggerActivityForRecordalRegistration(statusCode, cProcess, statusService, userdocTypesService);
        if (statusTriggerActivity) {
            List<CProcessEvent> processEventList = cProcess.getProcessEventList();
            if(!CollectionUtils.isEmpty(processEventList)){
                CProcessEvent authorizationAction = processEventList.stream()
                        .filter(event -> Objects.nonNull(event.getEventAction()) && Objects.nonNull(event.getEventAction().getNewStatus()))
                        .filter(event -> event.getEventAction().getNewStatus().getStatusId().getStatusCode().equalsIgnoreCase(statusCode))
                        .findFirst()
                        .orElse(null);
                if (Objects.nonNull(authorizationAction)) {
                    CActionProcessEvent eventAction = authorizationAction.getEventAction();
                    LocalDate actionDate = DateUtils.convertToLocalDate(eventAction.getActionDate());
                    if (actionDate.isAfter(DefaultValue.NEW_IPAS_START_DATE)) {
                        CFileRecordal recordal = fileRecordalService.selectRecordalByActionId(eventAction.getActionId());
                        if (Objects.isNull(recordal)) {
                            model.addAttribute("missingRecordalRecordError", true);
                        }
                    }
                }
            }
        }
    }

    private void setHasPermissionToViewRightProcessMenu(HttpServletRequest request, Model model, String sessionIdentifier, boolean isManualSubProcess, CProcess cProcess) {
        if (isManualSubProcess) {
            model.addAttribute("hasPermissionToViewRightProcessMenu", ProcessUtils.hasPermissionsForEditManualSubProcessProcessDataFromProcessMenu(request, cProcess));
        } else {
            model.addAttribute("hasPermissionToViewRightProcessMenu", ProcessUtils.hasPermissionsForEditProcessDataFromProcessMenu(request, sessionIdentifier, cProcess,processService,userService));
        }
    }

    private CMark getOriginalMark(CMark sessionMark) {
        CMark originalMark;
        if (sessionMark.isReception()) {
            CFileId fileId = sessionMark.getFile().getFileId();
            originalMark = ReceptionUtils.getReceptionMark(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr(), markService, fileService, receptionRequestService, receptionInitializationService);
        } else {
            originalMark = markService.findMark(sessionMark.getFile().getFileId(), false);
        }
        return originalMark;
    }

    private CPatent getOriginalPatent(CPatent sessionPatent) {
        CPatent originalPatent;
        if (sessionPatent.isReception()) {
            CFileId fileId = sessionPatent.getFile().getFileId();
            originalPatent = ReceptionUtils.getReceptionPatent(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr(), patentService, fileService, receptionRequestService, receptionInitializationService);
        } else {
            originalPatent = patentService.findPatent(sessionPatent.getFile().getFileId(), false);
        }
        return originalPatent;
    }

    private CUserdoc getOriginalUserdoc(CUserdoc sessionUserdoc) {
        return userdocService.findUserdoc(sessionUserdoc.getDocumentId());
    }

    private COffidoc getOriginalOffidoc(COffidoc sessionOffidoc) {
        return offidocService.findOffidoc(sessionOffidoc.getOffidocId());
    }

}
