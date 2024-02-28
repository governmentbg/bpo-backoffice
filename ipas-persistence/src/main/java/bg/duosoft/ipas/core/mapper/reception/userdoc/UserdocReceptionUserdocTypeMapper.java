package bg.duosoft.ipas.core.mapper.reception.userdoc;

import bg.duosoft.ipas.core.mapper.reception.ReceptionMapperHelper;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocTypes;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * User: Georgi
 * Date: 8.6.2020 г.
 * Time: 17:56
 */
@Mapper(componentModel = "spring", uses = {ReceptionMapperHelper.class})
public abstract class UserdocReceptionUserdocTypeMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion",             constant = "1")
    @Mapping(target = "userdocType.userdocTyp", source = "reception.userdoc.userdocType")
    @Mapping(target = "pk.userdocTyp",         source = "reception.userdoc.userdocType")
    @Mapping(target = "pk.docOri",              source = "documentId.docOrigin")
    @Mapping(target = "pk.docLog",              source = "documentId.docLog")
    @Mapping(target = "pk.docSer",              source = "documentId.docSeries")
    @Mapping(target = "pk.docNbr",              source = "documentId.docNbr")
    @Mapping(target = "indProcessFirst",        constant = "S")
    public abstract IpUserdocTypes toEntity(CReception reception, CDocumentId documentId);
}
