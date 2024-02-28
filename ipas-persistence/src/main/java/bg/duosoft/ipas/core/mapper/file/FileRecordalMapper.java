package bg.duosoft.ipas.core.mapper.file;

import bg.duosoft.ipas.core.model.file.CFileRecordal;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpFileRecordal;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpFileRecordalPK;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public abstract class FileRecordalMapper {

    @Autowired
    private DocService docService;

    @Mapping(source = "pk.fileSeq", target = "fileId.fileSeq")
    @Mapping(source = "pk.fileTyp", target = "fileId.fileType")
    @Mapping(source = "pk.fileSer", target = "fileId.fileSeries")
    @Mapping(source = "pk.fileNbr", target = "fileId.fileNbr")
    @Mapping(source = "pk.docOri", target = "documentId.docOrigin")
    @Mapping(source = "pk.docLog", target = "documentId.docLog")
    @Mapping(source = "pk.docSer", target = "documentId.docSeries")
    @Mapping(source = "pk.docNbr", target = "documentId.docNbr")
    @Mapping(source = "procTyp", target = "actionId.processId.processType")
    @Mapping(source = "procNbr", target = "actionId.processId.processNbr")
    @Mapping(source = "actionNbr", target = "actionId.actionNbr")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "invalidationDocOri", target = "invalidationDocumentId.docOrigin")
    @Mapping(source = "invalidationDocLog", target = "invalidationDocumentId.docLog")
    @Mapping(source = "invalidationDocSer", target = "invalidationDocumentId.docSeries")
    @Mapping(source = "invalidationDocNbr", target = "invalidationDocumentId.docNbr")
    @Mapping(source = "invalidationDate", target = "invalidationDate")
    @Mapping(source = "invalidationProcTyp", target = "invalidationActionId.processId.processType")
    @Mapping(source = "invalidationProcNbr", target = "invalidationActionId.processId.processNbr")
    @Mapping(source = "invalidationActionNbr", target = "invalidationActionId.actionNbr")
    @BeanMapping(ignoreByDefault = true)
    public abstract CFileRecordal toCore(IpFileRecordal ipFileRecordal);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract IpFileRecordal toEntity(CFileRecordal fileRecordal);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<IpFileRecordal> toEntityList(List<CFileRecordal> fileRecordals);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<CFileRecordal> toCoreList(List<IpFileRecordal> ipFileRecordals);

    @AfterMapping
    protected void afterToCore(IpFileRecordal source, @MappingTarget CFileRecordal target) {
        IpFileRecordalPK pk = source.getPk();
        String externalSystemId = docService.selectExternalSystemId(pk.getDocOri(), pk.getDocLog(), pk.getDocSer(), pk.getDocNbr());
        if(!StringUtils.isEmpty(externalSystemId)){
            target.setExternalSystemId(externalSystemId);
        }

        Integer invalidationDocNbr = source.getInvalidationDocNbr();
        if(Objects.nonNull(invalidationDocNbr)){
            String invalidationExternalSystemId = docService.selectExternalSystemId(source.getInvalidationDocOri(), source.getInvalidationDocLog(), source.getInvalidationDocSer(), source.getInvalidationDocNbr());
            if(!StringUtils.isEmpty(invalidationExternalSystemId)){
                target.setInvalidationExternalSystemId(invalidationExternalSystemId);
            }
        }

    }

}
