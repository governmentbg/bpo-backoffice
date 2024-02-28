package bg.duosoft.ipas.core.mapper.patent;

import bg.duosoft.ipas.core.mapper.MapperHelper;
import bg.duosoft.ipas.core.mapper.common.FileSeqTypSerNbrMapper;
import bg.duosoft.ipas.core.mapper.efiling.ObjectEFilingDataMapper;
import bg.duosoft.ipas.core.mapper.file.FileMapper;
import bg.duosoft.ipas.core.mapper.file.RelationshipExtendedMapper;
import bg.duosoft.ipas.core.mapper.plant.PlantMapper;
import bg.duosoft.ipas.core.mapper.spc.SpcExtendedMapper;
import bg.duosoft.ipas.core.model.patent.CAuthorshipData;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CTechnicalData;
import bg.duosoft.ipas.core.model.person.CAuthor;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.persistence.model.entity.FileSeqTypSerNbrPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.patent.*;
import bg.duosoft.ipas.util.person.InventorUtils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import java.util.*;

@Mapper(componentModel = "spring",
        uses = {
                FileMapper.class,
                PatentIpcClassMapper.class,
                PatentCpcClassMapper.class,
                AuthorMapper.class,
                PlantMapper.class,
                SpcExtendedMapper.class,
                ClaimMapper.class,
                RefExamMapper.class,
                PatentDetailsMapper.class,
                RelationshipExtendedMapper.class,
                DrawingMapper.class,
                ObjectEFilingDataMapper.class,
                PatentLocarnoClassesMapper.class,
        })
public abstract class PatentMapper {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileSeqTypSerNbrMapper fileSeqTypSerNbrMapper;

    @Autowired
    private PatentFileMapper patentFileMapper;

    @Autowired
    private PatentTechnicalDataMapper patentTechnicalDataMapper;

    @Autowired
    private PctApplicationDataMapper pctApplicationDataMapper;


    @Autowired
    private PatentRelationshipsMapperHelper patentRelationshipsMapperHelper;

    @Mapping(target = "patentContainsDrawingList", expression = "java(ipPatent.getIpPatentDrawings() != null)")//TODO collectionUtils
    @Mapping(target = "indReadDrawingList", expression = "java(loadFileContent)")
    @Mapping(target = "technicalData.title", source = "title")
    @Mapping(target = "technicalData.claimList", source = "ipPatentClaims")
    @Mapping(target = "technicalData.citationList", source = "ipPatentRefExams")
    @Mapping(target = "technicalData.englishTitle", source = "englishTitle")
    @Mapping(target = "technicalData.lastClaimsPageRef", source = "lastClaimsPageRef")
    @Mapping(target = "technicalData.lastDescriptionPageRef", source = "lastDescriptionPageRef")
//    @Mapping(target = "technicalData.noveltyDate",                             source = "") // TODO
    @Mapping(target = "technicalData.hasIpc", expression = "java(ipPatent.getIpPatentIpcClasses().isEmpty())")
    @Mapping(target = "technicalData.hasCpcClasses", expression = "java(ipPatent.getIpPatentCpcClasses().isEmpty())")
    @Mapping(target = "technicalData.hasCpc", expression = "java(ipPatent.getIpPatentClaims().isEmpty())")
    @Mapping(target = "technicalData.ipcClassList", source = "ipPatentIpcClasses")
    @Mapping(target = "technicalData.cpcClassList", source = "ipPatentCpcClasses")
    @Mapping(target = "technicalData.drawingList", source = "ipPatentDrawings")
    @Mapping(target = "relationshipExtended", source = "relationshipExtended")
    @Mapping(target = "file", source = "file")
    @Mapping(target = "rowVersion", source = "rowVersion")
    @Mapping(target = "authorshipData.authorList", source = "ipPatentInventors")
    @Mapping(target = "plantData", source = "plantData")
    @Mapping(target = "spcExtended", source = "spcExtended")
    @Mapping(target = "patentDetails", source = "patentDetails")
    @Mapping(target = "patentEFilingData", source = "patentEFilingData")
    @BeanMapping(ignoreByDefault = true)
    public abstract CPatent toCore(IpPatent ipPatent, @Context Boolean loadFileContent);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract IpPatent toEntity(CPatent cPatent, @Context Boolean loadFileContent);


    // ipPatentToPatent custom mappers
    @AfterMapping
    protected void afterToCore(@MappingTarget CPatent target, IpPatent source, @Context Boolean loadFileContent) {


        if (source != null) {

            patentFileMapper.toCore(source, target.getFile());
            pctApplicationDataMapper.toCore(source, target);

            target.getFile().getRegistrationData().setIndRegistered(MapperHelper.getTextAsBoolean(source.getIndRegistered()));

            if (source.getIpPatentSummaries() != null) {
                CTechnicalData cTechnicalData = target.getTechnicalData();

                for (IpPatentSummary ipPatentSummary : source.getIpPatentSummaries()) {
                    if (ipPatentSummary.getPk().getLanguageCode().equals("MX")) {
                        cTechnicalData.setMainAbstract(ipPatentSummary.getSummary());
                    }
                    if (ipPatentSummary.getPk().getLanguageCode().equals("US")) {
                        cTechnicalData.setEnglishAbstract(ipPatentSummary.getSummary());
                    }
                }
            }
            CAuthorshipData cAuthorshipData = target.getAuthorshipData();
            if (cAuthorshipData != null) {
                if (source.getIndOwnerSameasInventor() != null && source.getIndOwnerSameasInventor().equals('N')) {
                    cAuthorshipData.setIndOwnerSameAuthor(true);
                } else {
                    cAuthorshipData.setIndOwnerSameAuthor(false);
                }

                List<CAuthor> authorList = cAuthorshipData.getAuthorList();
                if (!CollectionUtils.isEmpty(authorList)) {
                    for (CAuthor cAuthor : authorList) {
                        Long authorSeq = cAuthor.getAuthorSeq();
                        if (Objects.isNull(authorSeq)) {
                            Long maxSequenceNumber = InventorUtils.getMaxSequenceNumber(authorList);
                            if (Objects.isNull(maxSequenceNumber)) {
                                maxSequenceNumber = 0L;
                            }
                            cAuthor.setAuthorSeq(maxSequenceNumber + 1);
                        }
                    }
                }
            }
            if (Objects.nonNull(target.getPatentDetails()) && !CollectionUtils.isEmpty(target.getPatentDetails().getPatentAttachments())){
                target.getPatentDetails().getPatentAttachments().sort(Comparator.comparing(r->r.getDateCreated()));
            }

            patentRelationshipsMapperHelper.fileRelationshipToExtendedRelationship(target,fileService);
        }
    }

    // patentToIpPatent custom methods
    @AfterMapping
    protected void afterToEntity(@MappingTarget IpPatent target, CPatent source) {

        patentFileMapper.toEntity(source.getFile(), target);

        pctApplicationDataMapper.toEntity(source, target);

        patentTechnicalDataMapper.toEntity(source.getTechnicalData(), target);

        // TODO mapping abstract data
        CAuthorshipData cAuthorshipData = source.getAuthorshipData();

        target.setIndRegistered(MapperHelper.getBooleanAsText(source.getFile().getRegistrationData().getIndRegistered()));

        if (cAuthorshipData != null) {
            if (cAuthorshipData.getIndOwnerSameAuthor() != null && cAuthorshipData.getIndOwnerSameAuthor() == true) {
                target.setIndOwnerSameasInventor("N");
            } else {
                target.setIndOwnerSameasInventor(null);
            }
        }


        CTechnicalData cTechnicalData = source.getTechnicalData();
        List<IpPatentSummary> ipPatentSummaries = new ArrayList<>();

        if (cTechnicalData.getMainAbstract() != null && !cTechnicalData.getMainAbstract().isEmpty()) {
            IpPatentSummaryPK ipPatentSummaryPK = new IpPatentSummaryPK();
            IpPatentSummary ipPatentSummary = new IpPatentSummary();
            ipPatentSummaryPK.setLanguageCode("MX");
            ipPatentSummary.setSummary(cTechnicalData.getMainAbstract());
            ipPatentSummary.setPk(ipPatentSummaryPK);
            ipPatentSummary.setRowVersion(1);
            ipPatentSummaries.add(ipPatentSummary);

        }

        if (cTechnicalData.getEnglishAbstract() != null && !cTechnicalData.getEnglishAbstract().isEmpty()) {
            IpPatentSummaryPK ipPatentSummaryPK = new IpPatentSummaryPK();
            IpPatentSummary ipPatentSummary = new IpPatentSummary();
            ipPatentSummaryPK.setLanguageCode("US");
            ipPatentSummary.setSummary(cTechnicalData.getEnglishAbstract());
            ipPatentSummary.setPk(ipPatentSummaryPK);
            ipPatentSummary.setRowVersion(1);
            ipPatentSummaries.add(ipPatentSummary);

        }



        target.setIpPatentSummaries(ipPatentSummaries);


//        target.getFile().getIpDoc().setReceptionDate(target.getReceptionDate()); --> Обсъждахме с Жоро дали да бъде тригер или от кода да се сетва тази дата ?

    }
    //end of patentToIpPatent custom methods


    @AfterMapping
    protected void fillPrimaryKeys(@MappingTarget IpPatent target, CPatent source) {
        List<FileSeqTypSerNbrPK> pks = new ArrayList<>();
        target.setPk(new IpFilePK());
        pks.add(target.getPk());


        if (target.getIpPatentClaims() != null) {
            target.getIpPatentClaims().stream().map(IpPatentClaims::getPk).forEach(pks::add);
        }

        if (target.getIpPatentRefExams() != null) {
            target.getIpPatentRefExams().stream().map(IpPatentRefExam::getPk).forEach(pks::add);
        }

        if (Objects.nonNull(target.getPatentEFilingData())){
            target.getPatentEFilingData().setPk(target.getPk());
        }

        if (target.getIpPatentDrawings() != null) {
            target.getIpPatentDrawings().stream().forEach(drawing->{
                pks.add(drawing.getPk());
                if (drawing.getSingleDesignExtended()!=null){
                    pks.add(drawing.getSingleDesignExtended().getPk());
                }
            });
        }

        if (target.getIpPatentIpcClasses() != null) {
            target.getIpPatentIpcClasses().stream().map(IpPatentIpcClasses::getPk).forEach(pks::add);

        }
        if (target.getIpPatentCpcClasses() != null) {
            target.getIpPatentCpcClasses().stream().map(IpPatentCpcClasses::getPk).forEach(pks::add);

        }

        if (target.getIpPatentInventors() != null) {
            target.getIpPatentInventors().stream().map(IpPatentInventors::getPk).forEach(pks::add);
        }

        if (target.getOwners() != null) {
            target.getOwners().stream().map(IpPatentOwners::getPk).forEach(pks::add);
        }

        if (target.getRepresentatives() != null) {
            target.getRepresentatives().stream().map(IpPatentReprs::getPk).forEach(pks::add);
        }

        if (target.getPriorities() != null) {
            target.getPriorities().stream().map(IpPatentPriorities::getPk).forEach(pks::add);
        }

        if (target.getIpPatentSummaries() != null) {
            target.getIpPatentSummaries().stream().map(IpPatentSummary::getPk).forEach(pks::add);
        }

        if (target.getRelationshipExtended() != null) {
            target.getRelationshipExtended().setPk(new IpFilePK());
            pks.add(target.getRelationshipExtended().getPk());
        }

        if (target.getPlantData() != null) {
            target.getPlantData().setPk(new IpFilePK());
            pks.add(target.getPlantData().getPk());
        }

        if (target.getSpcExtended() != null) {
            target.getSpcExtended().setPk(new IpFilePK());
            pks.add(target.getSpcExtended().getPk());
        }


        if (target.getPatentDetails() != null) {
            target.getPatentDetails().setPk(new IpFilePK());
            pks.add(target.getPatentDetails().getPk());
            if (!CollectionUtils.isEmpty(target.getPatentDetails().getPatentAttachments())){
                target.getPatentDetails().getPatentAttachments().stream().map(IpPatentAttachment::getPk).forEach(pks::add);
            }
        }
        pks.forEach(r -> fileSeqTypSerNbrMapper.toEntity(source.getFile().getFileId(), r));
    }
}
