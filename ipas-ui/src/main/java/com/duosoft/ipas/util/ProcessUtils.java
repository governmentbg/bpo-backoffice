package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.offidoc.COffidocType;
import bg.duosoft.ipas.core.model.process.*;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.service.offidoc.OffidocService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.NextProcessActionType;
import bg.duosoft.ipas.enums.ProcessEventType;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.enums.security.SecurityRolePrefix;
import bg.duosoft.ipas.util.process.ProcessActionUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.file.CFileSessionUtils;
import com.duosoft.ipas.util.session.offidoc.OffidocSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProcessUtils {
    private static String PROCESS_EVENT_IDENTIFIER_SEPARATOR = "-";

    public static String getProcessEventIdentifier(CProcessEvent cProcessEvent) {
        if (Objects.isNull(cProcessEvent))
            throw new RuntimeException("Process event is empty !");

        String eventType = cProcessEvent.getEventType();
        if (ProcessEventType.ACTION.code().equals(eventType) || ProcessEventType.NOTE.code().equals(eventType)) {
            CActionId actionId = cProcessEvent.getEventAction().getActionId();
            return actionId.getProcessId().getProcessType() + PROCESS_EVENT_IDENTIFIER_SEPARATOR +
                    actionId.getProcessId().getProcessNbr() + PROCESS_EVENT_IDENTIFIER_SEPARATOR +
                    actionId.getActionNbr();
        } else if (ProcessEventType.USERDOC.code().equals(eventType) || ProcessEventType.MANUAL.code().equals(eventType)) {
            CProcessId eventProcessId = cProcessEvent.getEventProcessId();
            return eventProcessId.getProcessType() + PROCESS_EVENT_IDENTIFIER_SEPARATOR + eventProcessId.getProcessNbr();
        } else
            throw new RuntimeException("Process event type is empty !");
    }

    public static CProcessId getProcessIdFromString(String processIdString) {
        if (StringUtils.isEmpty(processIdString))
            return null;

        String[] split = processIdString.split(PROCESS_EVENT_IDENTIFIER_SEPARATOR);
        if (split.length != 2)
            throw new RuntimeException("Wrong process identifier !");

        CProcessId cProcessId = new CProcessId();
        cProcessId.setProcessType(split[0]);
        cProcessId.setProcessNbr(Integer.valueOf(split[1]));
        return cProcessId;
    }

    public static CActionId getActionIdFromString(String actionIdString) {
        if (StringUtils.isEmpty(actionIdString))
            return null;

        String[] split = actionIdString.split(PROCESS_EVENT_IDENTIFIER_SEPARATOR);
        if (split.length != 3)
            throw new RuntimeException("Wrong action identifier !");

        CProcessId cProcessId = new CProcessId();
        cProcessId.setProcessType(split[0]);
        cProcessId.setProcessNbr(Integer.valueOf(split[1]));

        CActionId cActionId = new CActionId();
        cActionId.setProcessId(cProcessId);
        cActionId.setActionNbr(Integer.valueOf(split[2]));
        return cActionId;
    }

    public static List<CNextProcessAction> selectNextNotificationProcessActions(List<CNextProcessAction> nextProcessActions) {
        if (CollectionUtils.isEmpty(nextProcessActions))
            return null;

        return nextProcessActions.stream()
                .filter(Objects::nonNull)
                .filter(cNextProcessAction -> cNextProcessAction.getProcessActionType() == NextProcessActionType.NOTE)
                .sorted(Comparator.comparing(CNextProcessAction::getActionTypeName))
                .collect(Collectors.toList());
    }

    public static List<CNextProcessAction> selectNextNormalProcessActions(List<CNextProcessAction> nextProcessActions) {
        if (CollectionUtils.isEmpty(nextProcessActions))
            return null;

        return nextProcessActions.stream()
                .filter(Objects::nonNull)
                .filter(cNextProcessAction -> cNextProcessAction.getProcessActionType() == NextProcessActionType.NORMAL)
                .sorted(Comparator.comparing(CNextProcessAction::getActionTypeName))
                .collect(Collectors.toList());
    }

    public static List<CNextProcessAction> selectNextSpecialProcessActions(List<CNextProcessAction> nextProcessActions) {
        if (CollectionUtils.isEmpty(nextProcessActions))
            return null;

        return nextProcessActions.stream()
                .filter(Objects::nonNull)
                .filter(cNextProcessAction -> cNextProcessAction.getProcessActionType() == NextProcessActionType.SPECIAL_RANDOM_TO_RANDOM ||
                        cNextProcessAction.getProcessActionType() == NextProcessActionType.SPECIAL_RANDOM_TO_SPECIFIC ||
                        cNextProcessAction.getProcessActionType() == NextProcessActionType.SPECIAL_SPECIFIC_TO_RANDOM)
                .sorted(Comparator.comparing(CNextProcessAction::getActionTypeName))
                .collect(Collectors.toList());
    }

    // String process -> procTyp-procNumber (example: 2-121371)
    public static CProcess selectProcess(String process, ProcessService processService, boolean addProcessEvents) {
        CProcessId processId = ProcessUtils.getProcessIdFromString(process);
        CProcess cProcess = processService.selectProcess(processId, addProcessEvents);
        if (Objects.isNull(cProcess))
            throw new RuntimeException("Missing process with ID = " + process);

        return cProcess;
    }

    public static boolean isContainUserdocOrOutgoingOffidoc(List<CProcessEvent> cProcessEvents) {
        CProcessEvent result = cProcessEvents.stream()
                .filter(ProcessUtils::isUserdocOrOutgoingOffidoc)
                .findAny()
                .orElse(null);
        return result != null;
    }

    public static boolean isContainUserdoc(List<CProcessEvent> cProcessEvents) {
        CProcessEvent result = cProcessEvents.stream()
                .filter(cProcessEvent -> cProcessEvent.getEventType().equals(ProcessEventType.USERDOC.code()))
                .findAny()
                .orElse(null);
        return result != null;
    }

    public static boolean isUserdocOrOutgoingOffidoc(CProcessEvent cProcessEvent) {
        String eventType = cProcessEvent.getEventType();
        boolean isUserdoc = eventType.equals(ProcessEventType.USERDOC.code());

        boolean isOutgoingOffidoc = false;
        CActionProcessEvent eventAction = cProcessEvent.getEventAction();
        if (Objects.nonNull(eventAction)) {
            COffidoc generatedOffidoc = eventAction.getGeneratedOffidoc();
            if (Objects.nonNull(generatedOffidoc)) {
                COffidocType offidocType = generatedOffidoc.getOffidocType();
                if (Objects.nonNull(offidocType.getDirection()) && offidocType.getDirection().equalsIgnoreCase(OffidocService.OFFIDOC_DIRECTION_OUT)) {
                    isOutgoingOffidoc = true;
                }
            }
        }

        return isUserdoc || isOutgoingOffidoc;
    }

    public static CProcessEvent selectProcessEventByActionId(CActionId actionId, ProcessService processService) {
        if (Objects.isNull(actionId))
            throw new RuntimeException("Action ID is emtpy !");

        CProcess cProcess = processService.selectProcess(actionId.getProcessId(), true);
        if (Objects.isNull(cProcess))
            throw new RuntimeException("Missing process with ID = " + actionId.getProcessId());

        return cProcess.getProcessEventList().stream()
                .filter(Objects::nonNull)
                .filter(cProcessEvent -> Objects.nonNull(cProcessEvent.getEventAction()))
                .filter(cProcessEvent -> {
                    CActionId id = cProcessEvent.getEventAction().getActionId();
                    CProcessId processId = id.getProcessId();
                    return id.getActionNbr().equals(actionId.getActionNbr()) &&
                            processId.getProcessNbr().equals(actionId.getProcessId().getProcessNbr()) &&
                            processId.getProcessType().equals(actionId.getProcessId().getProcessType());
                })
                .findFirst()
                .orElse(null);
    }

    public static boolean isSameProcess(CProcessId processId, CProcessId anotherProcessId) {
        String processType = processId.getProcessType();
        Integer processNbr = processId.getProcessNbr();
        String anotherProcessType = anotherProcessId.getProcessType();
        Integer anotherProcessNbr = anotherProcessId.getProcessNbr();
        return (processNbr.equals(anotherProcessNbr)) && (processType.equalsIgnoreCase(anotherProcessType));
    }

    public static CProcessId selectFirstParentOfProcess(CProcessParentData processParentData) {
        if (Objects.isNull(processParentData))
            return null;

        return processParentData.getProcessId();
    }

    public static boolean isUserdocProcess(CProcess process) {
        CDocumentId documentId = process.getProcessOriginData().getDocumentId();
        return Objects.nonNull(documentId) && Objects.nonNull(documentId.getDocNbr());
    }

    public static boolean hasPermissionsForChangeManualDueDate(CProcess process) {
        if (Objects.isNull(process))
            return false;
        if (SecurityUtils.hasRights(SecurityRole.ProcessChangeManualDueDate))
            return true;
        return ProcessActionUtils.isResposibleUser(process);
    }

    public static boolean hasPermissionsForEditProcessDataFromProcessMenu(HttpServletRequest request, String sessionIdentifier, CProcess process,ProcessService processService,UserService userService) {
        return ProcessActionUtils.isResposibleUser(process) || hasPermissionsForEditObject(request, sessionIdentifier,processService,userService) || ProcessActionUtils.hasPermissionsForExecuteActions(process);
    }

    public static boolean hasPermissionsForEditManualSubProcessProcessDataFromProcessMenu(HttpServletRequest request, CProcess process) {
        return ProcessActionUtils.isResposibleUser(process) || SecurityUtils.isManualSubProcessEditEnabled(process) || ProcessActionUtils.hasPermissionsForExecuteActions(process);
    }

    public static boolean hasPermissionsToEditProcess(HttpServletRequest request, String sessionIdentifier, CProcess process,ProcessService processService,UserService userService) {
        boolean hasPermissionForExecuteActions = SecurityUtils.hasRights(SecurityRole.ProcessExecuteActionsForForeignObjects);
        boolean hasPermissionForChangeResponsibleUser = SecurityUtils.hasRights(SecurityRole.ProcessChangeResponsibleUser);
        boolean hasPermissionForChangeExpirationDate = SecurityUtils.hasRights(SecurityRole.ProcessChangeManualDueDate);
        return ProcessActionUtils.isResposibleUser(process) || hasPermissionForChangeExpirationDate || hasPermissionForChangeResponsibleUser || hasPermissionForExecuteActions || hasPermissionsForEditObject(request, sessionIdentifier,processService,userService);
    }

    public static boolean hasPermissionsToEditManualSubProcessProcess(HttpServletRequest request, CProcess process) {
        boolean hasPermissionForExecuteActions = SecurityUtils.hasRights(SecurityRole.ProcessExecuteActionsForForeignObjects);
        boolean hasPermissionForChangeResponsibleUser = SecurityUtils.hasRights(SecurityRole.ProcessChangeResponsibleUser);
        boolean hasPermissionForChangeExpirationDate = SecurityUtils.hasRights(SecurityRole.ProcessChangeManualDueDate);
        return ProcessActionUtils.isResposibleUser(process) || hasPermissionForChangeExpirationDate || hasPermissionForChangeResponsibleUser || hasPermissionForExecuteActions || SecurityUtils.isManualSubProcessEditEnabled(process);
    }

    private static boolean hasPermissionsForEditObject(HttpServletRequest request, String sessionIdentifier, ProcessService processService, UserService userService) {
        boolean hasEditObjectPermission = false;
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case MARK:
                CFile markFile = CFileSessionUtils.getSessionFile(request, sessionIdentifier);
                hasEditObjectPermission = SecurityUtils.isIntellectualPropertyObjectEditEnabled(SecurityRolePrefix.IP_OJBECT_MARK_SECURITY_ROLE_PREFIX, markFile,processService,userService);
                break;
            case PATENT:
                CFile patentFile = CFileSessionUtils.getSessionFile(request, sessionIdentifier);
                hasEditObjectPermission = SecurityUtils.isIntellectualPropertyObjectEditEnabled(SecurityRolePrefix.IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX, patentFile,processService,userService);
                break;
            case OFFIDOC:
                COffidoc offidoc = OffidocSessionUtils.getSessionOffidoc(request, sessionIdentifier);
                hasEditObjectPermission = SecurityUtils.isOffidocObjectEditEnabled(offidoc,processService,userService);
                break;
            case USERDOC:
                CUserdoc userdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
                hasEditObjectPermission = SecurityUtils.isUserdocObjectEditEnabled(userdoc,processService,userService);
                break;
        }
        return hasEditObjectPermission;
    }
}
