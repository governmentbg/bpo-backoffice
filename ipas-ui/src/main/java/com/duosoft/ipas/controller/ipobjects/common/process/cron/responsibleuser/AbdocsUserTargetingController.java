package com.duosoft.ipas.controller.ipobjects.common.process.cron.responsibleuser;

import bg.duosoft.abdocs.model.*;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.CConfigParam;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.process.CProcessResponsibleUserChange;
import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.model.userdoc.config.CUserdocTypeConfig;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.core.service.userdoc.config.UserdocTypeConfigService;
import bg.duosoft.ipas.enums.ProcessType;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.process.ProcessTypeUtils;
import com.duosoft.ipas.config.YAMLConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
public class AbdocsUserTargetingController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private AbdocsService abdocsServiceAdmin;

    @Autowired
    private DocService docService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigParamService configParamService;

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private UserdocTypeConfigService userdocTypeConfigService;

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Scheduled(fixedDelayString = "${ipas.properties.abdocs.responsibleUserChange.targeting.fixedDelay}")
    public void run() {
        log.info("ABDOCS user targeting cron start time is {}", DateUtils.TIME_FORMATTER_HOUR_MINUTE_SECOND.format(new Date()));
        executeTargeting();
        log.info("ABDOCS user targeting cron end time is {}", DateUtils.TIME_FORMATTER_HOUR_MINUTE_SECOND.format(new Date()));
    }

    private void executeTargeting() {
        List<CProcessResponsibleUserChange> responsibleUserChanges = processService.selectNotProcessedAbdocsUserTargeting();
        if (!CollectionUtils.isEmpty(responsibleUserChanges)) {
            responsibleUserChanges.forEach(this::insertDocAction);
        }
    }

    private void insertDocAction(CProcessResponsibleUserChange responsibleUserChange) {
        try {
            CProcess process = processService.selectProcess(new CProcessId(responsibleUserChange.getProcTyp(), responsibleUserChange.getProcNbr()), false);
            if (Objects.nonNull(process)) {
                CProcessId topProcess = process.getProcessOriginData().getTopProcessId();
                if (Objects.nonNull(topProcess)) {
                    if (allowInsertDocAction(process)) {
                        User newResponsibleUser = userService.getUser(responsibleUserChange.getNewResponsibleUserId());
                        User userChanged = userService.getUser(responsibleUserChange.getUserChanged());
                        Integer abdocsNewResponsibleUser = abdocsServiceAdmin.selectAbdocsUserIdByUsername(newResponsibleUser.getLogin());
                        Integer abdocsUserChanged = abdocsServiceAdmin.selectAbdocsUserIdByUsername(userChanged.getLogin());
                        if (Objects.nonNull(abdocsNewResponsibleUser) && Objects.nonNull(abdocsUserChanged)) {
                            Document abdocsDocument = selectAbdocsDocument(process);
                            if (Objects.nonNull(abdocsDocument)) {
                                DocActionRequest docActionRequest = createDocActionRequest(abdocsDocument, abdocsUserChanged, abdocsNewResponsibleUser);
                                abdocsServiceAdmin.insertDocAction(docActionRequest);
                                processService.updateAbdocsUserTargetingAsProcessed(responsibleUserChange.getProcTyp(), responsibleUserChange.getProcNbr(), responsibleUserChange.getChangeNbr());
                                log.info("User targeting has been processed successfully ! Document id: {}, Target user: {}", abdocsDocument.getDocId(), newResponsibleUser.getLogin());
                            } else {
                                markRecordAsProcessed(responsibleUserChange);
                                log.info("User targeting has been marked as processed, but it's not really processed, because ABDOCS document wasn't found ! Record: {}", responsibleUserChange.toString());
                            }
                        } else {
                            markRecordAsProcessed(responsibleUserChange);
                            log.info("User targeting has been marked as processed, but it's not really processed, because user wasn't found in ABDOCS ! Record: {}", responsibleUserChange.toString());
                        }
                    } else {
                        markRecordAsProcessed(responsibleUserChange);
                        log.info("User targeting has been marked as processed, but it's not really processed, because process type isn't part of allowed types or ABDOCS_USER_TARGETING_ON_RESPONSIBLE_USER_CHANGE is false for userdocs! Top Process: {}, Record:  {}", topProcess.toString(), responsibleUserChange.toString());
                    }
                }
            } else {
                markRecordAsProcessed(responsibleUserChange);
                log.info("User targeting has been marked as processed, but it's not really processed, because process wasn't found ! Record:  {}", responsibleUserChange.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void markRecordAsProcessed(CProcessResponsibleUserChange responsibleUserChange) {
        processService.updateAbdocsUserTargetingAsProcessed(responsibleUserChange.getProcTyp(), responsibleUserChange.getProcNbr(), responsibleUserChange.getChangeNbr());
    }

    private Document selectAbdocsDocument(CProcess process) {
        String registrationNumber = null;
        if (ProcessTypeUtils.isIpObjectProcess(process)) {
            registrationNumber = process.getProcessOriginData().getFileId().createFilingNumber();
        } else if (ProcessTypeUtils.isUserdocProcess(process)) {
            registrationNumber = docService.selectExternalSystemId(process.getProcessOriginData().getDocumentId());
        }
        if (StringUtils.isEmpty(registrationNumber)) {
            return null;
        }
        return abdocsServiceAdmin.selectDocumentByRegistrationNumber(registrationNumber);
    }

    private boolean allowInsertDocAction(CProcess process) {
        final int topProcessType = Integer.parseInt(process.getProcessOriginData().getTopProcessId().getProcessType());

        if (!(ProcessType.PATENT.code() == topProcessType || ProcessType.UTILITY_MODEL.code() == topProcessType || ProcessType.ACP.code() == topProcessType)) {
            return false;
        }

        if (Objects.nonNull(process.getProcessOriginData().getDocumentId())) {
            CUserdocType userdocType = userdocTypesService.selectUserdocTypeByDocId(process.getProcessOriginData().getDocumentId());
            CUserdocTypeConfig userdocTypeConfig = userdocTypeConfigService.findById(userdocType.getUserdocType());
            return userdocTypeConfig.getAbdocsUserTargetingOnResponsibleUserChange();
        }

        return true;
    }

    private DocActionRequest createDocActionRequest(Document document, Integer fromUser, Integer userId) {
        Date currentDate = new Date();

        DocUnit docUnit = new DocUnit();
        docUnit.setCreateDate(currentDate);
        docUnit.setDocUnitRole(DocUnitRole.TO.value());
        docUnit.setUnitId(userId);

        DocActionRequest request = new DocActionRequest();
        request.setDocId(document.getDocId());
        request.setCreateDate(currentDate);
        request.setType(DocActionType.Targeting.value());
        request.setAddRootDocPermissions(true);
        request.setUnitId(fromUser);
        request.setFromUnitId(fromUser);
        request.setNote(getNote());
        request.setExpectedResultId(DocActionExpectedResult.ExecutionTargeting.value());
        request.setDocUnits(Collections.singletonList(docUnit));
        return request;
    }

    private String getNote() {
        CConfigParam notesConfig = configParamService.selectExtConfigByCode(ConfigParamService.ABDOCS_NOTE_FOR_USER_TARGETING_ON_RESPONSIBLE_USER_CHANGE);
        if (Objects.isNull(notesConfig)) {
            throw new RuntimeException("Cannot find ABDOCS_NOTE_FOR_USER_TARGETING_ON_RESPONSIBLE_USER_CHANGE config !");
        }
        return notesConfig.getValue();
    }
}
