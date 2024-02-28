package bg.duosoft.ipas.core.mapper.file;


import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CRelationship;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationship;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring")
public abstract class Relationship1Mapper {

    @Autowired
    private StatusService statusService;

    @Mapping(target = "relationshipRole", constant = "1")
    @Mapping(target = "relationshipType", source = "pk.relationshipTyp")
    @Mapping(target = "fileId.fileSeq", source = "pk.fileSeq2")
    @Mapping(target = "fileId.fileType", source = "pk.fileTyp2")
    @Mapping(target = "fileId.fileSeries", source = "pk.fileSer2")
    @Mapping(target = "fileId.fileNbr", source = "pk.fileNbr2")
    @BeanMapping(ignoreByDefault = true)
    public abstract CRelationship toCore(IpFileRelationship relationship);


    @Mapping(target = "pk.fileSeq1", source = "fileId.fileSeq")
    @Mapping(target = "pk.fileTyp1", source = "fileId.fileType")
    @Mapping(target = "pk.fileSer1", source = "fileId.fileSeries")
    @Mapping(target = "pk.fileNbr1", source = "fileId.fileNbr")
    @Mapping(target = "pk.fileSeq2", source = "relationship.fileId.fileSeq")
    @Mapping(target = "pk.fileTyp2", source = "relationship.fileId.fileType")
    @Mapping(target = "pk.fileSer2", source = "relationship.fileId.fileSeries")
    @Mapping(target = "pk.fileNbr2", source = "relationship.fileId.fileNbr")
    @Mapping(target = "pk.relationshipTyp", source = "relationship.relationshipType")
    @Mapping(target = "relationshipType.relationshipTyp", source = "relationship.relationshipType")
    @Mapping(target = "rowVersion", constant = "1")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpFileRelationship toEntity(CRelationship relationship, CFileId fileId);

}
