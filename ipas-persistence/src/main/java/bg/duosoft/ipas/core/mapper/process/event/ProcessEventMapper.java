package bg.duosoft.ipas.core.mapper.process.event;

import bg.duosoft.ipas.core.mapper.MapperHelper;
import bg.duosoft.ipas.core.mapper.process.ProcessIdMapper;
import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.core.model.process.CActionProcessEvent;
import bg.duosoft.ipas.core.model.process.CProcessEvent;
import bg.duosoft.ipas.core.model.process.CUserdocProcessEvent;
import bg.duosoft.ipas.core.service.nomenclature.ProcessTypeService;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.enums.ActionProcessEventStatus;
import bg.duosoft.ipas.enums.ProcessEventType;
import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import bg.duosoft.ipas.persistence.model.entity.action.IpActionPK;
import bg.duosoft.ipas.persistence.model.entity.action.IpJournal;
import bg.duosoft.ipas.persistence.model.nonentity.ProcessEventResult;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring", uses = {
        ProcessIdMapper.class
})
public abstract class ProcessEventMapper {

    @Autowired
    private ActionProcessEventMapper actionProcessEventMapper;

    @Autowired
    private UserdocProcessEventMapper userdocProcessEventMapper;

    @Autowired
    private StatusService statusService;

    @Autowired
    private ProcessTypeService processTypeService;

    @Autowired
    private UserdocTypesService userdocTypesService;

    /*TODO
        String eventNotes
        Boolean indEventProcessPending
   */
    @Mapping(target = "eventDate", source = "actionDate")
    @Mapping(target = "eventDescription", source = "actionTyp.actionTypeName")
    @Mapping(target = "eventTypeCode", source = "actionTyp.actionTyp")
    @Mapping(target = "eventUser", source = "captureUser.userName")
    @Mapping(target = "eventProcessId", source = "ipProc.pk")
    @Mapping(target = "actionJournalDate", source = "journal.publicationDate")
    @Mapping(target = "actionJournalCode", source = "journal.journalCode")
    @BeanMapping(ignoreByDefault = true)
    public abstract CProcessEvent toCore(IpAction ipAction, @Context IpActionPK lastProcessAction);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<CProcessEvent> toCoreActionList(List<IpAction> ipActions, @Context IpActionPK lastProcessAction);

    @AfterMapping
    protected void afterToCore(@MappingTarget CProcessEvent target, IpAction source, @Context IpActionPK lastProcessAction) {
        CActionProcessEvent cActionProcessEvent = actionProcessEventMapper.toCore(source);
        if (Objects.nonNull(lastProcessAction)) {
            CActionId actionId = cActionProcessEvent.getActionId();
            if (Objects.equals(actionId.getActionNbr(), lastProcessAction.getActionNbr())
                    && Objects.equals(actionId.getProcessId().getProcessNbr(), lastProcessAction.getProcNbr())
                    && actionId.getProcessId().getProcessType().equals(lastProcessAction.getProcTyp())) {
                cActionProcessEvent.setLastActionInProcess(true);
            }
        }
        target.setEventAction(cActionProcessEvent);

        Boolean isChangeStatus = MapperHelper.getTextAsBoolean(source.getIndChangesStatus());
        if (isChangeStatus)
            target.setEventType(ProcessEventType.ACTION.code());
        else
            target.setEventType(ProcessEventType.NOTE.code());

        IpJournal journal = source.getJournal();
        if (Objects.nonNull(journal)) {
            if (Objects.isNull(journal.getPublicationDate()))
                target.setEventStatus(ActionProcessEventStatus.PUBLISHED.name());
            else
                target.setEventStatus(ActionProcessEventStatus.NOT_PUBLISHED.name());
        }

    }

    @Mapping(target = "eventUser", source = "responsibleUserName")
    @Mapping(target = "eventProcessId", source = "processId")
    @BeanMapping(ignoreByDefault = true)
    public abstract CProcessEvent toCore(ProcessEventResult processEventResult);

    @BeanMapping(ignoreByDefault = true)
    public abstract List<CProcessEvent> toCoreProcessEventResultList(List<ProcessEventResult> processEventResults);

    @AfterMapping
    protected void afterToCore(@MappingTarget CProcessEvent target, ProcessEventResult source) {
        CFileId fileId = source.getFileId();
        CDocumentId documentId = source.getDocumentId();
        COffidocId offidocId = source.getOffidocId();
        if (Objects.isNull(fileId) && Objects.isNull(documentId) && Objects.isNull(offidocId)) {//Manual process
            target.setEventDate(source.getProcessCreationDate());
            target.setEventDescription(processTypeService.getProcessTypeMap().get(source.getProcessId().getProcessType()));
            target.setEventType(ProcessEventType.MANUAL.code());
        } else if (Objects.nonNull(documentId)) {
            String userdocTyp = source.getUserdocTyp();
            CUserdocProcessEvent userdocProcessEvent = userdocProcessEventMapper.toCore(source);

            target.setEventDate(source.getProcessCreationDate());
            target.setEventDescription(Objects.isNull(userdocTyp) ? null : userdocTypesService.selectUserdocTypesMap().get(userdocTyp));
            target.setEventType(ProcessEventType.USERDOC.code());
            target.setEventTypeCode(Objects.isNull(userdocTyp) ? null : userdocTyp);
            target.setEventUserdoc(userdocProcessEvent);
        }
        target.setEventStatus(statusService.getStatusMap().get(source.getProcessId().getProcessType() + "-" + source.getStatusCode()));
    }

}
