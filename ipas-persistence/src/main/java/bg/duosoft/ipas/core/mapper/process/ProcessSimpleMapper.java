package bg.duosoft.ipas.core.mapper.process;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.mapper.miscellaneous.SimpleUserMapper;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.process.CProcessSimpleData;
import bg.duosoft.ipas.persistence.model.entity.design.SingleDesignIpFile;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcSimple;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * User: ggeorgiev
 * Date: 21.6.2019 г.
 * Time: 14:13
 */
@Mapper(componentModel = "spring", uses = {
        SimpleUserMapper.class,
        ProcessIdMapper.class
})
public abstract class ProcessSimpleMapper extends BaseObjectMapper<IpProcSimple, CProcessSimpleData> {

    @Mapping(target = "creationDate",                     source = "creationDate")
    @Mapping(target = "statusDate",                       source = "statusDate")
    @Mapping(target = "responsibleUser",                  source = "responsibleUser")
    @Mapping(target = "processId",                        source = "pk")
    @Mapping(target = "statusCode",                       source = "statusCode")
    @Mapping(target = "fileProcessId.processType",        source = "fileProcTyp")
    @Mapping(target = "fileProcessId.processNbr",         source = "fileProcNbr")
    @Mapping(target = "documentId.docOrigin",             source = "docOri")
    @Mapping(target = "documentId.docLog",                source = "docLog")
    @Mapping(target = "documentId.docSeries",             source = "docSer")
    @Mapping(target = "documentId.docNbr",                source = "docNbr")
    @Mapping(target = "fileId.fileSeq",                   source = "fileSeq")
    @Mapping(target = "fileId.fileType",                  source = "fileTyp")
    @Mapping(target = "fileId.fileSeries",                source = "fileSer")
    @Mapping(target = "fileId.fileNbr",                   source = "fileNbr")
    @BeanMapping(ignoreByDefault = true)
    public abstract CProcessSimpleData toCore(IpProcSimple ipProcSimple);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract IpProcSimple toEntity(CProcessSimpleData file);
}
