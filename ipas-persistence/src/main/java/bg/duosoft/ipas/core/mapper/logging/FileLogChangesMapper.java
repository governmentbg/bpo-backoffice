package bg.duosoft.ipas.core.mapper.logging;

import bg.duosoft.ipas.core.model.logging.CFileLogChanges;
import bg.duosoft.ipas.persistence.model.entity.IpFileLogChanges;
import org.mapstruct.*;

/**
 * User: ggeorgiev
 * Date: 06.12.2021
 * Time: 11:56
 */
@Mapper(componentModel = "spring")
public abstract class FileLogChangesMapper extends LogChangesMapperBase<IpFileLogChanges, CFileLogChanges> {

    @Mapping(target = "fileId.fileSeq",                     source = "pk.fileSeq")
    @Mapping(target = "fileId.fileType",                    source = "pk.fileTyp")
    @Mapping(target = "fileId.fileSeries",                  source = "pk.fileSer")
    @Mapping(target = "fileId.fileNbr",                     source = "pk.fileNbr")
    @Mapping(target = "changeDate", source = "changeDate")
    @Mapping(target = "changeNumber", source = "pk.logChangeNbr")
    @Mapping(target = "dataVersionCode", source = "dataVersionWcode")
    @Mapping(target = "dataCode", source = "dataCode")
    @BeanMapping(ignoreByDefault = true)
    public abstract CFileLogChanges toCore(IpFileLogChanges ipFileLogChanges, @Context boolean addData);

    @AfterMapping
    public void afterMapping(IpFileLogChanges ipFileLogChanges, @MappingTarget CFileLogChanges target, @Context boolean addData) {
        if (addData) {
            target.setDataValue(ipFileLogChanges.getDataValue());
            target.setChangeDetails(generateLogChangeDetails(ipFileLogChanges.getDataValue(), target.getFileId().createFilingNumber()));
        }
    }
}
