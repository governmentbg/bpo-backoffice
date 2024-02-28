package bg.duosoft.ipas.test.mapper;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.mapper.MapperHelper;
import bg.duosoft.ipas.core.mapper.process.ProcessMapper;
import bg.duosoft.ipas.core.model.action.CActionType;
import bg.duosoft.ipas.core.model.action.CJournal;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.miscellaneous.CSubstatus;
import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.process.*;
import bg.duosoft.ipas.enums.ActionProcessEventStatus;
import bg.duosoft.ipas.enums.ProcessEventType;
import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import bg.duosoft.ipas.persistence.model.entity.action.IpActionPK;
import bg.duosoft.ipas.persistence.model.entity.action.IpJournal;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfActionType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfStatus;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfSubStatus;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfSubStatusPK;
import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidoc;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcFreezes;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.persistence.repository.entity.process.IpProcRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ProcessMapperTest extends MapperTestBase {
    @Autowired
    private IpProcRepository ipProcRepository;

    @Autowired
    private ProcessMapper processMapper;

    private IpProcPK ipProcPKMain;
    private IpProcPK ipProcPKOffidoc;
    private IpProcPK ipProcPKFreezing;
    private IpProcPK ipProcPKFrozen;
    private IpProcPK ipProcPKActionOffidoc;
    private IpProcPK ipProcMainWithManual;
    private IpProc originalProcess;
    private IpProc revertedProcess;
    private CProcess cProcess;

    @Before
    public void init() {
        ipProcPKMain = new IpProcPK("2", 121371);
        ipProcPKOffidoc = new IpProcPK("3", 531077);
        ipProcPKFreezing = new IpProcPK("2", 108084);
        ipProcPKFrozen = new IpProcPK("18", 764428);
        ipProcPKActionOffidoc = new IpProcPK("2", 534091);
        ipProcMainWithManual = new IpProcPK("2", 81324);

    }

    private void initCoreAndOriginalEntityObject(IpProcPK pk) {
        originalProcess = ipProcRepository.findById(pk).orElse(null);
        cProcess = processMapper.toCore(originalProcess, true);
        revertedProcess = processMapper.toEntity(cProcess);
    }

    @Test
    @Transactional
    public void transformIpProcToCProcessMainData() {
        initCoreAndOriginalEntityObject(ipProcPKMain);

        assertEquals(cProcess.getProcessId().getProcessNbr(), originalProcess.getPk().getProcNbr());
        assertEquals(cProcess.getProcessId().getProcessType(), originalProcess.getPk().getProcTyp());
        assertEquals(cProcess.getCreationDate(), originalProcess.getCreationDate());
        assertEquals(cProcess.getDueDate(), originalProcess.getExpirationDate());
        assertEquals(cProcess.getStatusDate(), originalProcess.getStatusDate());
        assertEquals(cProcess.getProcessTypeName(), originalProcess.getProcessType().getProcTypeName());
    }

    @Test
    @Transactional
    public void transformIpProcToCProcessStatusData() {
        initCoreAndOriginalEntityObject(ipProcPKMain);

        CStatus cStatus = cProcess.getStatus();
        CfStatus originalStatus = originalProcess.getStatusCode();
        assertEquals(cStatus.getStatusId().getProcessType(), originalStatus.getPk().getProcTyp());
        assertEquals(cStatus.getStatusId().getStatusCode(), originalStatus.getPk().getStatusCode());
        assertEquals(cStatus.getIndResponsibleRequired(), MapperHelper.getTextAsBoolean(originalStatus.getIndResponsibleReq()));
    }

    @Test
    @Transactional
    public void transformIpProcToCProcessSubStatusData() {
        IpProc databaseProcess = new IpProc();
        databaseProcess.setPk(ipProcPKMain);

        //Set random sub status for mapping test, because this DB table is empty !
        CfSubStatus cfSubStatus = new CfSubStatus();
        cfSubStatus.setSubStatusName("Test Sub Status");
        CfSubStatusPK cfSubStatusPK = new CfSubStatusPK();
        cfSubStatusPK.setProcTyp("2");
        cfSubStatusPK.setStatusCode("034");
        cfSubStatusPK.setSubStatusCode(112);
        cfSubStatus.setPk(cfSubStatusPK);
        databaseProcess.setSubStatusCode(cfSubStatus);
        CProcess cProcess = processMapper.toCore(databaseProcess, true);

        CSubstatus cSubstatus = cProcess.getSubstatus();
        CfSubStatus originalSubStatus = databaseProcess.getSubStatusCode();
        assertEquals(cSubstatus.getSubStatus().getSubstatusCode(), originalSubStatus.getPk().getSubStatusCode());
        assertEquals(cSubstatus.getSubStatus().getStatusId().getStatusCode(), originalSubStatus.getPk().getStatusCode());
        assertEquals(cSubstatus.getSubStatus().getStatusId().getProcessType(), originalSubStatus.getPk().getProcTyp());
        assertEquals(cSubstatus.getSubstatusName(), originalSubStatus.getSubStatusName());
    }

    @Test
    @Transactional
    public void transformIpProcToCProcessResponsibleUserData() {
        initCoreAndOriginalEntityObject(ipProcPKMain);

        IpUser originalUser = originalProcess.getResponsibleUser();
        CUser cResponsibleUser = cProcess.getResponsibleUser();
        assertEquals(originalUser.getUserId(), cResponsibleUser.getUserId());
        assertEquals(originalUser.getUserName(), cResponsibleUser.getUserName());
    }

    @Test
    @Transactional
    public void transformIpProcToCProcessFreezingData() {
        initCoreAndOriginalEntityObject(ipProcPKFreezing);

        List<IpProcFreezes> originalFreezingList = originalProcess.getIpProcFreezingList();
        List<CProcessFreezing> cFreezingList = cProcess.getProcessFreezingList();

        assertNotNull(originalFreezingList);
        assertNotNull(cFreezingList);
        assertEquals(originalFreezingList.size(), cFreezingList.size());

        for (int i = 0; i < originalFreezingList.size(); i++) {
            IpProcFreezes orignalFreezing = originalFreezingList.get(i);
            CProcessFreezing cFreezing = cFreezingList.get(i);

            assertEquals(orignalFreezing.getPk().getFreezingProcNbr(), cFreezing.getFreezingProcessId().getProcessNbr());
            assertEquals(orignalFreezing.getPk().getFreezingProcTyp(), cFreezing.getFreezingProcessId().getProcessType());
            assertEquals(orignalFreezing.getIndFreezeNoOffidoc(), MapperHelper.getBooleanAsText(cFreezing.getIndNoOffidoc()));
            assertEquals(orignalFreezing.getIndFreezeContinueWhenEnd(), MapperHelper.getBooleanAsText(cFreezing.getIndContinueWhenEnd()));
        }
    }

    @Test
    @Transactional
    public void transformIpProcToCProcessFrozenData() {
        initCoreAndOriginalEntityObject(ipProcPKFrozen);

        List<IpProcFreezes> originalFrozenList = originalProcess.getIpProcFrozenList();
        List<CProcessFrozen> cFrozenList = cProcess.getProcessFrozenList();

        assertNotNull(originalFrozenList);
        assertNotNull(cFrozenList);
        assertEquals(originalFrozenList.size(), cFrozenList.size());

        for (int i = 0; i < originalFrozenList.size(); i++) {
            IpProcFreezes orignalFrozen = originalFrozenList.get(i);
            CProcessFrozen cFrozen = cFrozenList.get(i);

            assertEquals(orignalFrozen.getPk().getFrozenProcNbr(), cFrozen.getFrozenProcessId().getProcessNbr());
            assertEquals(orignalFrozen.getPk().getFrozenProcTyp(), cFrozen.getFrozenProcessId().getProcessType());
            assertEquals(orignalFrozen.getIndFreezeNoOffidoc(), MapperHelper.getBooleanAsText(cFrozen.getIndNoOffidoc()));
            assertEquals(orignalFrozen.getIndFreezeContinueWhenEnd(), MapperHelper.getBooleanAsText(cFrozen.getIndContinueWhenEnd()));
        }
    }

    @Test
    @Transactional
    public void transformIpProcToCProcessOriginData() {
        initCoreAndOriginalEntityObject(ipProcPKMain);
        CProcessOriginData processOriginDataMainObject = cProcess.getProcessOriginData();
        assertEquals(processOriginDataMainObject.getRelatedToWorkCode(), originalProcess.getProcessType().getRelatedToWcode());
        assertEquals(processOriginDataMainObject.getTopProcessId().getProcessType(), originalProcess.getFileProcTyp());
        assertEquals(processOriginDataMainObject.getTopProcessId().getProcessNbr(), originalProcess.getFileProcNbr());
        assertEquals(processOriginDataMainObject.getFileId().getFileType(), originalProcess.getFile().getPk().getFileTyp());
        assertEquals(processOriginDataMainObject.getFileId().getFileNbr(), originalProcess.getFile().getPk().getFileNbr());
        assertEquals(processOriginDataMainObject.getFileId().getFileSeq(), originalProcess.getFile().getPk().getFileSeq());
        assertEquals(processOriginDataMainObject.getFileId().getFileSeries(), originalProcess.getFile().getPk().getFileSer());
        assertEquals(processOriginDataMainObject.getApplicationType(), originalProcess.getApplTyp());

        initCoreAndOriginalEntityObject(ipProcPKOffidoc);
        CProcessOriginData processOriginDataOffidoc = cProcess.getProcessOriginData();
        assertEquals(processOriginDataOffidoc.getRelatedToWorkCode(), originalProcess.getProcessType().getRelatedToWcode());
        assertEquals(processOriginDataOffidoc.getTopProcessId().getProcessType(), originalProcess.getFileProcTyp());
        assertEquals(processOriginDataOffidoc.getTopProcessId().getProcessNbr(), originalProcess.getFileProcNbr());
        assertEquals(processOriginDataOffidoc.getUpperProcessId().getProcessNbr(), originalProcess.getUpperProc().getPk().getProcNbr());
        assertEquals(processOriginDataOffidoc.getUpperProcessId().getProcessType(), originalProcess.getUpperProc().getPk().getProcTyp());
        assertEquals(processOriginDataOffidoc.getOffidocId().getOffidocNbr(), originalProcess.getOffidoc().getPk().getOffidocNbr());
        assertEquals(processOriginDataOffidoc.getOffidocId().getOffidocSeries(), originalProcess.getOffidoc().getPk().getOffidocSer());
        assertEquals(processOriginDataOffidoc.getOffidocId().getOffidocOrigin(), originalProcess.getOffidoc().getPk().getOffidocOri());

        initCoreAndOriginalEntityObject(ipProcPKFrozen);
        CProcessOriginData processOriginDataUserdoc = cProcess.getProcessOriginData();
        assertEquals(processOriginDataUserdoc.getRelatedToWorkCode(), originalProcess.getProcessType().getRelatedToWcode());
        assertEquals(processOriginDataUserdoc.getTopProcessId().getProcessType(), originalProcess.getFileProcTyp());
        assertEquals(processOriginDataUserdoc.getTopProcessId().getProcessNbr(), originalProcess.getFileProcNbr());
        assertEquals(processOriginDataUserdoc.getUpperProcessId().getProcessNbr(), originalProcess.getUpperProc().getPk().getProcNbr());
        assertEquals(processOriginDataUserdoc.getUpperProcessId().getProcessType(), originalProcess.getUpperProc().getPk().getProcTyp());
        assertEquals(processOriginDataUserdoc.getDocumentId().getDocNbr(), originalProcess.getUserdocIpDoc().getPk().getDocNbr());
        assertEquals(processOriginDataUserdoc.getDocumentId().getDocLog(), originalProcess.getUserdocIpDoc().getPk().getDocLog());
        assertEquals(processOriginDataUserdoc.getDocumentId().getDocOrigin(), originalProcess.getUserdocIpDoc().getPk().getDocOri());
        assertEquals(processOriginDataUserdoc.getDocumentId().getDocSeries(), originalProcess.getUserdocIpDoc().getPk().getDocSer());
        assertEquals(processOriginDataUserdoc.getUserdocType(), originalProcess.getUserdocTyp().getUserdocTyp());
        assertEquals(processOriginDataUserdoc.getUserdocFileId().getFileSeries(), originalProcess.getUserdocFile().getPk().getFileSer());
        assertEquals(processOriginDataUserdoc.getUserdocFileId().getFileSeq(), originalProcess.getUserdocFile().getPk().getFileSeq());
        assertEquals(processOriginDataUserdoc.getUserdocFileId().getFileNbr(), originalProcess.getUserdocFile().getPk().getFileNbr());
        assertEquals(processOriginDataUserdoc.getUserdocFileId().getFileType(), originalProcess.getUserdocFile().getPk().getFileTyp());

        assertEquals(processOriginDataUserdoc.getManualProcDescription(), originalProcess.getManualProcDescription());
        assertEquals(processOriginDataUserdoc.getManualProcRef(), originalProcess.getManualProcRef());
    }

    @Test
    @Transactional
    public void transformIpProcToCUserdocProcessEvent() {
        initCoreAndOriginalEntityObject(ipProcPKMain);
        List<IpProc> upperProcesses = ipProcRepository.findAllByUpperProc_Pk(originalProcess.getPk());
        List<CProcessEvent> processEventList = cProcess.getProcessEventList();
        List<IpProc> userdocProcesses = upperProcesses.stream()
                .filter(ipProc -> Objects.nonNull(ipProc.getUserdocIpDoc()))
                .collect(Collectors.toList());
        assertNotNull(userdocProcesses);

        List<CProcessEvent> userdocProcessEvents = processEventList.stream()
                .filter(cProcessEvent -> Objects.nonNull(cProcessEvent.getEventUserdoc()))
                .collect(Collectors.toList());
        assertNotNull(userdocProcessEvents);

        userdocProcessEvents.forEach(cProcessEvent -> {
            IpProc selectedProcess = userdocProcesses.stream()
                    .filter(ipProc ->
                            ipProc.getPk().getProcNbr().equals(cProcessEvent.getEventProcessId().getProcessNbr()) &&
                                    ipProc.getPk().getProcTyp().equals(cProcessEvent.getEventProcessId().getProcessType())
                    )
                    .findAny()
                    .orElse(null);
            assertNotNull(selectedProcess);
            assertNotNull(selectedProcess.getUserdocIpDoc());

            CUserdocProcessEvent eventUserdoc = cProcessEvent.getEventUserdoc();
            assertNotNull(eventUserdoc);
            assertEquals(selectedProcess.getUserdocIpDoc().getFilingDate(), eventUserdoc.getFilingDate());
            assertEquals(selectedProcess.getUserdocTyp().getUserdocName(), eventUserdoc.getUserdocName());
            assertEquals(selectedProcess.getStatusCode().getStatusName(), eventUserdoc.getStatus());
            assertEquals(selectedProcess.getPk().getProcTyp(), eventUserdoc.getUserdocProcessId().getProcessType());
            assertEquals(selectedProcess.getPk().getProcNbr(), eventUserdoc.getUserdocProcessId().getProcessNbr());
            assertEquals(selectedProcess.getUserdocIpDoc().getPk().getDocNbr(), eventUserdoc.getDocumentId().getDocNbr());
            assertEquals(selectedProcess.getUserdocIpDoc().getPk().getDocLog(), eventUserdoc.getDocumentId().getDocLog());
            assertEquals(selectedProcess.getUserdocIpDoc().getPk().getDocOri(), eventUserdoc.getDocumentId().getDocOrigin());
            assertEquals(selectedProcess.getUserdocIpDoc().getPk().getDocSer(), eventUserdoc.getDocumentId().getDocSeries());
            assertEquals(selectedProcess.getUserdocIpDoc().getDocSeqTyp().getDocSeqName(), eventUserdoc.getDocumentSeqId().getDocSeqName());
            assertEquals(selectedProcess.getUserdocIpDoc().getDocSeqTyp().getDocSeqTyp(), eventUserdoc.getDocumentSeqId().getDocSeqType());
            assertEquals(selectedProcess.getUserdocIpDoc().getDocSeqNbr(), eventUserdoc.getDocumentSeqId().getDocSeqNbr());
            assertEquals(selectedProcess.getUserdocIpDoc().getDocSeqSeries(), eventUserdoc.getDocumentSeqId().getDocSeqSeries());
            assertEquals(selectedProcess.getUserdocIpDoc().getFilingDate(), cProcessEvent.getEventDate());
            assertEquals(selectedProcess.getUserdocTyp().getUserdocName(), cProcessEvent.getEventDescription());
            assertEquals(ProcessEventType.USERDOC.code(), cProcessEvent.getEventType());
            assertEquals(selectedProcess.getUserdocTyp().getUserdocTyp(), cProcessEvent.getEventTypeCode());
            if (Objects.nonNull(selectedProcess.getResponsibleUser()))
                assertEquals(selectedProcess.getResponsibleUser().getUserName(), cProcessEvent.getEventUser());
            assertEquals(selectedProcess.getStatusCode().getStatusName(), cProcessEvent.getEventStatus());
            assertEquals(selectedProcess.getPk().getProcNbr(), cProcessEvent.getEventProcessId().getProcessNbr());
            assertEquals(selectedProcess.getPk().getProcTyp(), cProcessEvent.getEventProcessId().getProcessType());
        });

    }

    @Test
    @Transactional
    public void transformIpProcToCActionProcessEvent() {
        initCoreAndOriginalEntityObject(ipProcPKMain);
        List<CProcessEvent> processEventList = cProcess.getProcessEventList();
        List<IpAction> ipActions = originalProcess.getIpActions();
        ipActions.forEach(ipAction -> {
            IpActionPK actionPK = ipAction.getPk();
            CProcessEvent cActionProcessEvent = processEventList.stream()
                    .filter(cProcessEvent -> Objects.nonNull(cProcessEvent.getEventAction()))
                    .filter(cProcessEvent -> cProcessEvent.getEventAction().getActionId().getActionNbr().equals(actionPK.getActionNbr()) &&
                            cProcessEvent.getEventAction().getActionId().getProcessId().getProcessNbr().equals(actionPK.getProcNbr()) &&
                            cProcessEvent.getEventAction().getActionId().getProcessId().getProcessType().equals(actionPK.getProcTyp()))
                    .findAny().orElse(null);
            assertTrue(Objects.nonNull(cActionProcessEvent));

            CActionProcessEvent eventAction = cActionProcessEvent.getEventAction();
            assertNotNull(eventAction);
            assertEquals(ipAction.getActionDate(), eventAction.getActionDate());
            assertEquals(ipAction.getCaptureDate(), eventAction.getCaptureDate());
            assertEquals(ipAction.getActionNotes(), eventAction.getNotes());
            assertEquals(ipAction.getNotes1(), eventAction.getNotes1());
            assertEquals(ipAction.getNotes2(), eventAction.getNotes2());
            assertEquals(ipAction.getNotes3(), eventAction.getNotes3());
            assertEquals(ipAction.getNotes4(), eventAction.getNotes4());
            assertEquals(ipAction.getNotes5(), eventAction.getNotes5());
            assertEquals(ipAction.getIndCancelled(), MapperHelper.getBooleanAsText(eventAction.getIndCancelled()));
            assertEquals(ipAction.getIndSignaturePending(), MapperHelper.getBooleanAsText(eventAction.getIndSignaturePending()));
            assertEquals(ipAction.getIndSignaturePending(), MapperHelper.getBooleanAsText(eventAction.getIndSignaturePending()));
            checkUserMapping(ipAction.getResponsibleUser(), eventAction.getResponsibleUser());
            checkUserMapping(ipAction.getResponsibleUser(), eventAction.getResponsibleUser());
            checkJournalMapping(ipAction.getJournal(), eventAction.getJournal());

            IpOffidoc ipOffidoc = ipAction.getIpOffidoc();
            if (Objects.nonNull(ipOffidoc)) {
                assertEquals(ipOffidoc.getPk().getOffidocNbr(), eventAction.getGeneratedOffidoc().getOffidocId().getOffidocNbr());
                assertEquals(ipOffidoc.getPk().getOffidocSer(), eventAction.getGeneratedOffidoc().getOffidocId().getOffidocSeries());
                assertEquals(ipOffidoc.getPk().getOffidocOri(), eventAction.getGeneratedOffidoc().getOffidocId().getOffidocOrigin());
                assertEquals(ipOffidoc.getOffidocTyp().getOffidocTyp(), eventAction.getGeneratedOffidoc().getOffidocType().getOffidocType());
                assertEquals(ipOffidoc.getOffidocTyp().getOffidocName(), eventAction.getGeneratedOffidoc().getOffidocType().getOffidocName());
                assertEquals(ipOffidoc.getPrintDate(), eventAction.getGeneratedOffidoc().getPrintDate());
                assertEquals(ipOffidoc.getOffidocProcNbr(), eventAction.getGeneratedOffidoc().getProcessId().getProcessNbr());
                assertEquals(ipOffidoc.getOffidocProcTyp(), eventAction.getGeneratedOffidoc().getProcessId().getProcessType());
            }
            assertEquals(ipAction.getIndChangesStatus(), MapperHelper.getBooleanAsText(eventAction.getIndChangesStatus()));
            checkStatusMapping(ipAction.getPriorStatusCode(), eventAction.getOldStatus());
            checkStatusMapping(ipAction.getNewStatusCode(), eventAction.getNewStatus());
            checkActionTypeMapping(ipAction.getActionTyp(), eventAction.getActionType());
            assertEquals(ipAction.getActionDate(), cActionProcessEvent.getEventDate());
            assertEquals(ipAction.getActionTyp().getActionTypeName(), cActionProcessEvent.getEventDescription());
            assertEquals(ipAction.getActionTyp().getActionTyp(), cActionProcessEvent.getEventTypeCode());

            Boolean isChangeStatus = MapperHelper.getTextAsBoolean(ipAction.getIndChangesStatus());
            if (isChangeStatus)
                assertEquals(ProcessEventType.ACTION.code(), cActionProcessEvent.getEventType());
            else
                assertEquals(ProcessEventType.NOTE.code(), cActionProcessEvent.getEventType());

            if (Objects.nonNull(ipAction.getCaptureUser()))
                assertEquals(ipAction.getCaptureUser().getUserName(), cActionProcessEvent.getEventUser());

            IpJournal journal = ipAction.getJournal();
            if (Objects.nonNull(journal)) {
                if (Objects.isNull(journal.getPublicationDate()))
                    assertEquals(ActionProcessEventStatus.PUBLISHED.name(), cActionProcessEvent.getEventStatus());
                else
                    assertEquals(ActionProcessEventStatus.NOT_PUBLISHED.name(), cActionProcessEvent.getEventStatus());

                assertEquals(journal.getPublicationDate(), cActionProcessEvent.getActionJournalDate());
                assertEquals(journal.getJournalCode(), cActionProcessEvent.getActionJournalCode());
            }
            assertEquals(ipAction.getPk().getProcNbr(), cActionProcessEvent.getEventProcessId().getProcessNbr());
            assertEquals(ipAction.getPk().getProcTyp(), cActionProcessEvent.getEventProcessId().getProcessType());
        });
    }

    @Test
    @Transactional
    public void transformIpProcToManualProcessEvent() {
        initCoreAndOriginalEntityObject(ipProcMainWithManual);
        List<IpProc> upperProcesses = ipProcRepository.findAllByUpperProc_Pk(originalProcess.getPk());
        List<CProcessEvent> processEventList = cProcess.getProcessEventList();
        List<IpProc> manualProcesses = upperProcesses.stream()
                .filter(ipProc -> Objects.isNull(ipProc.getUserdocIpDoc()) && Objects.isNull(ipProc.getOffidoc()) && Objects.isNull(ipProc.getFile()))
                .collect(Collectors.toList());
        assertNotNull(manualProcesses);

        List<CProcessEvent> manualProcessEvents = processEventList.stream()
                .filter(cProcessEvent -> Objects.isNull(cProcessEvent.getEventUserdoc()) && Objects.isNull(cProcessEvent.getEventAction()))
                .collect(Collectors.toList());
        assertNotNull(manualProcessEvents);

        manualProcessEvents.forEach(cProcessEvent -> {
            IpProc selectedManualProcess = manualProcesses.stream()
                    .filter(ipProc ->
                            ipProc.getPk().getProcNbr().equals(cProcessEvent.getEventProcessId().getProcessNbr()) &&
                                    ipProc.getPk().getProcTyp().equals(cProcessEvent.getEventProcessId().getProcessType())
                    )
                    .findAny()
                    .orElse(null);
            assertNotNull(selectedManualProcess);

            assertEquals(selectedManualProcess.getCreationDate(), cProcessEvent.getEventDate());
            assertEquals(selectedManualProcess.getProcessType().getProcTypeName(), cProcessEvent.getEventDescription());
            assertEquals(ProcessEventType.MANUAL.code(), cProcessEvent.getEventType());
            if (Objects.nonNull(selectedManualProcess.getResponsibleUser()))
                assertEquals(selectedManualProcess.getResponsibleUser().getUserName(), cProcessEvent.getEventUser());
            assertEquals(selectedManualProcess.getStatusCode().getStatusName(), cProcessEvent.getEventStatus());
            assertEquals(selectedManualProcess.getPk().getProcNbr(), cProcessEvent.getEventProcessId().getProcessNbr());
            assertEquals(selectedManualProcess.getPk().getProcTyp(), cProcessEvent.getEventProcessId().getProcessType());
        });
    }

    private void checkUserMapping(IpUser ipUser, CUser cUser) {
        if (Objects.nonNull(ipUser)) {
            assertEquals(cUser.getUserId(), ipUser.getUserId());
            assertEquals(cUser.getUserName(), ipUser.getUserName());
        }
    }

    private void checkJournalMapping(IpJournal ipJournal, CJournal cJournal) {
        if (Objects.nonNull(ipJournal)) {
            assertEquals(ipJournal.getJournalCode(), cJournal.getJournalCode());
            assertEquals(ipJournal.getJornalName(), cJournal.getJournalName());
            assertEquals(ipJournal.getNotificationDate(), cJournal.getNotificationDate());
            assertEquals(ipJournal.getPublicationDate(), cJournal.getPublicationDate());
            assertEquals(ipJournal.getIndClosed(), MapperHelper.getBooleanAsText(cJournal.getIndClosed()));
        }
    }

    private void checkStatusMapping(CfStatus cfStatus, CStatus cStatus) {
        if (Objects.nonNull(cfStatus)) {
            assertEquals(cStatus.getStatusName(), cfStatus.getStatusName());
            assertEquals(cStatus.getStatusId().getProcessType(), cfStatus.getPk().getProcTyp());
            assertEquals(cStatus.getStatusId().getStatusCode(), cfStatus.getPk().getStatusCode());
            assertEquals(MapperHelper.getBooleanAsText(cStatus.getIndResponsibleRequired()), cfStatus.getIndResponsibleReq());
        }
    }

    private void checkActionTypeMapping(CfActionType actionTyp, CActionType cActionType) {
        if (Objects.nonNull(actionTyp)) {
            assertEquals(actionTyp.getActionTyp(), cActionType.getActionType());
            assertEquals(actionTyp.getActionTypeName(), cActionType.getActionName());
            assertEquals(actionTyp.getNotes1Prompt(), cActionType.getNotes1Prompt());
            assertEquals(actionTyp.getNotes2Prompt(), cActionType.getNotes2Prompt());
            assertEquals(actionTyp.getNotes3Prompt(), cActionType.getNotes3Prompt());
            assertEquals(actionTyp.getNotes4Prompt(), cActionType.getNotes4Prompt());
            assertEquals(actionTyp.getNotes5Prompt(), cActionType.getNotes5Prompt());
            assertEquals(actionTyp.getRestrictApplTyp(), cActionType.getRestrictApplicationType());
            assertEquals(actionTyp.getRestrictApplSubtyp(), cActionType.getRestrictApplicationSubtype());
            assertEquals(actionTyp.getRestrictLawCode(), cActionType.getRestrictLawCode());
            assertEquals(actionTyp.getRestrictFileTyp(), cActionType.getRestrictFileType());
            assertEquals(actionTyp.getChk1Prompt(), cActionType.getChk1Prompt());
            assertEquals(actionTyp.getChk2Prompt(), cActionType.getChk2Prompt());
            assertEquals(actionTyp.getChk3Prompt(), cActionType.getChk3Prompt());
            assertEquals(actionTyp.getChk4Prompt(), cActionType.getChk4Prompt());
            assertEquals(actionTyp.getChk5Prompt(), cActionType.getChk5Prompt());
            assertEquals(actionTyp.getListCode2(), cActionType.getListCode2());
            assertEquals(actionTyp.getListCode3(), cActionType.getListCode3());
            if (Objects.nonNull(actionTyp.getList())) {
                assertEquals(actionTyp.getList().getListCode(), cActionType.getListCode());
                assertEquals(actionTyp.getList().getListName(), cActionType.getListName());
            }
        }
    }

    @Test
    @Transactional
    public void transformIpProcToCProcessAndRevertIt() {
        initCoreAndOriginalEntityObject(ipProcPKMain);
        assertEquals(originalProcess.getCreationDate(), revertedProcess.getCreationDate());
        assertEquals(originalProcess.getExpirationDate(), revertedProcess.getExpirationDate());
        assertEquals(originalProcess.getStatusDate(), revertedProcess.getStatusDate());
        assertEquals(originalProcess.getProcessType().getProcTypeName(), revertedProcess.getProcessType().getProcTypeName());
        assertEquals(originalProcess.getPk().getProcTyp(), revertedProcess.getPk().getProcTyp());
        assertEquals(originalProcess.getPk().getProcNbr(), revertedProcess.getPk().getProcNbr());

        assertEquals(originalProcess.getStatusCode().getStatusName(), revertedProcess.getStatusCode().getStatusName());
        assertEquals(originalProcess.getStatusCode().getPk().getProcTyp(), revertedProcess.getStatusCode().getPk().getProcTyp());
        assertEquals(originalProcess.getStatusCode().getPk().getStatusCode(), revertedProcess.getStatusCode().getPk().getStatusCode());
        assertEquals(originalProcess.getStatusCode().getIndResponsibleReq(), revertedProcess.getStatusCode().getIndResponsibleReq());

        if (Objects.nonNull(originalProcess.getSubStatusCode())) {
            assertEquals(originalProcess.getSubStatusCode().getSubStatusName(), revertedProcess.getSubStatusCode().getSubStatusName());
            assertEquals(originalProcess.getSubStatusCode().getPk().getProcTyp(), revertedProcess.getSubStatusCode().getPk().getProcTyp());
            assertEquals(originalProcess.getSubStatusCode().getPk().getStatusCode(), revertedProcess.getSubStatusCode().getPk().getStatusCode());
            assertEquals(originalProcess.getSubStatusCode().getPk().getSubStatusCode(), revertedProcess.getSubStatusCode().getPk().getSubStatusCode());
        }

        if (Objects.nonNull(originalProcess.getResponsibleUser())) {
            assertEquals(originalProcess.getResponsibleUser().getUserName(), revertedProcess.getResponsibleUser().getUserName());
            assertEquals(originalProcess.getResponsibleUser().getUserId(), revertedProcess.getResponsibleUser().getUserId());
        }

        initCoreAndOriginalEntityObject(ipProcPKFreezing);
        if (Objects.nonNull(originalProcess.getIpProcFreezingList())) {
            assertEquals(originalProcess.getIpProcFreezingList().size(), revertedProcess.getIpProcFreezingList().size());
            originalProcess.getIpProcFreezingList().forEach(ipProcFreezes -> {
                IpProcFreezes matchFreezing = revertedProcess.getIpProcFreezingList().stream()
                        .filter(ipProcFreezes1 -> ipProcFreezes1.getPk().getFreezingProcNbr().equals(ipProcFreezes.getPk().getFreezingProcNbr()) &&
                                ipProcFreezes1.getPk().getFreezingProcTyp().equals(ipProcFreezes.getPk().getFreezingProcTyp()))
                        .findAny().orElse(null);
                assertNotNull(matchFreezing);

                assertEquals(ipProcFreezes.getIndFreezeContinueWhenEnd(), matchFreezing.getIndFreezeContinueWhenEnd());
                assertEquals(ipProcFreezes.getIndFreezeNoOffidoc(), matchFreezing.getIndFreezeNoOffidoc());
            });
        }


        if (Objects.nonNull(originalProcess.getIpProcFrozenList())) {
            assertEquals(originalProcess.getIpProcFrozenList().size(), revertedProcess.getIpProcFrozenList().size());
            originalProcess.getIpProcFrozenList().forEach(ipProcFreezes -> {
                IpProcFreezes matchFrozen = revertedProcess.getIpProcFrozenList().stream()
                        .filter(ipProcFreezes1 -> ipProcFreezes1.getPk().getFrozenProcNbr().equals(ipProcFreezes.getPk().getFrozenProcNbr()) &&
                                ipProcFreezes1.getPk().getFrozenProcTyp().equals(ipProcFreezes.getPk().getFrozenProcTyp()))
                        .findAny().orElse(null);
                assertNotNull(matchFrozen);

                assertEquals(ipProcFreezes.getIndFreezeContinueWhenEnd(), matchFrozen.getIndFreezeContinueWhenEnd());
                assertEquals(ipProcFreezes.getIndFreezeNoOffidoc(), matchFrozen.getIndFreezeNoOffidoc());
            });
        }

    }

}
