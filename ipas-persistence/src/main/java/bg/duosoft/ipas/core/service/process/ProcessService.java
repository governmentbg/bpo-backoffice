package bg.duosoft.ipas.core.service.process;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.core.model.process.*;
import bg.duosoft.ipas.core.model.userdoc.CUserdocDocData;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.persistence.model.entity.vw.VwSelectNextProcessActions;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ProcessService {

    List<CProcess> selectNotFinishedUserdocProcessesRelatedToIpObjectProcess(CProcessId cProcessId,boolean addProcessEvents);

    List<CProcess> selectSubUserdocProcessesRelatedToIpObjectProcess(CProcessId cProcessId,boolean addProcessEvents);

    void setResponsibleUserChangeAsRead(String processType, Integer processNbr,Integer responsibleUser);

    CProcess selectProcess(CProcessId cProcessId, boolean addProcessEvents);

    CProcess selectUserdocProcess(CDocumentId documentId, boolean addProcessEvents);

    CFileId selectTopProcessFileId(CProcessId cProcessId);

    CProcessId selectFileProcessId(CFileId fileId);

    CProcessId selectUserdocProcessId(CDocumentId documentId);

    CProcessParentData generateProcessParentHierarchy(CProcessId cProcessId);

    CProcessParentData generateProcessHierarchy(CProcessId cProcessId);

    List<CNextProcessAction> selectNextProcessActions(CProcess process, boolean hasRightsForExecuteAutomaticActions);

    CNextProcessAction selectNextProcessActionBySelectedActionType(CProcess cProcess, String actionTyp);

    void updateProcess(CProcess cProcess) throws IpasValidationException;

    CNextProcessAction selectNextActionAfterExpirationDate(CProcessId processId, String statusCode);

    List<CNextProcessAction> selectActionsWithExpirationDate(CProcess cProcess);

    boolean isUpperProcessUserdoc(CDocumentId id);

    void updateResponsibleUser(Integer userId, String processType, Integer processNbr);

    void updateResponsibleUsers(Integer userId, List<CProcessId> cProcessIds);

    Integer selectResponsibleUserOfUserdocParentProcess(String docOri, String docLog, Integer docSer, Integer docNbr);

    Integer selectResponsibleUser(CProcessId processId);

    Integer selectIpObjectResponsibleUser(CFileId fileId);

    Integer selectUserdocResponsibleUser(CDocumentId documentId);

    Integer selectOffidocResponsibleUser(COffidocId offidocId);

    List<String> selectProcessIdsOfUserdocAndOffidocChildren(CProcessId processId);

    long count();

    int selectUpperProcessesCount(String procTyp, Integer procNbr);

    int selectUserdocUpperProcessesCount(String procTyp, Integer procNbr);

    void deleteProcess(CProcessId processId);

    CProcess createManualSubProcess(CProcess parentProcess, String manualSubProcessType);

    CTopProcessFileData selectTopProcessFileData(CProcessId topProcessId);

    boolean isUpperProcessOfUserdocIsInStatusRegistered(CDocumentId documentId);

    List<Integer> selectSubProcessesResponsibleUserIds(String procTyp, Integer procNbr);

    List<CProcessResponsibleUserChange> selectNotProcessedAbdocsUserTargeting();

    void updateAbdocsUserTargetingAsProcessed(String processType, Integer processNbr,Integer changeNbr);

    void updateUserdocProcessCreationDate(Date creationDate, String docOri, String docLog, Integer docSer, Integer docNbr);

    String selectUserdocTypeByProcessId(CProcessId processId);

    void updateStatusCodeAndDateById(String statusCode,CProcessId processId);

    List<CUserdocDocData> selectSubUserdocPartialDataByUserdocTyp(CProcessId processId, String userdocType);

}