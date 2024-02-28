package bg.duosoft.ipas.core.mapper.file;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FileIdMapper {
    @Mappings({
            @Mapping(source = "fileSeq", target = "fileSeq"),
            @Mapping(source = "fileTyp", target = "fileType"),
            @Mapping(source = "fileSer", target = "fileSeries"),
            @Mapping(source = "fileNbr", target = "fileNbr"),
    })
    @BeanMapping(ignoreByDefault = true)
    CFileId toCore(IpFilePK ipFilePK);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    IpFilePK toEntity(CFileId fileId);


}
