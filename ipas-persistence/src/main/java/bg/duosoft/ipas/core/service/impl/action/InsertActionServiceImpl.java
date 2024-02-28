package bg.duosoft.ipas.core.service.impl.action;

import bg.duosoft.ipas.IpasDataUtils;
import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.action.CActionType;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.offidoc.COffidocType;
import bg.duosoft.ipas.core.model.process.*;
import bg.duosoft.ipas.core.service.action.*;
import bg.duosoft.ipas.core.service.nomenclature.ActionTypeService;
import bg.duosoft.ipas.core.service.nomenclature.OffidocTypeService;
import bg.duosoft.ipas.core.service.offidoc.OffidocService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.process.ProcessInsertActionValidator;
import bg.duosoft.ipas.enums.*;
import bg.duosoft.ipas.services.core.*;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.offidoc.OffidocUtils;
import bg.duosoft.ipas.util.process.ProcessActionUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@Slf4j
@LogExecutionTime
public class InsertActionServiceImpl implements InsertActionService {

    private static final String DELETE_ACTION_DEFAULT_REASON = "Exception on insert action additional data !";
    public static final Integer TRIGGER_ACTIVITY_CODE_USERDOC = 2;
    private final Object lockInsertAction = new Object();

    @Autowired
    private IpasActionService ipasActionService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private OffidocTypeService offidocTypeService;

    @Autowired
    private OffidocService offidocService;

    @Autowired
    private ActionService actionService;

    @Autowired
    private ProcessInsertActionValidator processActionValidator;

    @Autowired
    private ActionTypeService actionTypeService;

    @Autowired
    private InsertActionAdditionalDataService insertActionAdditionalDataService;

    @Override
    public void insertAction(CProcessInsertActionRequest insertActionRequest) throws InsertActionException {
        synchronized (lockInsertAction) {
            CProcessId processId = insertActionRequest.getProcessId();
            if (Objects.isNull(processId)) {
                throw new InsertActionException("Process id is empty !");
            }

            CActionType actionType = actionTypeService.findById(insertActionRequest.getActionType());
            if (Objects.isNull(actionType)) {
                throw new InsertActionException("Action type is empty !");
            }

            CProcess process = processService.selectProcess(processId, false);
            if (Objects.isNull(process)) {
                throw new InsertActionException("Process does not exist! Process id: " + processId);
            }
            if (!ProcessActionUtils.hasPermissionsForExecuteActions(process)) {
                throw new InsertActionException("User " + SecurityUtils.getLoggedUsername() + " has not permissions to execute process actions !");
            }

            CNextProcessAction nextProcessAction = processService.selectNextProcessActionBySelectedActionType(process, actionType.getActionType());
            if (!ProcessActionUtils.hasPermissionsForExecuteAutomaticActions(nextProcessAction)) {
                throw new InsertActionException("User " + SecurityUtils.getLoggedUsername() + " has not permissions to execute automatic actions !");
            }

            String generatedOffidoc = nextProcessAction.getGeneratedOffidoc();
            if (!StringUtils.isEmpty(generatedOffidoc) && CollectionUtils.isEmpty(insertActionRequest.getOffidocTemplates())) {
                addDefaultOffidocTemplate(insertActionRequest, generatedOffidoc);
            }

            List<ValidationError> validationErrors = processActionValidator.validate(insertActionRequest, actionType, nextProcessAction, process);
            if (!CollectionUtils.isEmpty(validationErrors)) {
                throw new IpasValidationException(validationErrors);
            }

            printOldOffdicos(processId);//Ipas doesn't allow insertion of new actions if not printed office documents exists
            try {
                CProcessInsertActionResult insertResult = ipasServicesInsertProcessAction(insertActionRequest, nextProcessAction.getProcessActionType());
                if (!insertResult.isInserted()) {
                    throw new InsertActionException("Cannot insert action...");
                }
                insertAdditionalData(insertActionRequest, processId, nextProcessAction);
            } catch (IpasValidationException e) {
                throw e;
            } catch (Exception e) {
                throw new InsertActionException(e);
            }
        }
    }

    private void insertAdditionalData(CProcessInsertActionRequest insertActionRequest, CProcessId processId, CNextProcessAction nextProcessAction) throws IpasServiceException {
        CActionId actionId = null;
        try {
            CProcess updatedProcess = processService.selectProcess(processId, true); // This process contains process event for new action, which was inserted in previous step
            actionId = selectInsertedActionId(updatedProcess, nextProcessAction.getActionType());
            insertActionAdditionalDataService.insertAdditionalData(updatedProcess, actionId, insertActionRequest, nextProcessAction);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            deleteActionInIpas(processId, nextProcessAction, actionId, e);
            throw e;
        }
    }

    private void addDefaultOffidocTemplate(CProcessInsertActionRequest insertActionRequest, String generatedOffidoc) {
        List<String> offidocTemplates = insertActionRequest.getOffidocTemplates();
        if (CollectionUtils.isEmpty(offidocTemplates)) {
            COffidocType offidocTypeObject = offidocTypeService.selectById(generatedOffidoc);
            insertActionRequest.setOffidocTemplates(Collections.singletonList(offidocTypeObject.getDefaultTemplate()));
        }
    }

    private void printOldOffdicos(CProcessId processId) {
        try {
            OffidocUtils.printAllNotPrintedOffidocs(processId, processService, offidocService);
        } catch (IpasServiceException e) {
            log.error(e.getMessage(), e);
        }
    }

    private CActionId selectInsertedActionId(CProcess process, String actionType) {
        List<CProcessEvent> processEventList = process.getProcessEventList();
        CProcessEvent processEvent = processEventList.stream()
                .filter(cProcessEvent -> Objects.nonNull(cProcessEvent.getEventAction()))
                .filter(cProcessEvent -> Objects.nonNull(cProcessEvent.getEventAction().getActionType()))
                .filter(cProcessEvent -> cProcessEvent.getEventAction().getActionType().getActionType().equals(actionType))
                .max(Comparator.comparing(cProcessEvent -> cProcessEvent.getEventAction().getCaptureDate()))
                .orElse(null);

        if (Objects.isNull(processEvent)) {
            throw new RuntimeException("Cannot find process event for process " + process.getProcessId() + " and action type " + actionType);
        }

        CActionId actionId = processEvent.getEventAction().getActionId();
        if (Objects.isNull(actionId)) {
            throw new RuntimeException("Action id is empty for process " + process.getProcessId() + " and action type " + actionType);
        }
        return actionId;
    }

    private CProcessInsertActionResult ipasServicesInsertProcessAction(CProcessInsertActionRequest insertActionRequest, NextProcessActionType processActionType) throws IpasServiceException {
        switch (processActionType) {
            case NORMAL:
                return ipasServicesInsertNormalAction(insertActionRequest);
            case NOTE:
                return ipasServicesInsertNoteAction(insertActionRequest);
            case SPECIAL_RANDOM_TO_RANDOM:
            case SPECIAL_RANDOM_TO_SPECIFIC:
            case SPECIAL_SPECIFIC_TO_RANDOM:
                return ipasServicesInsertSpecialAction(insertActionRequest);
            default:
                throw new IpasServiceException("Invalid NextProcessActionType !");

        }
    }

    private CProcessInsertActionResult ipasServicesInsertNormalAction(CProcessInsertActionRequest cActionRequest) throws IpasServiceException {
        InsertActionRequest insertActionRequest = createIpasServicesInsertActionRequest(cActionRequest);
        if (Objects.isNull(insertActionRequest))
            return null;

        InsertActionResult insertActionResult = ipasActionService.insertAction(insertActionRequest);
        return new CProcessInsertActionResult(insertActionResult.isInserted(), insertActionResult.getProcessType(), insertActionResult.getStatusCode());
    }

    private CProcessInsertActionResult ipasServicesInsertNoteAction(CProcessInsertActionRequest cActionRequest) throws IpasServiceException {
        InsertActionRequest insertActionRequest = createIpasServicesInsertActionRequest(cActionRequest);
        if (Objects.isNull(insertActionRequest))
            return null;

        InsertActionResult insertActionResult = ipasActionService.insertNoteAction(insertActionRequest);
        return new CProcessInsertActionResult(insertActionResult.isInserted(), insertActionResult.getProcessType(), insertActionResult.getStatusCode());
    }

    private CProcessInsertActionResult ipasServicesInsertSpecialAction(CProcessInsertActionRequest cActionRequest) throws IpasServiceException {
        InsertActionRequest insertActionRequest = createIpasServicesInsertActionRequest(cActionRequest);
        if (Objects.isNull(insertActionRequest))
            return null;

        InsertActionResult insertActionResult = ipasActionService.insertSpecialAction(insertActionRequest);
        return new CProcessInsertActionResult(insertActionResult.isInserted(), insertActionResult.getProcessType(), insertActionResult.getStatusCode());
    }

    private InsertActionRequest createIpasServicesInsertActionRequest(CProcessInsertActionRequest cActionRequest) {
        if (Objects.isNull(cActionRequest))
            return null;

        _int.wipo.ipas.ipasservices.CProcessId ipasProcessId = null;
        CProcessId processId = cActionRequest.getProcessId();
        if (Objects.nonNull(processId)) {
            ipasProcessId = new _int.wipo.ipas.ipasservices.CProcessId();
            ipasProcessId.setProcessType(processId.getProcessType());
            ipasProcessId.setProcessNbr(IpasDataUtils.generateIpasInteger(processId.getProcessNbr()));
        }

        return new InsertActionRequest(
                null,
                null,
                null,
                cActionRequest.getActionType(),
                ProcessActionUtils.changeMidnightActionDate(cActionRequest.getActionDate(), cActionRequest.getProcessId(), actionService),
                cActionRequest.getNotes1(),
                cActionRequest.getNotes2(),
                cActionRequest.getNotes3(),
                cActionRequest.getNotes4(),
                cActionRequest.getNotes5(),
                cActionRequest.getNotes(),
                cActionRequest.getResponsibleUser(),
                cActionRequest.getSpecialFinalStatus(),
                cActionRequest.getManualDueDate(),
                cActionRequest.getCaptureUser(),
                null,
                ipasProcessId);
    }

    private void deleteActionInIpas(CProcessId processId, CNextProcessAction nextProcessAction, CActionId actionId, Exception e) {
        if (Objects.isNull(actionId)) {
            log.error("Action for process " + processId + " with action type " + nextProcessAction.getActionType() + " from date " + DateUtils.formatDateTime(new Date()) + " is not deleted because new action id is empty. Please check and delete it manually !!");
            //TODO Write in new table for exceptions which need manual fix.
        } else {
            String reason = e.getMessage();
            if (Objects.isNull(reason)) {
                reason = DELETE_ACTION_DEFAULT_REASON;
            }
            boolean isDeleted = actionService.deleteAction(actionId, SecurityUtils.getLoggedUserId(), reason);
            if (!isDeleted) {
                //TODO Write in new table for exceptions which need manual fix.
            }
        }
    }

}