package bg.duosoft.ipas.core.mapper.common;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.persistence.model.entity.FileSeqTypSerNbrPK;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FileSeqTypSerNbrMapper {
    @Mapping(source = "fileSeries", target = "fileSer")
    @Mapping(source = "fileType", target = "fileTyp")
    @Mapping(source = "fileNbr", target = "fileNbr")
    @Mapping(source = "fileSeq", target = "fileSeq")
    @BeanMapping(ignoreByDefault = true)
    void toEntity(CFileId fileId, @MappingTarget FileSeqTypSerNbrPK filePK);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    void toCore(FileSeqTypSerNbrPK filePk, @MappingTarget CFileId fileId);


}
