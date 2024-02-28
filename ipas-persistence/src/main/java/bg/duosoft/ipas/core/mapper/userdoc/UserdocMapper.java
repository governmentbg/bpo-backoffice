package bg.duosoft.ipas.core.mapper.userdoc;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.mapper.document.DocumentIdMapper;
import bg.duosoft.ipas.core.mapper.document.DocumentMapper;
import bg.duosoft.ipas.core.mapper.efiling.UserdocEFilingDataMapper;
import bg.duosoft.ipas.core.mapper.mark.InternationalNiceClassMapper;
import bg.duosoft.ipas.core.mapper.miscellaneous.SimpleUserMapper;
import bg.duosoft.ipas.core.mapper.person.PersonAddressMapper;
import bg.duosoft.ipas.core.mapper.process.ProcessSimpleMapper;
import bg.duosoft.ipas.core.mapper.userdoc.court_appeal.UserdocCourtAppealMapper;
import bg.duosoft.ipas.core.mapper.userdoc.grounds.UserdocRootGroundsMapper;
import bg.duosoft.ipas.core.mapper.userdoc.reviewers.UserdocReviewerMapper;
import bg.duosoft.ipas.core.mapper.userdoc.userdoc_type.UserdocTypeMapper;
import bg.duosoft.ipas.core.model.IpasApplicationSearchResult;
import bg.duosoft.ipas.core.model.document.CDocument;
import bg.duosoft.ipas.core.model.file.CFileRecordal;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.model.process.CProcessSimpleData;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocMainObjectData;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.application.SearchApplicationService;
import bg.duosoft.ipas.core.service.file.FileRecordalService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.ext.core.*;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcSimple;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocTypes;
import bg.duosoft.ipas.persistence.model.entity.userdoc.court_appeals.IpUserdocCourtAppeal;
import bg.duosoft.ipas.persistence.model.entity.userdoc.grounds.IpSingleDesignGroundData;
import bg.duosoft.ipas.persistence.model.entity.userdoc.reviewers.IpUserdocReviewer;
import bg.duosoft.ipas.util.DefaultValue;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring", uses = {
        DocumentIdMapper.class,
        DocumentMapper.class,
        SimpleUserMapper.class,
        UserdocRootGroundsMapper.class,
        UserdocCourtAppealMapper.class,
        UserdocReviewerMapper.class,
        PersonAddressMapper.class,
        ProcessSimpleMapper.class,
        UserdocPersonMapper.class,
        UserdocNiceClassMapper.class,
        UserdocSingleDesignMapper.class,
        UserdocApprovedNiceClassMapper.class,
        InternationalNiceClassMapper.class,
        UserdocApprovedDataMapper.class,
        UserdocEFilingDataMapper.class,
        UserdocExtraDataMapper.class,
        StringToBooleanMapper.class,
        UserdocPatentDataMapper.class
})
public abstract class UserdocMapper {

    @Autowired
    private ProcessService processService;

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private UserdocTypeMapper userdocTypeMapper;

    @Autowired
    private UserdocApprovedDataMapper userdocApprovedDataMapper;

    @Autowired
    private UserdocPatentDataMapper userdocPatentDataMapper;

    @Autowired
    private UserdocEFilingDataMapper userdocEFilingDataMapper;

    @Autowired
    private ProcessSimpleMapper processSimpleMapper;

    @Autowired
    private FileRecordalService fileRecordalService;

    @Autowired
    private SearchApplicationService searchApplicationService;

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion", source = "rowVersion")
    @Mapping(target = "documentId", source = "pk")
    @Mapping(target = "captureUser", source = "captureUser")
    @Mapping(target = "captureDate", source = "captureDate")
    @Mapping(target = "applicantNotes", source = "applicantNotes")
    @Mapping(target = "courtDocNbr", source = "courtDocNbr")
    @Mapping(target = "courtDocDate", source = "courtDocDate")
    @Mapping(target = "decreeDate", source = "decreeDate")
    @Mapping(target = "servicePerson", source = "servicePerson")
    @Mapping(target = "userdocRootGrounds", source = "userdocRootGrounds")
    @Mapping(target = "userdocCourtAppeals", source = "userdocCourtAppeals")
    @Mapping(target = "reviewers", source = "reviewers")
    @Mapping(target = "userdocPersonData.personList", source = "persons")
    @Mapping(target = "document", source = "ipDoc")
    @Mapping(target = "protectionData.niceClassList", source = "ipUserdocNiceClasses")
    @Mapping(target = "approvedNiceClassList", source = "ipUserdocApprovedNiceClasses")
    @Mapping(target = "internationalNiceClasses", source = "ipInternationalNiceClasses")
    @Mapping(target = "approvedData", source = "ipDoc.ipUserdocApprovedData")
    @Mapping(target = "patentData", source = "ipDoc.patentData")
    @Mapping(target = "indCompulsoryLicense", source = "indCompulsoryLicense")
    @Mapping(target = "indExclusiveLicense", source = "indExclusiveLicense")
    @Mapping(target = "userdocEFilingData", source = "ipDoc.userdocEFilingData")
    @Mapping(target = "singleDesigns", source = "ipUserdocSingleDesigns")
    @Mapping(target = "userdocExtraData", source = "userdocExtraData")
    public abstract CUserdoc toCore(IpUserdoc ipUserdoc, @Context Boolean loadFileContent);

    @InheritInverseConfiguration
    public abstract IpUserdoc toEntity(CUserdoc cUserdoc, @Context Boolean loadFileContent);

    @InheritConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract void fillUserdocFields(CUserdoc cUserdoc, @MappingTarget IpUserdoc target);

    @AfterMapping
    protected void afterToCore(@MappingTarget CUserdoc target, IpUserdoc source, @Context Boolean loadFileContent) {
        IpDocPK pk = source.getPk();

        IpProcSimple proc = source.getIpDoc().getProc();
        CProcessSimpleData processSimpleData = processSimpleMapper.toCore(proc);
        processSimpleData.setFileId(null);// File ID is always empty for userdoc process !
        target.setProcessSimpleData(processSimpleData);

        CProcessParentData cProcessParentData = processService.generateProcessParentHierarchy(processSimpleData.getProcessId());
        target.setUserdocParentData(cProcessParentData);

        List<IpUserdocTypes> ipUserdocTypes = source.getIpUserdocTypes();
        if (!CollectionUtils.isEmpty(ipUserdocTypes) && ipUserdocTypes.size() == DefaultValue.ONE_RESULT_SIZE) {
            CUserdocType cUserdocType = userdocTypeMapper.toCore(ipUserdocTypes.get(DefaultValue.FIRST_RESULT));
            target.setUserdocType(cUserdocType);
        }

        CFileRecordal fileRecordal = fileRecordalService.selectRecordalByUserdocId(target.getDocumentId());
        if (Objects.nonNull(fileRecordal)) {
            target.setFileRecordal(fileRecordal);
        }

        IpasApplicationSearchResult ipasApplicationSearchResult = searchApplicationService.selectApplication(target.getUserdocParentData().getTopProcessFileData().getFileId());
        if (Objects.nonNull(ipasApplicationSearchResult)) {
            List<CPerson> owners = ipasApplicationSearchResult.getOwners();
            target.setUserdocMainObjectData(new CUserdocMainObjectData(ipasApplicationSearchResult.getFileId(), owners));
        }
        target.setNotes(source.getIpDoc().getNotes());
        target.getReviewers().sort(Comparator.comparing(reviewer -> reviewer.getUser().getUserName()));
    }

    @AfterMapping
    protected void afterToEntity(@MappingTarget IpUserdoc target, CUserdoc source) {
        IpDocPK pk = target.getPk();

        CDocument document = source.getDocument();
        if (Objects.nonNull(document)) {
            target.setIpDoc(documentMapper.toEntity(document));
            if (Objects.nonNull(target.getIpDoc())) {
                target.getIpDoc().setIpUserdocApprovedData(userdocApprovedDataMapper.toEntity(source.getApprovedData()));
                target.getIpDoc().setUserdocEFilingData(userdocEFilingDataMapper.toEntity(source.getUserdocEFilingData()));
                target.getIpDoc().setPatentData(userdocPatentDataMapper.toEntity(source.getPatentData()));
            }
        }

        if (Objects.nonNull(target.getIpDoc().getUserdocEFilingData())) {
            target.getIpDoc().getUserdocEFilingData().setPk(pk);
        }

        CUserdocType userdocType = source.getUserdocType();
        if (Objects.nonNull(userdocType)) {
            IpUserdocTypes ipUserdocTypes = userdocTypeMapper.toEntity(userdocType);
            ipUserdocTypes.getPk().setDocOri(pk.getDocOri());
            ipUserdocTypes.getPk().setDocLog(pk.getDocLog());
            ipUserdocTypes.getPk().setDocSer(pk.getDocSer());
            ipUserdocTypes.getPk().setDocNbr(pk.getDocNbr());
            target.setIpUserdocTypes(Collections.singletonList(ipUserdocTypes));
        }

        if (!CollectionUtils.isEmpty(target.getPersons())) {
            target.getPersons().stream().map(IpUserdocPerson::getPk).forEach(userdocPersonPK -> {
                userdocPersonPK.setDocOri(pk.getDocOri());
                userdocPersonPK.setDocLog(pk.getDocLog());
                userdocPersonPK.setDocSer(pk.getDocSer());
                userdocPersonPK.setDocNbr(pk.getDocNbr());
            });
        }

        if (!CollectionUtils.isEmpty(target.getIpUserdocNiceClasses())) {
            target.getIpUserdocNiceClasses().stream().map(IpUserdocNiceClasses::getPk).forEach(ipUserdocNiceClassesPK -> {
                ipUserdocNiceClassesPK.setDocOri(pk.getDocOri());
                ipUserdocNiceClassesPK.setDocLog(pk.getDocLog());
                ipUserdocNiceClassesPK.setDocSer(pk.getDocSer());
                ipUserdocNiceClassesPK.setDocNbr(pk.getDocNbr());
            });
        }

        if (!CollectionUtils.isEmpty(target.getIpUserdocApprovedNiceClasses())) {
            target.getIpUserdocApprovedNiceClasses().stream().map(IpUserdocApprovedNiceClasses::getPk).forEach(ipUserdocApprovedNiceClassesPK -> {
                ipUserdocApprovedNiceClassesPK.setDocOri(pk.getDocOri());
                ipUserdocApprovedNiceClassesPK.setDocLog(pk.getDocLog());
                ipUserdocApprovedNiceClassesPK.setDocSer(pk.getDocSer());
                ipUserdocApprovedNiceClassesPK.setDocNbr(pk.getDocNbr());
            });
        }

        if (!CollectionUtils.isEmpty(target.getIpInternationalNiceClasses())) {
            target.getIpInternationalNiceClasses().stream().forEach(ipInternationalNiceClasses -> {
                ipInternationalNiceClasses.setDocOri(pk.getDocOri());
                ipInternationalNiceClasses.setDocLog(pk.getDocLog());
                ipInternationalNiceClasses.setDocSer(pk.getDocSer());
                ipInternationalNiceClasses.setDocNbr(pk.getDocNbr());
            });
        }

        if (!CollectionUtils.isEmpty(target.getIpUserdocSingleDesigns())) {
            target.getIpUserdocSingleDesigns().stream().map(IpUserdocSingleDesign::getPk).forEach(ipUserdocSingleDesignsPK -> {
                ipUserdocSingleDesignsPK.setDocOri(pk.getDocOri());
                ipUserdocSingleDesignsPK.setDocLog(pk.getDocLog());
                ipUserdocSingleDesignsPK.setDocSer(pk.getDocSer());
                ipUserdocSingleDesignsPK.setDocNbr(pk.getDocNbr());
            });
        }

        if (Objects.nonNull(target.getIpDoc().getIpUserdocApprovedData())) {
            target.getIpDoc().getIpUserdocApprovedData().setPk(pk);
        }

        if (Objects.nonNull(target.getIpDoc().getPatentData())) {
            target.getIpDoc().getPatentData().setPk(pk);
        }

        if (!CollectionUtils.isEmpty(target.getPersons())) {
            target.getPersons().stream().map(r -> r.getPk()).forEach(r -> {
                r.setDocOri(pk.getDocOri());
                r.setDocLog(pk.getDocLog());
                r.setDocSer(pk.getDocSer());
                r.setDocNbr(pk.getDocNbr());
            });
        }

        if (!CollectionUtils.isEmpty(target.getReviewers())) {
            target.getReviewers().stream().map(IpUserdocReviewer::getPk).forEach(reviewer -> {
                reviewer.setDocOri(pk.getDocOri());
                reviewer.setDocLog(pk.getDocLog());
                reviewer.setDocSer(pk.getDocSer());
                reviewer.setDocNbr(pk.getDocNbr());
            });
        }

        if (!CollectionUtils.isEmpty(target.getUserdocCourtAppeals())) {
            target.getUserdocCourtAppeals().stream().map(IpUserdocCourtAppeal::getPk).forEach(ipUserdocCourtAppeal -> {
                ipUserdocCourtAppeal.setDocOri(pk.getDocOri());
                ipUserdocCourtAppeal.setDocLog(pk.getDocLog());
                ipUserdocCourtAppeal.setDocSer(pk.getDocSer());
                ipUserdocCourtAppeal.setDocNbr(pk.getDocNbr());
            });
        }

        if (!CollectionUtils.isEmpty(target.getUserdocExtraData())) {
            target.getUserdocExtraData().stream().forEach(data -> {
                data.getPk().setDocOri(pk.getDocOri());
                data.getPk().setDocLog(pk.getDocLog());
                data.getPk().setDocSer(pk.getDocSer());
                data.getPk().setDocNbr(pk.getDocNbr());
            });
        }

        if (!CollectionUtils.isEmpty(target.getUserdocRootGrounds())) {
            target.getUserdocRootGrounds().stream().forEach(rootGround -> {
                rootGround.getPk().setDocOri(pk.getDocOri());
                rootGround.getPk().setDocLog(pk.getDocLog());
                rootGround.getPk().setDocSer(pk.getDocSer());
                rootGround.getPk().setDocNbr(pk.getDocNbr());

                rootGround.getUserdocSubGrounds().stream().forEach(subGround -> {
                    subGround.getPk().setDocOri(pk.getDocOri());
                    subGround.getPk().setDocLog(pk.getDocLog());
                    subGround.getPk().setDocSer(pk.getDocSer());
                    subGround.getPk().setDocNbr(pk.getDocNbr());
                });

                if (Objects.nonNull(rootGround.getMarkGroundData())) {
                    if (!CollectionUtils.isEmpty(rootGround.getMarkGroundData().getUserdocGroundsNiceClasses())) {
                        rootGround.getMarkGroundData().getUserdocGroundsNiceClasses().stream().forEach(niceClass -> {
                            niceClass.getPk().setDocOri(pk.getDocOri());
                            niceClass.getPk().setDocLog(pk.getDocLog());
                            niceClass.getPk().setDocSer(pk.getDocSer());
                            niceClass.getPk().setDocNbr(pk.getDocNbr());
                            niceClass.getPk().setRootGroundId(rootGround.getPk().getRootGroundId());
                        });
                    }
                    rootGround.getMarkGroundData().setPk(rootGround.getPk());
                }
                if (Objects.nonNull(rootGround.getPatentGroundData())) {
                    rootGround.getPatentGroundData().setPk(rootGround.getPk());
                }

                if (!CollectionUtils.isEmpty(rootGround.getSingleDesignGroundData())) {
                    for (IpSingleDesignGroundData singleDesignData : rootGround.getSingleDesignGroundData()) {
                        singleDesignData.getPk().setDocOri(pk.getDocOri());
                        singleDesignData.getPk().setDocLog(pk.getDocLog());
                        singleDesignData.getPk().setDocSer(pk.getDocSer());
                        singleDesignData.getPk().setDocNbr(pk.getDocNbr());
                        singleDesignData.getPk().setRootGroundId(rootGround.getPk().getRootGroundId());
                    }
                }
            });

        }

        CProcessSimpleData processSimpleData = source.getProcessSimpleData();
        IpDoc ipDoc = target.getIpDoc();
        if (Objects.nonNull(ipDoc) && Objects.nonNull(processSimpleData)) {
            IpProcSimple ipProcSimple = processSimpleMapper.toEntity(processSimpleData);
            ipDoc.setProc(ipProcSimple);
        }
        ipDoc.setNotes(source.getNotes());

    }

}
