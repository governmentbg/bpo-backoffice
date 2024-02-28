package bg.duosoft.ipas.core.mapper.process.event;

import bg.duosoft.ipas.core.mapper.document.DocumentIdMapper;
import bg.duosoft.ipas.core.mapper.document.DocumentSeqIdMapper;
import bg.duosoft.ipas.core.mapper.process.ProcessIdMapper;
import bg.duosoft.ipas.core.model.process.CUserdocProcessEvent;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.persistence.model.nonentity.ProcessEventResult;
import bg.duosoft.ipas.util.process.ProcessEventUtils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {
        ProcessIdMapper.class,
        DocumentIdMapper.class,
        DocumentSeqIdMapper.class,

})
public abstract class UserdocProcessEventMapper {

    @Autowired
    private StatusService statusService;

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Autowired
    private ProcessService processService;

    @Mapping(target = "filingDate", source = "documentFilingDate")
    @Mapping(target = "userdocProcessId", source = "processId")
    @Mapping(target = "upperProcessId", source = "upperProcessId")
    @Mapping(target = "documentId", source = "documentId")
    @Mapping(target = "documentSeqId", source = "documentSeqId")
    @Mapping(target = "applicantName", source = "applicantName")
    @Mapping(target = "notes", source = "userdocNotes")
    @Mapping(target = "externalSystemId", source = "externalSystemId")
    @Mapping(target = "expirationDate", source = "processExpirationDate")
    @BeanMapping(ignoreByDefault = true)
    public abstract CUserdocProcessEvent toCore(ProcessEventResult processEventResult);

    @AfterMapping
    protected void afterToCore(@MappingTarget CUserdocProcessEvent target, ProcessEventResult source) {
        target.setUserdocName(userdocTypesService.selectUserdocTypesMap().get(source.getUserdocTyp()));
        target.setStatus(statusService.getStatusMap().get(source.getProcessId().getProcessType() + "-" + source.getStatusCode()));
        target.setNextProcessActionAfterExpirationDate(processService.selectNextActionAfterExpirationDate(source.getProcessId(), source.getStatusCode()));
    }

}
