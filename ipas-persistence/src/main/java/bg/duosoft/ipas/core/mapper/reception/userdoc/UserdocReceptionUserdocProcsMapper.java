package bg.duosoft.ipas.core.mapper.reception.userdoc;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocProcs;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * User: Georgi
 * Date: 9.6.2020 г.
 * Time: 15:57
 */
@Mapper(componentModel = "spring")
public abstract class UserdocReceptionUserdocProcsMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion",                 constant = "1")
    @Mapping(target = "pk.docOri",                  source = "documentId.docOrigin")
    @Mapping(target = "pk.docLog",                  source = "documentId.docLog")
    @Mapping(target = "pk.docSer",                  source = "documentId.docSeries")
    @Mapping(target = "pk.docNbr",                  source = "documentId.docNbr")
    @Mapping(target = "pk.userdocFileSeq",          source = "fileId.fileSeq")
    @Mapping(target = "pk.userdocFileTyp",          source = "fileId.fileType")
    @Mapping(target = "pk.userdocFileSer",          source = "fileId.fileSeries")
    @Mapping(target = "pk.userdocFileNbr",          source = "fileId.fileNbr")
    @Mapping(target = "pk.userdocTyp",              source = "reception.userdoc.userdocType")
    @Mapping(target = "procTyp",                    source = "processId.processType")
    @Mapping(target = "procNbr",                    source = "processId.processNbr")
    public abstract IpUserdocProcs toEntity(CReception reception, CFileId fileId, CDocumentId documentId, CProcessId processId);
}
