package bg.duosoft.ipas.core.mapper.userdoc;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.mapper.MapperHelper;
import bg.duosoft.ipas.core.model.mark.CMarkAttachment;
import bg.duosoft.ipas.core.model.userdoc.CUserdocHierarchyNode;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.persistence.model.entity.mark.IpLogo;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocHierarchyChildNode;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

/**
 * User: ggeorgiev
 * Date: 28.01.2021
 * Time: 12:36
 */
@Mapper(componentModel = "spring")
public abstract class UserdocHierarchyMapper extends BaseObjectMapper<UserdocHierarchyChildNode, CUserdocHierarchyNode> {
    @Autowired
    private UserdocTypesService userdocTypesService;
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "documentId.docOrigin", source = "docOri")
    @Mapping(target = "documentId.docLog", source = "docLog")
    @Mapping(target = "documentId.docSeries", source = "docSer")
    @Mapping(target = "documentId.docNbr", source = "docNbr")
    @Mapping(target = "documentSeqId.docSeqType", source = "docSeqTyp")
    @Mapping(target = "documentSeqId.docSeqNbr", source = "docSeqNbr")
    @Mapping(target = "documentSeqId.docSeqSeries", source = "docSeqSeries")
    @Mapping(target = "processId.processType", source = "procTyp")
    @Mapping(target = "processId.processNbr", source = "procNbr")
    @Mapping(target = "upperProcessId.processType", source = "upperProcTyp")
    @Mapping(target = "upperProcessId.processNbr", source = "upperProcNbr")
    @Mapping(target = "userdocType", source = "userdocTyp")
    @Mapping(target = "externalSystemId", source = "externalSystemId")
    @Mapping(target = "filingDate", source = "filingDate")
    @Mapping(target = "fileId.fileSeq", source = "fileSeq")
    @Mapping(target = "fileId.fileType", source = "fileTyp")
    @Mapping(target = "fileId.fileSeries", source = "fileSer")
    @Mapping(target = "fileId.fileNbr", source = "fileNbr")
    @Mapping(target = "efilingUser", source = "efilingUser")
    @Mapping(target = "statusCode", source = "statusCode")
    @Mapping(target = "responsibleUserId", source = "responsibleUserId")
    public abstract CUserdocHierarchyNode toCore(UserdocHierarchyChildNode e);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CUserdocHierarchyNode> toCoreList(List<UserdocHierarchyChildNode> eList);

    @AfterMapping
    protected void afterToEntity(@MappingTarget CUserdocHierarchyNode target, UserdocHierarchyChildNode source) {
        target.setUserdocTypeName(userdocTypesService.selectUserdocTypesMap().get(source.getUserdocTyp()));
    }
}
