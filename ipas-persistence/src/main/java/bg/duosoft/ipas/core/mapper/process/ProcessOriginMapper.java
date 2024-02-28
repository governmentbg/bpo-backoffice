package bg.duosoft.ipas.core.mapper.process;

import bg.duosoft.ipas.core.mapper.document.DocumentIdMapper;
import bg.duosoft.ipas.core.mapper.file.FileIdMapper;
import bg.duosoft.ipas.core.mapper.offidoc.OffidocIdMapper;
import bg.duosoft.ipas.core.model.process.CProcessOriginData;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        ProcessIdMapper.class,
        FileIdMapper.class,
        DocumentIdMapper.class,
        OffidocIdMapper.class,
})
public abstract class ProcessOriginMapper {

    @Mapping(target = "relatedToWorkCode", source = "processType.relatedToWcode")
    @Mapping(target = "topProcessId.processNbr", source = "fileProcNbr")
    @Mapping(target = "topProcessId.processType", source = "fileProcTyp")
    @Mapping(target = "upperProcessId", source = "upperProc.pk")
    @Mapping(target = "fileId", source = "file.pk")
    @Mapping(target = "offidocId", source = "offidoc.pk")
    @Mapping(target = "documentId", source = "userdocIpDoc.pk")
    @Mapping(target = "userdocFileId", source = "userdocFile.pk")
    @Mapping(target = "applicationType", source = "applTyp")
    @Mapping(target = "userdocType", source = "userdocTyp.userdocTyp")
    @Mapping(target = "manualProcDescription", source = "manualProcDescription")
    @Mapping(target = "manualProcRef", source = "manualProcRef")
    @BeanMapping(ignoreByDefault = true)
    public abstract CProcessOriginData toCore(IpProc proc);

}
