package bg.duosoft.ipas.core.mapper.reception;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocFiles;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * User: Georgi
 * Date: 28.5.2020 г.
 * Time: 16:16
 */
@Mapper(componentModel = "spring")
public abstract class ReceptionDocFilesMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion",                 constant = "1")
    @Mapping(target = "pk.docOri",                  source = "documentId.docOrigin")
    @Mapping(target = "pk.docLog",                  source = "documentId.docLog")
    @Mapping(target = "pk.docSer",                  source = "documentId.docSeries")
    @Mapping(target = "pk.docNbr",                  source = "documentId.docNbr")
    @Mapping(target = "pk.fileSeq",                 source = "fileId.fileSeq")
    @Mapping(target = "pk.fileTyp",                 source = "fileId.fileType")
    @Mapping(target = "pk.fileSer",                 source = "fileId.fileSeries")
    @Mapping(target = "pk.fileNbr",                 source = "fileId.fileNbr")
    public abstract IpDocFiles toEntity(CFileId fileId, CDocumentId documentId);

}
