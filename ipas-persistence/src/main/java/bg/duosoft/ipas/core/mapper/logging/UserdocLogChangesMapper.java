package bg.duosoft.ipas.core.mapper.logging;

import bg.duosoft.ipas.core.model.logging.CUserdocLogChanges;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocLogChanges;
import org.mapstruct.*;

/**
 * User: ggeorgiev
 * Date: 06.12.2021
 * Time: 13:12
 */
@Mapper(componentModel = "spring")
public abstract class UserdocLogChangesMapper extends LogChangesMapperBase<IpUserdocLogChanges, CUserdocLogChanges> {
    @Mapping(target = "documentId.docOrigin",   source = "pk.docOri")
    @Mapping(target = "documentId.docLog",      source = "pk.docLog")
    @Mapping(target = "documentId.docSeries",   source = "pk.docSer")
    @Mapping(target = "documentId.docNbr",      source = "pk.docNbr")
    @Mapping(target = "changeDate", source = "changeDate")
    @Mapping(target = "changeNumber", source = "pk.logChangeNbr")
    @Mapping(target = "dataVersionCode", source = "dataVersionWcode")
    @Mapping(target = "dataCode", source = "dataCode")
    @BeanMapping(ignoreByDefault = true)
    public abstract CUserdocLogChanges toCore(IpUserdocLogChanges ipLogChanges, @Context boolean addData);

    @AfterMapping
    public void afterMapping(IpUserdocLogChanges ipLogChanges, @MappingTarget CUserdocLogChanges target, @Context boolean addData) {
        if (addData) {
            target.setDataValue(ipLogChanges.getDataValue());
            target.setChangeDetails(generateLogChangeDetails(ipLogChanges.getDataValue(), target.getDocumentId().createFilingNumber()));
        }
    }

}
