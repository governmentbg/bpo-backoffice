package bg.duosoft.ipas.core.mapper.design;

import bg.duosoft.ipas.core.mapper.file.FileIdMapper;
import bg.duosoft.ipas.core.mapper.process.ProcessSimpleMapper;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.persistence.model.entity.design.SingleDesignIpFile;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        FileIdMapper.class,
        ProcessSimpleMapper.class
})
public abstract class SingleDesignFileMapper {

    @Mapping(target = "fileId",                                             source = "pk")
    @Mapping(target = "processSimpleData",                                  source = "ipProcSimple")
    @Mapping(target = "processId.processType",                              source = "procTyp")
    @Mapping(target = "processId.processNbr",                               source = "procNbr")
    @Mapping(target = "filingData.applicationType",                         source = "applTyp")
    @Mapping(target = "filingData.applicationSubtype",                      source = "applSubtyp")
    @Mapping(target = "filingData.lawCode",                                 source = "lawCode")
    @Mapping(target = "filingData.receptionDate",                           source = "ipDoc.receptionDate")
    @Mapping(target = "filingData.filingDate",                              source = "filingDate")
    @Mapping(target = "filingData.receptionDocument.documentId.docOrigin",  source = "ipDoc.pk.docOri")
    @Mapping(target = "filingData.receptionDocument.documentId.docNbr",     source = "ipDoc.pk.docNbr")
    @Mapping(target = "filingData.receptionDocument.documentId.docSeries",  source = "ipDoc.pk.docSer")
    @Mapping(target = "filingData.receptionDocument.documentId.docLog",     source = "ipDoc.pk.docLog")
    @BeanMapping(ignoreByDefault = true)
    public abstract CFile toCore(SingleDesignIpFile ipFile);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract SingleDesignIpFile toEntity(CFile file);
}
