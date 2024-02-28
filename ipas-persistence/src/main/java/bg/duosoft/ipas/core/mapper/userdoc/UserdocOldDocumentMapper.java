package bg.duosoft.ipas.core.mapper.userdoc;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.userdoc.CUserdocOldDocument;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocOldDocument;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserdocOldDocumentMapper extends BaseObjectMapper<IpUserdocOldDocument, CUserdocOldDocument> {

    @Mapping(source = "fileSeq", target = "fileSeq")
    @Mapping(source = "fileTyp", target = "fileType")
    @Mapping(source = "fileSer", target = "fileSeries")
    @Mapping(source = "fileNbr", target = "fileNbr")
    @Mapping(source = "externalSystemId", target = "externalSystemId")
    @Mapping(source = "filingDate", target = "filingDate")
    @Mapping(source = "newUserdocType", target = "newUserdocType")
    @Mapping(source = "responsibleUserId", target = "responsibleUserId")
    @Mapping(source = "registerInAbdocs", target = "registerInAbdocs")
    @BeanMapping(ignoreByDefault = true)
    public abstract CUserdocOldDocument toCore(IpUserdocOldDocument ipUserdocOldDocument);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<CUserdocOldDocument> toCoreList(List<IpUserdocOldDocument> ipUserdocOldDocuments);
}
