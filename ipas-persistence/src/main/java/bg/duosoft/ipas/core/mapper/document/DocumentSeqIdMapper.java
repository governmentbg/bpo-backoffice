package bg.duosoft.ipas.core.mapper.document;

import bg.duosoft.ipas.core.model.document.CDocumentSeqId;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentSeqIdMapper {

    @Mapping(target = "docSeqName", source = "docSeqTyp.docSeqName")
    @Mapping(target = "docSeqType", source = "docSeqTyp.docSeqTyp")
    @Mapping(target = "docSeqNbr", source = "docSeqNbr")
    @Mapping(target = "docSeqSeries", source = "docSeqSeries")
    @BeanMapping(ignoreByDefault = true)
    CDocumentSeqId toCore(IpDoc ipDoc);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    IpDoc toEntity(CDocumentSeqId cDocumentSeqId);

}
