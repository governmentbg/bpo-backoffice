package bg.duosoft.ipas.util.process;

import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.document.CDocumentSeqId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.core.model.process.CProcessEvent;
import bg.duosoft.ipas.enums.ProcessEventType;
import bg.duosoft.ipas.persistence.model.nonentity.ProcessEventResult;
import bg.duosoft.ipas.util.date.DateUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ProcessEventUtils {
    private static String PROCESS_EVENT_IDENTIFIER_SEPARATOR = "-";

    public static ProcessEventResult convertToProcessEventResult(Object[] object) {
        ProcessEventResult processEventResult = new ProcessEventResult();
        processEventResult.setProcessId(new CProcessId((String) object[0], ((Number) object[1]).intValue()));

        Object upperProcType = object[2];
        Object upperProcNbr = object[3];
        if (Objects.nonNull(upperProcType) && Objects.nonNull(upperProcNbr)) {
            processEventResult.setUpperProcessId(new CProcessId((String) upperProcType, ((Number) upperProcNbr).intValue()));
        }

        Object fileSeq = object[4];
        Object fileTyp = object[5];
        Object fileSer = object[6];
        Object fileNbr = object[7];
        if (Objects.nonNull(fileSeq) && Objects.nonNull(fileTyp) && Objects.nonNull(fileSer) && Objects.nonNull(fileNbr)) {
            processEventResult.setFileId(new CFileId((String) fileSeq, (String) fileTyp, ((Number) fileSer).intValue(), ((Number) fileNbr).intValue()));
        }

        Object docOri = object[8];
        Object docLog = object[9];
        Object docSer = object[10];
        Object docNbr = object[11];
        if (Objects.nonNull(docOri) && Objects.nonNull(docLog) && Objects.nonNull(docSer) && Objects.nonNull(docNbr)) {
            processEventResult.setDocumentId(new CDocumentId((String) docOri, (String) docLog, ((Number) docSer).intValue(), ((Number) docNbr).intValue()));
        }

        Object offidocOri = object[12];
        Object offidocSer = object[13];
        Object offidocNbr = object[14];
        if (Objects.nonNull(offidocOri) && Objects.nonNull(offidocSer) && Objects.nonNull(offidocNbr)) {
            processEventResult.setOffidocId(new COffidocId((String) offidocOri, ((Number) offidocSer).intValue(), ((Number) offidocNbr).intValue()));
        }

        Object processCreatingDate = object[15];
        if (Objects.nonNull(processCreatingDate)) {
            processEventResult.setProcessCreationDate((Date) processCreatingDate);
        }

        Object expirationDate = object[16];
        if (Objects.nonNull(expirationDate)) {
            processEventResult.setProcessExpirationDate((Date) expirationDate);
        }

        Object statusCode = object[17];
        if (Objects.nonNull(statusCode)) {
            processEventResult.setStatusCode((String) statusCode);
        }

        Object userdocType = object[18];
        if (Objects.nonNull(userdocType)) {
            processEventResult.setUserdocTyp((String) userdocType);
        }

        Object documentFilingDate = object[19];
        if (Objects.nonNull(documentFilingDate)) {
            processEventResult.setDocumentFilingDate((Date) documentFilingDate);
        }

        Object docSeqSer = object[20];
        Object docSeqTyp = object[21];
        Object docSeqNbr = object[22];
        Object docSeqName = object[23];
        if (Objects.nonNull(docSeqSer) && Objects.nonNull(docSeqTyp) && Objects.nonNull(docSeqNbr) && Objects.nonNull(docSeqName)) {
            processEventResult.setDocumentSeqId(new CDocumentSeqId((String) docSeqTyp, (String) docSeqName, ((Number) docSeqNbr).intValue(), ((Number) docSeqSer).intValue()));
        }

        Object externalSystemId = object[24];
        if (Objects.nonNull(externalSystemId)) {
            processEventResult.setExternalSystemId((String) externalSystemId);
        }

        Object servicePersonName = object[25];
        if (Objects.nonNull(servicePersonName)) {
            processEventResult.setApplicantName((String) servicePersonName);
        }

        Object userdocNotes = object[26];
        if (Objects.nonNull(userdocNotes)) {
            processEventResult.setUserdocNotes((String) userdocNotes);
        }

        Object responsibleUserName = object[27];
        if (Objects.nonNull(responsibleUserName)) {
            processEventResult.setResponsibleUserName((String) responsibleUserName);
        }


        return processEventResult;
    }

    public static CProcessEvent getSelectedProcessEvent(String processEventId, List<CProcessEvent> processEventList, ProcessEventType type) {
        CProcessEvent cProcessEvent = null;
        switch (type) {
            case NOTE:
            case ACTION:
                cProcessEvent = selectActionProcessEvent(processEventList, processEventId);
                break;
            case USERDOC:
                cProcessEvent = selectUserdocProcessEvent(processEventList, processEventId);
                break;
            case MANUAL:
                cProcessEvent = selectManualProcessEvent(processEventList, processEventId);
                break;
        }
        return cProcessEvent;
    }

    public static CProcessEvent selectActionProcessEvent(List<CProcessEvent> processEventList, String processEventIdString) {
        CActionId actionId = getActionIdFromString(processEventIdString);
        if (Objects.isNull(actionId))
            return null;

        return processEventList.stream()
                .filter(Objects::nonNull)
                .filter(cProcessEvent -> ProcessEventType.ACTION.code().equals(cProcessEvent.getEventType()) || ProcessEventType.NOTE.code().equals(cProcessEvent.getEventType()))
                .filter(cProcessEvent -> cProcessEvent.getEventAction().getActionId().getActionNbr().equals(actionId.getActionNbr()) &&
                        cProcessEvent.getEventAction().getActionId().getProcessId().getProcessType().equals(actionId.getProcessId().getProcessType()) &&
                        cProcessEvent.getEventAction().getActionId().getProcessId().getProcessNbr().equals(actionId.getProcessId().getProcessNbr()))
                .findFirst()
                .orElse(null);
    }

    public static CProcessEvent selectUserdocProcessEvent(List<CProcessEvent> processEventList, String processEventIdString) {
        CProcessId processId = getProcessIdFromString(processEventIdString);
        if (Objects.isNull(processId))
            return null;

        return processEventList.stream()
                .filter(Objects::nonNull)
                .filter(cProcessEvent -> ProcessEventType.USERDOC.code().equals(cProcessEvent.getEventType()))
                .filter(cProcessEvent -> cProcessEvent.getEventProcessId().getProcessType().equals(processId.getProcessType()) &&
                        cProcessEvent.getEventProcessId().getProcessNbr().equals(processId.getProcessNbr()))
                .findFirst()
                .orElse(null);
    }

    public static CProcessEvent selectManualProcessEvent(List<CProcessEvent> processEventList, String processEventIdString) {
        CProcessId processId = getProcessIdFromString(processEventIdString);
        if (Objects.isNull(processId))
            return null;

        return processEventList.stream()
                .filter(Objects::nonNull)
                .filter(cProcessEvent -> ProcessEventType.MANUAL.code().equals(cProcessEvent.getEventType()))
                .filter(cProcessEvent -> cProcessEvent.getEventProcessId().getProcessType().equals(processId.getProcessType()) &&
                        cProcessEvent.getEventProcessId().getProcessNbr().equals(processId.getProcessNbr()))
                .findFirst()
                .orElse(null);
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

    public static Integer calculateRemainingDaysBeforeExpirationDate(Date expirationDate) {
        if (Objects.isNull(expirationDate))
            return null;

        LocalDate expirationDateAslocalDate = DateUtils.dateToLocalDate(expirationDate);
        LocalDate today = LocalDate.now();

        if (today.isAfter(expirationDateAslocalDate)) {
            return null;
        } else {
            long days = ChronoUnit.DAYS.between(expirationDateAslocalDate, today);
            return Math.abs(Math.toIntExact(days));
        }
    }

    public static boolean isProcessEventActionExists(List<CProcessEvent> processEventList) {
        if (CollectionUtils.isEmpty(processEventList))
            return false;

        long count = processEventList.stream()
                .filter(Objects::nonNull)
                .filter(cProcessEvent -> ProcessEventType.ACTION.code().equals(cProcessEvent.getEventType()) || ProcessEventType.NOTE.code().equals(cProcessEvent.getEventType()))
                .count();

        return count != 0;
    }

}
