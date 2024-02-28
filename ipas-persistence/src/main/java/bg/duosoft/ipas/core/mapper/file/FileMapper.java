package bg.duosoft.ipas.core.mapper.file;

import bg.duosoft.ipas.core.mapper.MapperHelper;
import bg.duosoft.ipas.core.mapper.document.DocumentMapper;
import bg.duosoft.ipas.core.mapper.process.ProcessSimpleMapper;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileRecordal;
import bg.duosoft.ipas.core.model.file.CRelationship;
import bg.duosoft.ipas.core.model.reception.CReceptionCorrespondent;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionUserdocCorrespondent;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationship;
import bg.duosoft.ipas.util.DefaultValue;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {
        FileIdMapper.class,
        ProcessSimpleMapper.class,
        FileRecordalMapper.class,
})
public abstract class FileMapper {
    //TODO fileSourceWcode
    @Autowired
    private Relationship1Mapper relationship1Mapper;

    @Autowired
    private Relationship2Mapper relationship2Mapper;

    @Autowired
    private DocumentMapper documentMapper;

    @Mapping(target = "filingData.publicationTyp",                          source = "publicationTyp")
    @Mapping(target = "filingData.applicationType",                         source = "applTyp")
    @Mapping(target = "filingData.applicationSubtype",                      source = "applSubtyp")
    @Mapping(target = "filingData.lawCode",                                 source = "lawCode")
    @Mapping(target = "filingData.filingDate",                              source = "filingDate")
    @Mapping(target = "filingData.captureDate",                             source = "captureDate")
    @Mapping(target = "filingData.captureUserId",                           source = "captureUserId")
    @Mapping(target = "filingData.validationDate",                          source = "validationDate")
    @Mapping(target = "filingData.validationUserId",                        source = "validationUserId")
    @Mapping(target = "filingData.corrFileSeq",                             source = "corrFileSeq")
    @Mapping(target = "filingData.corrFileType",                            source = "corrFileTyp")
    @Mapping(target = "filingData.corrFileSeries",                          source = "corrFileSer")
    @Mapping(target = "filingData.corrFileNbr",                             source = "corrFileNbr")
    @Mapping(target = "filingData.indIncorrRecpDeleted",                    source = "indIncorrRecpDeleted")
    @Mapping(target = "filingData.receptionDate",                           source = "ipDoc.receptionDate")
    @Mapping(target = "filingData.externalOfficeCode",                      source = "ipDoc.externalOfficeCode")
    @Mapping(target = "filingData.externalOfficeFilingDate",                source = "ipDoc.externalOfficeFilingDate")
    @Mapping(target = "filingData.externalSystemId",                        source = "ipDoc.externalSystemId")
    @Mapping(target = "filingData.receptionUserId",                         source = "ipDoc.receptionUserId")
    @Mapping(target = "notes",                                              source = "ipDoc.notes")
    @Mapping(target = "registrationData.registrationId.registrationNbr",    source = "registrationNbr" )
    @Mapping(target = "registrationData.registrationId.registrationType",   source = "registrationTyp")
    @Mapping(target = "registrationData.registrationId.registrationSeries", source = "registrationSer")
    @Mapping(target = "registrationData.registrationId.registrationDup",    source = "registrationDup")
    @Mapping(target = "registrationData.registrationDate",                  source = "registrationDate")
    @Mapping(target = "registrationData.indRegistered",                     expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipFile.getIndRegistered()))")
    @Mapping(target = "registrationData.entitlementDate",                   source = "entitlementDate")
    @Mapping(target = "registrationData.expirationDate",                    source = "expirationDate")
    @Mapping(target = "priorityData.earliestAcceptedParisPriorityDate",     source = "firstPriorityDate")
    @Mapping(target = "publicationData.publicationDate",                    source = "publicationDate")
    @Mapping(target = "publicationData.journalCode",                        source = "journalCode")
    @Mapping(target = "publicationData.specialPublicationDate",             source = "specialPublDate")
    @Mapping(target = "fileId",                                             source = "pk")
    @Mapping(target = "rowVersion",                                         source = "rowVersion")
    @Mapping(target = "processId.processType",                              source = "procTyp")
    @Mapping(target = "processId.processNbr",                               source = "procNbr")
    @Mapping(target = "processSimpleData",                                  source = "ipProcSimple")
    @Mapping(target = "title",                                              source = "title")
    @Mapping(target = "fileRecordals",                                      source = "ipFileRecordals")
    @BeanMapping(ignoreByDefault = true)
    public abstract CFile toCore(IpFile ipFile);

    @InheritInverseConfiguration
    @Mapping(target = "indRegistered", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(file.getRegistrationData().getIndRegistered()))")
    @Mapping(target = "cfFileType.fileTyp", source = "file.fileId.fileType")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpFile toEntity(CFile file);

    @InheritConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract void fillIpFileFields(CFile file, @MappingTarget IpFile target);


    //custom ipFile to CFile mapping
    @AfterMapping
    protected void afterIpFileToFileMapping(IpFile source, @MappingTarget CFile target) {
        //adding relationship details
        List<CRelationship> res = new ArrayList<>();
        if (source.getIpFileRelationships1() != null) {
            res = source.getIpFileRelationships1().stream().map(r -> relationship1Mapper.toCore(r)).collect(Collectors.toList());
        }
        if (source.getIpFileRelationships2() != null) {
            res.addAll(source.getIpFileRelationships2().stream().map(r -> relationship2Mapper.toCore(r)).collect(Collectors.toList()));
        }
        if (res.size() > 0) {
            target.setRelationshipList(res);
        }

        target.getFilingData().setReceptionDocument(documentMapper.toCore(source.getIpDoc()));
        //end of adding relationship details
        sortRecordalsByDate(target);
    }
    //end of custom ipFileToFile mapping


    //custom file to ipFile mapping
    @AfterMapping
    protected void afterFileToIpFileMapping(CFile source, @MappingTarget IpFile target) {
        if (target.getRowVersion() == null) {
            target.setRowVersion(DefaultValue.ROW_VERSION);
        }
        target.setIpFileRelationships1(new ArrayList<>());
        target.setIpFileRelationships2(new ArrayList<>());
        if (!CollectionUtils.isEmpty(source.getRelationshipList())) {
            Map<String, List<CRelationship>> grouped = source.getRelationshipList().stream().collect(Collectors.groupingBy(CRelationship::getRelationshipRole));
            if (grouped.get("1") != null) {
                List<IpFileRelationship> rels1 = grouped.get("1").stream().map(r -> relationship1Mapper.toEntity(r, source.getFileId())).collect(Collectors.toList());
                target.setIpFileRelationships1(rels1);
            }
            if (grouped.get("2") != null) {
                List<IpFileRelationship> rels2 = grouped.get("2").stream().map(r -> relationship2Mapper.toEntity(r, source.getFileId())).collect(Collectors.toList());
                target.setIpFileRelationships2(rels2);
            }
        }

        if (Objects.nonNull(source.getFilingData().getReceptionDocument()))
            target.getIpDoc().setIndFaxReception(MapperHelper.getBooleanAsText(source.getFilingData().getReceptionDocument().getIndFaxReception()));

    }
    //end of custom file to ipFile mapping

    private void sortRecordalsByDate(@MappingTarget CFile target) {
        List<CFileRecordal> fileRecordals = target.getFileRecordals();
        if (!CollectionUtils.isEmpty(fileRecordals)) {
            fileRecordals.sort(Comparator.comparing(CFileRecordal::getDate));
        }
    }

    public abstract List<CFile> toCoreList(List<IpFile> ipFiles);

}
