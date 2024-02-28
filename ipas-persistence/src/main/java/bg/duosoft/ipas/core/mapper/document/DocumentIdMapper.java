package bg.duosoft.ipas.core.mapper.document;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentIdMapper {

    @Mapping(source = "docSer", target = "docSeries")
    @Mapping(source = "docOri", target = "docOrigin")
    @Mapping(source = "docNbr", target = "docNbr")
    @Mapping(source = "docLog", target = "docLog")
    @BeanMapping(ignoreByDefault = true)
    CDocumentId toCore(IpDocPK ipDocPK);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    IpDocPK toEntity(CDocumentId cDocumentId);


}
