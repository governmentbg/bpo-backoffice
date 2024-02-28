package bg.duosoft.ipas.util.process;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.process.CNextProcessAction;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.process.CTopProcessFileData;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.service.action.ActionService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.IpObjectSplitCode;
import bg.duosoft.ipas.enums.ListCode;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import org.springframework.data.util.Pair;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class ProcessActionUtils {

    public static boolean hasPermissionsForExecuteAutomaticActions(CNextProcessAction nextProcessAction) {
        if (Objects.nonNull(nextProcessAction.getAutomaticActionWcode()) && !SecurityUtils.hasRights(SecurityRole.ProcessAutomaticActionExecute)) {
            return false;
        }
        return true;
    }

    public static boolean hasPermissionsForExecuteActions(CProcess process) {
        if (Objects.isNull(process))
            return false;
        if (SecurityUtils.hasRights(SecurityRole.ProcessExecuteActionsForForeignObjects))
            return true;
        return isResposibleUser(process);
    }

    public static boolean isResposibleUser(CProcess process) {
        if (Objects.isNull(process.getResponsibleUser()) || Objects.isNull(process.getResponsibleUser().getUserId()))
            return false;

        return SecurityUtils.getLoggedUserAndAuthorizedByUserIds().contains(process.getResponsibleUser().getUserId());
    }

    public static Date changeMidnightActionDate(Date actionDate, CProcessId processId, ActionService actionService) {
        if (Objects.nonNull(actionDate)) {
            LocalDateTime localDateTime = DateUtils.convertToLocalDatTime(actionDate);
            if (localDateTime.getHour() == 0 && localDateTime.getMinute() == 0 && localDateTime.getSecond() == 0) {
                Date date = actionService.selectMaxActionDateByDate(processId, actionDate);
                if (Objects.nonNull(date)) {
                    return DateUtils.addSecondsToDate(date, 1L);
                }
            }
        }
        return actionDate;
    }

    public static boolean doesActionCauseIpObjectSplitting(CNextProcessAction nextProcessAction, CProcess process, ProcessService processService, UserdocService userdocService) {
        if (ProcessActionUtils.doesActionContainSplitConfiguration(nextProcessAction)) {
            Pair<IpObjectSplitCode, CFileId> pair = ProcessActionUtils.selectIpObjectSplitType(nextProcessAction, process, processService);
            if (Objects.nonNull(pair)) {
                IpObjectSplitCode splitCode = pair.getFirst();
                switch (splitCode) {
                    case IP_OBJECT_MARK_SPLIT:
                    case IP_OBJECT_DESIGN_SPLIT:
                        return true;
                    case USERDOC_MARK_SPLIT:
                    case USERDOC_DESIGN_SPLIT: {
                        CUserdoc userdoc = userdocService.findUserdoc(process.getProcessOriginData().getDocumentId());
                        if (UserdocUtils.doesUserdocMeetSplitConditions(userdoc)) {
                            return true;
                        }
                        break;
                    }
                }
            }
        }
        return false;
    }

    public static boolean doesActionContainSplitConfiguration(CNextProcessAction nextProcessAction) {
        String actionTypeListCode = nextProcessAction.getActionTypeListCode();
        if (!StringUtils.isEmpty(actionTypeListCode)) {
            ListCode listCode = ListCode.selectByCode(actionTypeListCode);
            return listCode == ListCode.SPLIT;
        }
        return false;
    }

    public static Pair<IpObjectSplitCode, CFileId> selectIpObjectSplitType(CNextProcessAction nextProcessAction, CProcess process, ProcessService processService) {
        if (!doesActionContainSplitConfiguration(nextProcessAction))
            return null;

        if (Objects.isNull(process)) {
            throw new RuntimeException("Process is empty !");
        }

        CTopProcessFileData topProcessFileData = processService.selectTopProcessFileData(process.getProcessOriginData().getTopProcessId());
        if (Objects.isNull(topProcessFileData)) {
            throw new RuntimeException("Top process file data is empty ! Process ID: " + process.getProcessId());
        }

        CFileId fileId = topProcessFileData.getFileId();
        if (Objects.isNull(fileId)) {
            throw new RuntimeException("Top process file id is empty !" + topProcessFileData.getProcessId());
        }

        FileType fileType = FileType.selectByCode(fileId.getFileType());
        switch (fileType) {
            case DIVISIONAL_MARK:
            case MARK: {
                if (ProcessTypeUtils.isIpObjectProcess(process)) {
                    return Pair.of(IpObjectSplitCode.IP_OBJECT_MARK_SPLIT, fileId);
                } else if (ProcessTypeUtils.isUserdocProcess(process)) {
                    return Pair.of(IpObjectSplitCode.USERDOC_MARK_SPLIT, fileId);
                }
                break;
            }
            case DESIGN: {
                if (ProcessTypeUtils.isIpObjectProcess(process)) {
                    return Pair.of(IpObjectSplitCode.IP_OBJECT_DESIGN_SPLIT, fileId);
                } else if (ProcessTypeUtils.isUserdocProcess(process)) {
                    return Pair.of(IpObjectSplitCode.USERDOC_DESIGN_SPLIT, fileId);
                }
                break;
            }
            default:
                return null;
        }
        return null;
    }


}
