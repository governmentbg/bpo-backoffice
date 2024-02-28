package bg.duosoft.ipas.core.mapper.process;

import bg.duosoft.ipas.core.mapper.action.ActiontIdMapper;
import bg.duosoft.ipas.core.mapper.miscellaneous.SimpleUserMapper;
import bg.duosoft.ipas.core.mapper.miscellaneous.StatusMapper;
import bg.duosoft.ipas.core.mapper.miscellaneous.SubStatusMapper;
import bg.duosoft.ipas.core.mapper.process.event.ProcessEventMapper;
import bg.duosoft.ipas.core.model.action.CAction;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.process.CProcessEvent;
import bg.duosoft.ipas.core.model.process.CProcessOriginData;
import bg.duosoft.ipas.core.service.action.ActionService;
import bg.duosoft.ipas.core.service.impl.logging.diff.DiffGenerator;
import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfUserdocType;
import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidoc;
import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidocPK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcFreezes;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.persistence.model.nonentity.ProcessEventResult;
import bg.duosoft.ipas.persistence.repository.entity.process.IpProcRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {
        ProcessIdMapper.class,
        StatusMapper.class,
        SubStatusMapper.class,
        SimpleUserMapper.class,
        FreezingMapper.class,
        FrozenMapper.class
})
public abstract class ProcessMapper {
    @Autowired
    private ProcessOriginMapper processOriginMapper;

    @Autowired
    private IpProcRepository ipProcRepository;

    @Autowired
    private ProcessEventMapper processEventMapper;

    @Autowired
    private ActiontIdMapper actiontIdMapper;

    @Autowired
    private ActionService actionService;

    /*TODO
       String statusGroupName
       String description
     */
    @Mapping(target = "creationDate", source = "creationDate")
    @Mapping(target = "dueDate", source = "expirationDate")
    @Mapping(target = "statusDate", source = "statusDate")
    @Mapping(target = "processTypeName", source = "processType.procTypeName")
    @Mapping(target = "processId", source = "pk")
    @Mapping(target = "status", source = "statusCode")
    @Mapping(target = "substatus", source = "subStatusCode")
    @Mapping(target = "responsibleUser", source = "responsibleUser")
    @Mapping(target = "processFreezingList", source = "ipProcFreezingList")
    @Mapping(target = "processFrozenList", source = "ipProcFrozenList")
    @Mapping(target = "indSignaturePending", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipProc.getIndSignaturePending()))")
    @Mapping(target = "indFreezingJustEnded", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipProc.getIndFreezingJustEnded()))")
    @Mapping(target = "endFreezeFlag", source = "endFreezeFlag")
    @BeanMapping(ignoreByDefault = true)
    public abstract CProcess toCore(IpProc ipProc, @Context Boolean addProcessEvents);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<CProcess> toCoreList(List<IpProc> ipProcs, @Context Boolean addProcessEvents);

    @AfterMapping
    protected void afterToCore(@MappingTarget CProcess target, IpProc source, @Context Boolean addProcessEvents) {
        target.setProcessOriginData(processOriginMapper.toCore(source));
        if (Objects.nonNull(addProcessEvents) && addProcessEvents)
            createProcessEventList(target, source);
    }

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion", constant = "1")
    @Mapping(target = "processType.procTyp", source = "processId.processType")
    @Mapping(target = "indSignaturePending", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(cProcess.getIndSignaturePending()))")
    @Mapping(target = "indFreezingJustEnded", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(cProcess.getIndFreezingJustEnded()))")
    public abstract IpProc toEntity(CProcess cProcess);

    @AfterMapping
    protected void afterToEntity(@MappingTarget IpProc target, CProcess source) {
        CProcessOriginData processOriginData = source.getProcessOriginData();
        if (Objects.nonNull(processOriginData)) {
            target.setApplTyp(processOriginData.getApplicationType());

            String userdocType = processOriginData.getUserdocType();
            if (!StringUtils.isEmpty(userdocType)) {
                CfUserdocType cfUserdocType = new CfUserdocType();
                cfUserdocType.setUserdocTyp(userdocType);
                target.setUserdocTyp(cfUserdocType);
            }

            CProcessId topProcessId = processOriginData.getTopProcessId();
            if (Objects.nonNull(topProcessId)) {
                target.setFileProcNbr(topProcessId.getProcessNbr());
                target.setFileProcTyp(topProcessId.getProcessType());
            }

            CFileId fileId = processOriginData.getFileId();
            if (Objects.nonNull(fileId)) {
                IpFile ipFile = new IpFile();
                ipFile.setPk(new IpFilePK(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr()));
                target.setFile(ipFile);
            }

            CDocumentId documentId = processOriginData.getDocumentId();
            if (Objects.nonNull(documentId)) {
                IpDoc ipDoc = new IpDoc();
                ipDoc.setPk(new IpDocPK(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr()));
                target.setUserdocIpDoc(ipDoc);
            }

            CFileId userdocFileId = processOriginData.getUserdocFileId();
            if (Objects.nonNull(userdocFileId)) {
                IpFile userdocFile = new IpFile();
                userdocFile.setPk(new IpFilePK(userdocFileId.getFileSeq(), userdocFileId.getFileType(), userdocFileId.getFileSeries(), userdocFileId.getFileNbr()));
                target.setUserdocFile(userdocFile);
            }

            CProcessId upperProcessId = processOriginData.getUpperProcessId();
            if (Objects.nonNull(upperProcessId)) {
                IpProc upperProc = new IpProc();
                upperProc.setPk(new IpProcPK(upperProcessId.getProcessType(), upperProcessId.getProcessNbr()));
                target.setUpperProc(upperProc);
            }

            COffidocId offidocId = processOriginData.getOffidocId();
            if (Objects.nonNull(offidocId)) {
                IpOffidoc ipOffidoc = new IpOffidoc();
                ipOffidoc.setPk(new IpOffidocPK(offidocId.getOffidocOrigin(), offidocId.getOffidocSeries(), offidocId.getOffidocNbr()));
                target.setOffidoc(ipOffidoc);
            }

            target.setManualProcDescription(processOriginData.getManualProcDescription());
        }
    }

    @AfterMapping
    protected void fillProcessIds(@MappingTarget IpProc target, CProcess source) {
        String processType = source.getProcessId().getProcessType();
        Integer processNbr = source.getProcessId().getProcessNbr();

        List<IpProcFreezes> ipProcFreezingList = target.getIpProcFreezingList();
        if (!CollectionUtils.isEmpty(ipProcFreezingList)) {
            ipProcFreezingList.forEach(ipProcFreezes -> {
                ipProcFreezes.getPk().setFrozenProcNbr(processNbr);
                ipProcFreezes.getPk().setFrozenProcTyp(processType);
            });
        }

        List<IpProcFreezes> ipProcFrozenList = target.getIpProcFrozenList();
        if (!CollectionUtils.isEmpty(ipProcFrozenList)) {
            ipProcFrozenList.forEach(ipProcFreezes -> {
                ipProcFreezes.getPk().setFreezingProcNbr(processNbr);
                ipProcFreezes.getPk().setFreezingProcTyp(processType);
            });
        }
    }

    private void createProcessEventList(@MappingTarget CProcess target, IpProc source) {
        List<CProcessEvent> processEventList = new ArrayList<>();

        List<IpAction> ipActions = source.getIpActions();
        if (!CollectionUtils.isEmpty(ipActions)) {
            CAction lastInsertedAction = actionService.selectLastInsertedAction(source.getPk().getProcTyp(), source.getPk().getProcNbr());
            List<CProcessEvent> actionProcessEvents = processEventMapper.toCoreActionList(ipActions, Objects.isNull(lastInsertedAction) ? null : actiontIdMapper.toEntity(lastInsertedAction.getActionId()));
            if (!CollectionUtils.isEmpty(actionProcessEvents)) {
                for (CProcessEvent actionProcessEvent : actionProcessEvents) {
                    Boolean isActionDeletedFromOldIpas = actionProcessEvent.getEventAction().getIndDeleted();
                    if (Objects.isNull(isActionDeletedFromOldIpas) || !isActionDeletedFromOldIpas) {
                        processEventList.add(actionProcessEvent);
                    }
                }
            }
        }

        List<ProcessEventResult> upperProcesses = ipProcRepository.selectByUpperProcess(source.getPk().getProcTyp(),source.getPk().getProcNbr());
        if (!CollectionUtils.isEmpty(upperProcesses)) {
            List<ProcessEventResult> userdocProcesses = upperProcesses.stream().filter(object -> Objects.nonNull(object.getDocumentId())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(userdocProcesses))
                processEventList.addAll(processEventMapper.toCoreProcessEventResultList(userdocProcesses));

            List<ProcessEventResult> manualProcesses = upperProcesses.stream().filter(object -> Objects.isNull(object.getDocumentId()) && Objects.isNull(object.getFileId()) && Objects.isNull(object.getOffidocId())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(manualProcesses))
                processEventList.addAll(processEventMapper.toCoreProcessEventResultList(manualProcesses));
        }

        if (!CollectionUtils.isEmpty(processEventList)) {
            processEventList.sort(Comparator.comparing(CProcessEvent::getEventDate));//Sorting by event date
            target.setProcessEventList(processEventList);
        }
    }
}
