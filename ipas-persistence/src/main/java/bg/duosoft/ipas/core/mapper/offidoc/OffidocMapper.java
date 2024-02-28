package bg.duosoft.ipas.core.mapper.offidoc;

import bg.duosoft.ipas.core.mapper.process.ProcessSimpleMapper;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.offidoc.COffidocAbdocsDocument;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.service.ext.OffidocAbdocsDocumentService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidoc;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Mapper(componentModel = "spring", uses = {
        OffidocIdMapper.class,
        OffidocTypeMapper.class,
        ProcessSimpleMapper.class
})
public abstract class OffidocMapper {

    @Autowired
    private OffidocPublishedDecisionMapper offidocPublishedDecisionMapper;

    @Autowired
    private ProcessService processService;

    @Autowired
    private OffidocAbdocsDocumentService offidocAbdocsDocumentService;

    @Mapping(source = "pk", target = "offidocId")
    @Mapping(source = "offidocProcTyp", target = "processId.processType")
    @Mapping(source = "offidocProcNbr", target = "processId.processNbr")
    @Mapping(source = "printDate", target = "printDate")
    @Mapping(source = "offidocTyp", target = "offidocType")
    @Mapping(source = "procTyp", target = "actionId.processId.processType")
    @Mapping(source = "procNbr", target = "actionId.processId.processNbr")
    @Mapping(source = "actionNbr", target = "actionId.actionNbr")
    @Mapping(source = "ipProcSimple", target = "processSimpleData")
    @BeanMapping(ignoreByDefault = true)
    public abstract COffidoc toCore(IpOffidoc ipOffidoc);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract IpOffidoc toEntity(COffidoc cOffidoc);

    @AfterMapping
    protected void afterToCore(@MappingTarget COffidoc target, IpOffidoc source) {
        CProcessParentData cProcessParentData = processService.generateProcessParentHierarchy(target.getProcessId());
        target.setOffidocParentData(cProcessParentData);

        int upperProcessesCount = processService.selectUpperProcessesCount(target.getProcessId().getProcessType(), target.getProcessId().getProcessNbr());
        target.setHasChildren(upperProcessesCount != 0);

        COffidocAbdocsDocument cOffidocAbdocsDocument = offidocAbdocsDocumentService.selectById(target.getOffidocId());
        if (Objects.nonNull(cOffidocAbdocsDocument)) {
            target.setExternalSystemId(cOffidocAbdocsDocument.getRegistrationNumber());
            target.setAbdocsDocumentId(cOffidocAbdocsDocument.getAbdocsDocumentId());
        }

        if (Objects.nonNull(source.getPublishedDecision())){
            target.setPublishedDecision(offidocPublishedDecisionMapper.toCore(source.getPublishedDecision(),false));
        }
    }

}
