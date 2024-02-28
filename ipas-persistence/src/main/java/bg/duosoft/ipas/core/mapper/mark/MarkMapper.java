package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.mapper.acp.*;
import bg.duosoft.ipas.core.mapper.common.FileSeqTypSerNbrMapper;
import bg.duosoft.ipas.core.mapper.efiling.ObjectEFilingDataMapper;
import bg.duosoft.ipas.core.mapper.file.FileMapper;
import bg.duosoft.ipas.core.mapper.file.RelationshipExtendedMapper;
import bg.duosoft.ipas.core.model.acp.CAcpPersonsData;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.mark.CMarkAttachment;
import bg.duosoft.ipas.core.model.mark.CProtectionData;
import bg.duosoft.ipas.core.service.mark.MarkUsageRuleService;
import bg.duosoft.ipas.core.service.nomenclature.UsageRuleService;
import bg.duosoft.ipas.enums.MarkSignType;
import bg.duosoft.ipas.persistence.model.entity.FileSeqTypSerNbrPK;
import bg.duosoft.ipas.persistence.model.entity.ext.acp.*;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.mark.*;
import bg.duosoft.ipas.persistence.model.entity.userdoc.grounds.IpUserdocRootGrounds;
import bg.duosoft.ipas.util.mark.MarkSignDataAttachmentUtils;
import bg.duosoft.ipas.util.mark.MarkSignTypeUtils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {
                NiceClassMapper.class,
                FileMapper.class,
                ObjectEFilingDataMapper.class,
                RelationshipExtendedMapper.class,
                EnotifMarkMapper.class,
                InternationalNiceClassMapper.class,
                MarkUsageRuleMapper.class,
                AcpAffectedObjectMapper.class,
                AcpViolationPlaceMapper.class,
                AcpDetailsMapper.class,
                AcpTakenItemMapper.class,
                AcpCheckReasonMapper.class,
                AcpCheckDataMapper.class,
                AcpAdministrativePenaltyMapper.class,
                MarkInternationalReplacementMapper.class
        })
public abstract class MarkMapper {

    @Autowired
    private FileSeqTypSerNbrMapper fileSeqTypSerNbrMapper;
    @Autowired
    private MarkFileMapper markFileMapper;
    @Autowired
    private AcpPersonDataMapper acpPersonDataMapper;
    @Autowired
    private MarkSignDataMapper markSignDataMapper;
    @Autowired
    private MarkAttachmentLogoMapper markAttachmentLogoMapper;
    @Autowired
    private MarkAttachmentMapper markAttachmentMapper;

    @Autowired
    private MarkUsageRuleService markUsageRuleService;

    @Mapping(target = "indReadAttachments", expression = "java(addAttachmentsData)")
    @Mapping(target = "madridApplicationData.internationalFileNumber", source = "intregn")
    @Mapping(target = "madridApplicationData.basicFileRef", source = "basicFileRef")
    @Mapping(target = "madridApplicationData.intFilingDate", source = "intFilingDate")
    @Mapping(target = "protectionData.niceClassList", source = "ipMarkNiceClasses")
    @Mapping(target = "file", source = "file")
    @Mapping(target = "limitationData.disclaimer", source = "disclaimer")
    @Mapping(target = "limitationData.disclaimerInOtherLang", source = "disclaimerLang2")
    @Mapping(target = "limitationData.byConsent", source = "byConsentDescription")
    @Mapping(target = "limitationData.regulations", source = "ipMarkRegulation.regulationsDescription")
    @Mapping(target = "signData.markName", source = "name.markName")
    @Mapping(target = "signData.markNameInOtherLang", source = "name.markNameLang2")
    @Mapping(target = "signData.markTranslation", source = "translation")
    @Mapping(target = "signData.markTranslationInOtherLang", source = "translationLang2")
    @Mapping(target = "signData.seriesDescription", source = "markSeriesDescription")
    @Mapping(target = "signData.markTransliteration", source = "markTransliteration")
    @Mapping(target = "signData.markTransliterationInOtherLang", source = "markTransliterationLang2")
    @Mapping(target = "renewalData.lastRenewalDate", source = "lastRenewalDate")
    @Mapping(target = "relationshipExtended", source = "relationshipExtended")
    @Mapping(target = "rowVersion", source = "rowVersion")
    @Mapping(target = "novelty1Date", source = "novelty1Date")
    @Mapping(target = "novelty2Date", source = "novelty2Date")
    @Mapping(target = "markEFilingData", source = "markEFilingData")
    @Mapping(target = "enotifMarks", source = "enotifMarks")
    @Mapping(target = "description", source = "notes")
    @Mapping(target = "internationalNiceClasses", source = "ipInternationalNiceClasses")
    @Mapping(target = "acpAffectedObjects", source = "acpAffectedObjects")
    @Mapping(target = "acpViolationPlaces", source = "acpViolationPlaces")
    @Mapping(target = "acpDetails", source = "acpDetails")
    @Mapping(target = "acpTakenItems", source = "acpTakenItems")
    @Mapping(target = "acpCheckReasons", source = "acpCheckReasons")
    @Mapping(target = "acpCheckData", source = "acpCheckData")
    @Mapping(target = "acpAdministrativePenalty", source = "acpAdministrativePenalty")
    @Mapping(target = "usageRules", source = "usageRules")
    @Mapping(target = "markInternationalReplacement", source = "markInternationalReplacement")
    @BeanMapping(ignoreByDefault = true)
    public abstract CMark toCore(IpMark ipMark, @Context Boolean addAttachmentsData);

    @InheritInverseConfiguration
    @Mapping(target = "ipMarkRegulation.rowVersion", constant = "1")
    @Mapping(target = "name.rowVersion", constant = "1")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpMark toEntity(CMark cMark);

    //ipMarkToMark custom mappers
    @AfterMapping
    protected void afterToCore(@MappingTarget CMark target, IpMark source, @Context Boolean addAttachmentsData) {
//        intellectualPropertyFileMapper.fillFileDetailsBase(source, target.getFile());
        markFileMapper.toCore(source, target.getFile());
        if (Objects.isNull(target.getFile().getAcpPersonsData())) {
            target.getFile().setAcpPersonsData(new CAcpPersonsData());
        }
        acpPersonDataMapper.toCore(source, target.getFile().getAcpPersonsData());
        target.getSignData().setSignType(MarkSignType.selectByCode(source.getSignWcode()));
        afterToCoreAttachmentData(target, source, addAttachmentsData);
    }

    // end of ipMarkToMark custom mappers!


    //markToIpMark custom methods
    @AfterMapping
    protected void afterToEntity(@MappingTarget IpMark target, CMark source) {
//        intellectualPropertyFileMapper.fillIntellectualPropertyEntityDetailsBase(source.getFile(), target);
        markFileMapper.toEntity(source.getFile(), target);
        acpPersonDataMapper.toEntity(source.getFile().getAcpPersonsData(), target);
        markSignDataMapper.toEntity(source.getSignData(), target);
        target.setSignWcode(source.getSignData().getSignType().code());
        afterToEntityAttachmentData(target, source);
        //updating nice class txt
        CProtectionData protectionData = source.getProtectionData();
        if (Objects.nonNull(protectionData) && !CollectionUtils.isEmpty(protectionData.getNiceClassList())) {
            String niceClassTxt = protectionData
                    .getNiceClassList()
                    .stream()
                    .map(r -> r.getNiceClassNbr())
                    .filter(Objects::nonNull)
                    .sorted()
                    .map(r -> r.toString())
                    .collect(Collectors.joining(" ", " ", " "));
            target.setNiceClassTxt(niceClassTxt);
        }
    }

    @AfterMapping
    protected void fillPrimaryKeys(@MappingTarget IpMark target, CMark source) {
        List<FileSeqTypSerNbrPK> pks = new ArrayList<>();
        target.setPk(new IpFilePK());
        pks.add(target.getPk());


        if (target.getIpMarkNiceClasses() != null) {
            target.getIpMarkNiceClasses().stream().forEach(nice -> {
                pks.add(nice.getPk());
                if (nice.getIpMarkNiceClassesExt() != null) {
                    pks.add(nice.getIpMarkNiceClassesExt().getPk());
                }
            });
        }

        if (Objects.nonNull(target.getMarkInternationalReplacement())) {
            target.getMarkInternationalReplacement().setPk(target.getPk());

            if (!CollectionUtils.isEmpty(target.getMarkInternationalReplacement().getReplacementNiceClasses())) {
                target.getMarkInternationalReplacement().getReplacementNiceClasses().stream().map(IpMarkIntlReplacementNiceClasses::getPk).forEach(niceClassPK -> {
                    niceClassPK.setFileSeq(source.getFile().getFileId().getFileSeq());
                    niceClassPK.setFileTyp(source.getFile().getFileId().getFileType());
                    niceClassPK.setFileSer(source.getFile().getFileId().getFileSeries());
                    niceClassPK.setFileNbr(source.getFile().getFileId().getFileNbr());
                });
            }
        }

        if (Objects.nonNull(target.getEnotifMarks()) &&!CollectionUtils.isEmpty(target.getEnotifMarks())){
            target.getEnotifMarks().stream().map(EnotifMark::getPk).forEach(enotifMark->{
                enotifMark.setFileSeq(source.getFile().getFileId().getFileSeq());
                enotifMark.setFileTyp(source.getFile().getFileId().getFileType());
                enotifMark.setFileSer(source.getFile().getFileId().getFileSeries());
                enotifMark.setFileNbr(source.getFile().getFileId().getFileNbr());

            });
        }

        if (!CollectionUtils.isEmpty(target.getAcpAffectedObjects())) {
            target.getAcpAffectedObjects().stream().forEach(affectedObject -> {
                affectedObject.setFileSeq(source.getFile().getFileId().getFileSeq());
                affectedObject.setFileTyp(source.getFile().getFileId().getFileType());
                affectedObject.setFileSer(source.getFile().getFileId().getFileSeries());
                affectedObject.setFileNbr(source.getFile().getFileId().getFileNbr());

            });
        }


        if (!CollectionUtils.isEmpty(target.getAcpCheckReasons())) {
            target.getAcpCheckReasons().stream().map(AcpCheckReason::getPk).forEach(acpCheckReasonPK -> {
                acpCheckReasonPK.setFileSeq(source.getFile().getFileId().getFileSeq());
                acpCheckReasonPK.setFileTyp(source.getFile().getFileId().getFileType());
                acpCheckReasonPK.setFileSer(source.getFile().getFileId().getFileSeries());
                acpCheckReasonPK.setFileNbr(source.getFile().getFileId().getFileNbr());

            });
        }

        if (!CollectionUtils.isEmpty(target.getAcpTakenItems())) {
            target.getAcpTakenItems().stream().map(AcpTakenItem::getPk).forEach(acpTakenItemPK -> {
                acpTakenItemPK.setFileSeq(source.getFile().getFileId().getFileSeq());
                acpTakenItemPK.setFileTyp(source.getFile().getFileId().getFileType());
                acpTakenItemPK.setFileSer(source.getFile().getFileId().getFileSeries());
                acpTakenItemPK.setFileNbr(source.getFile().getFileId().getFileNbr());
            });
        }

        if (!CollectionUtils.isEmpty(target.getAcpViolationPlaces())) {
            target.getAcpViolationPlaces().stream().map(AcpViolationPlace::getPk).forEach(acpViolationPlacePK -> {
                acpViolationPlacePK.setFileSeq(source.getFile().getFileId().getFileSeq());
                acpViolationPlacePK.setFileTyp(source.getFile().getFileId().getFileType());
                acpViolationPlacePK.setFileSer(source.getFile().getFileId().getFileSeries());
                acpViolationPlacePK.setFileNbr(source.getFile().getFileId().getFileNbr());

            });
        }

        if (!CollectionUtils.isEmpty(target.getAcpRepresentatives())) {
            target.getAcpRepresentatives().stream().map(AcpReprs::getPk).forEach(acpReprPk -> {
                acpReprPk.setFileSeq(source.getFile().getFileId().getFileSeq());
                acpReprPk.setFileTyp(source.getFile().getFileId().getFileType());
                acpReprPk.setFileSer(source.getFile().getFileId().getFileSeries());
                acpReprPk.setFileNbr(source.getFile().getFileId().getFileNbr());

            });
        }

        if (!CollectionUtils.isEmpty(target.getIpInternationalNiceClasses())) {
            target.getIpInternationalNiceClasses().stream().forEach(ipInternationalNiceClasses -> {
                ipInternationalNiceClasses.setFileSeq(source.getFile().getFileId().getFileSeq());
                ipInternationalNiceClasses.setFileTyp(source.getFile().getFileId().getFileType());
                ipInternationalNiceClasses.setFileSer(source.getFile().getFileId().getFileSeries());
                ipInternationalNiceClasses.setFileNbr(source.getFile().getFileId().getFileNbr());
            });
        }


        if (!CollectionUtils.isEmpty(target.getUsageRules())) {
            target.getUsageRules().stream().map(IpMarkUsageRule::getPk).forEach(usageRulePK -> {
                usageRulePK.setFileSeq(source.getFile().getFileId().getFileSeq());
                usageRulePK.setFileTyp(source.getFile().getFileId().getFileType());
                usageRulePK.setFileSer(source.getFile().getFileId().getFileSeries());
                usageRulePK.setFileNbr(source.getFile().getFileId().getFileNbr());
            });
        }

        if (target.getOwners() != null) {
            target.getOwners().stream().map(IpMarkOwners::getPk).forEach(pks::add);
        }
        if (target.getRepresentatives() != null) {
            target.getRepresentatives().stream().map(IpMarkReprs::getPk).forEach(pks::add);

        }
        if (Objects.nonNull(target.getMarkEFilingData())){
            target.getMarkEFilingData().setPk(target.getPk());
        }
        if (Objects.nonNull(target.getAcpDetails())) {
            target.getAcpDetails().setPk(target.getPk());
        }

        if (Objects.nonNull(target.getAcpServicePerson()) && Objects.nonNull(target.getAcpServicePerson().getServicePerson())) {
            target.getAcpServicePerson().setPk(target.getPk());
        }

        if (Objects.nonNull(target.getAcpInfringerPerson()) && Objects.nonNull(target.getAcpInfringerPerson().getInfringerPerson())) {
            target.getAcpInfringerPerson().setPk(target.getPk());
        }

        if (Objects.nonNull(target.getAcpAdministrativePenalty())) {
            target.getAcpAdministrativePenalty().setPk(target.getPk());
        }

        if (Objects.nonNull(target.getAcpCheckData())) {
            target.getAcpCheckData().setPk(target.getPk());
        }

        if (target.getIpMarkRegulation() != null) {
            target.getIpMarkRegulation().setPk(new IpFilePK());
            pks.add(target.getIpMarkRegulation().getPk());
        }
        if (target.getPriorities() != null) {
            target.getPriorities().stream().map(IpMarkPriorities::getPk).forEach(pks::add);
        }
        if (target.getLogo() != null) {
            target.getLogo().setPk(new IpFilePK());
            pks.add(target.getLogo().getPk());
            if (target.getLogo().getIpLogoViennaClassesCollection() != null) {
                target.getLogo().getIpLogoViennaClassesCollection().stream().map(IpLogoViennaClasses::getPk).forEach(pks::add);
            }
        }

        if (target.getRelationshipExtended() != null) {
            target.getRelationshipExtended().setPk(new IpFilePK());
            pks.add(target.getRelationshipExtended().getPk());
        }

        List<IpMarkAttachment> attachments = target.getAttachments();
        if (!CollectionUtils.isEmpty(attachments)) {
            for (IpMarkAttachment attachment : attachments) {
                attachment.setFileSeq(source.getFile().getFileId().getFileSeq());
                attachment.setFileTyp(source.getFile().getFileId().getFileType());
                attachment.setFileSer(source.getFile().getFileId().getFileSeries());
                attachment.setFileNbr(source.getFile().getFileId().getFileNbr());

                List<IpMarkAttachmentViennaClasses> viennaClassList = attachment.getViennaClassList();
                if (!CollectionUtils.isEmpty(viennaClassList)) {
                    for (IpMarkAttachmentViennaClasses ipMarkAttachmentViennaClasses : viennaClassList) {
                        ipMarkAttachmentViennaClasses.setMarkAttachment(attachment);
                        ipMarkAttachmentViennaClasses.getPk().setAttachmentId(attachment.getId());
                        ipMarkAttachmentViennaClasses.getPk().setFileSeq(source.getFile().getFileId().getFileSeq());
                        ipMarkAttachmentViennaClasses.getPk().setFileTyp(source.getFile().getFileId().getFileType());
                        ipMarkAttachmentViennaClasses.getPk().setFileSer(source.getFile().getFileId().getFileSeries());
                        ipMarkAttachmentViennaClasses.getPk().setFileNbr(source.getFile().getFileId().getFileNbr());
                    }
                }
            }
        }

        pks.forEach(r -> fileSeqTypSerNbrMapper.toEntity(source.getFile().getFileId(), r));

    }

    @AfterMapping
    protected void fillAdditionalDataAfterPrimaryKeys(@MappingTarget IpMark target, CMark source) {
        afterToEntityUsageRuleData(target);
    }
    //end of markToIpMark custom methods

    private void afterToCoreAttachmentData(@MappingTarget CMark target, IpMark source, @Context Boolean addAttachmentsData) {
        target.getSignData().setAttachments(createCoreAttachments(source, addAttachmentsData));
    }

    public List<CMarkAttachment> createCoreAttachments(IpMark source, Boolean addAttachmentsData) {
        List<CMarkAttachment> attachments = new ArrayList<>();

        if (MarkSignTypeUtils.isMarkContainImages(MarkSignType.selectByCode(source.getSignWcode()))) {
            IpLogo logo = source.getLogo();
            if (Objects.nonNull(logo)) {
                CMarkAttachment cMarkAttachment = markAttachmentLogoMapper.toCore(logo, addAttachmentsData);
                attachments.add(cMarkAttachment);
            }
        }


        List<IpMarkAttachment> sourceAtts = source.getAttachments();
        if (!CollectionUtils.isEmpty(sourceAtts)) {
            for (IpMarkAttachment attachment : sourceAtts) {
                attachments.add(markAttachmentMapper.toCore(attachment, addAttachmentsData));
            }
        }
        return attachments;
    }


    private void afterToEntityUsageRuleData(@MappingTarget IpMark target) {
        if (!CollectionUtils.isEmpty(target.getUsageRules())) {
            for (IpMarkUsageRule usageRule : target.getUsageRules()) {
                if (Objects.isNull(usageRule.getContent())) {
                    IpMarkUsageRulePK pk = usageRule.getPk();
                    usageRule.setContent(markUsageRuleService.findMarkUsageRule(pk.getId(), pk.getType(), pk.getFileSeq(), pk.getFileTyp(), pk.getFileSer(), pk.getFileNbr(), true).getContent());
                }
            }
        }
    }

    private void afterToEntityAttachmentData(@MappingTarget IpMark target, CMark source) {
        List<IpMarkAttachment> ipMarkAttachments = target.getAttachments();
        if (Objects.isNull(ipMarkAttachments))
            target.setAttachments(new ArrayList<>());

        MarkSignType signType = source.getSignData().getSignType();
        if (MarkSignTypeUtils.isMarkContainImages(signType)) {
            CMarkAttachment markLogo = MarkSignDataAttachmentUtils.selectFirstImageAttachment(source.getSignData());
            if (Objects.nonNull(markLogo)) {
                IpLogo ipLogo = markAttachmentLogoMapper.toEntity(markLogo);
                target.setLogo(ipLogo);
            }
        }

        if (MarkSignTypeUtils.isMarkContainAudio(signType)) {
            List<CMarkAttachment> markAttachments = MarkSignDataAttachmentUtils.selectAudiosFromAttachments(source.getSignData().getAttachments());
            fillMarkAttachment(target, markAttachments);
        }

        if (MarkSignTypeUtils.isMarkContainVideo(signType)) {
            List<CMarkAttachment> markAttachments = MarkSignDataAttachmentUtils.selectVideosFromAttachments(source.getSignData().getAttachments());
            fillMarkAttachment(target, markAttachments);
        }
    }

    private void fillMarkAttachment(IpMark target, List<CMarkAttachment> markAttachments) {
        if (!CollectionUtils.isEmpty(markAttachments)) {
            for (CMarkAttachment sound : markAttachments) {
                IpMarkAttachment soundEntity = markAttachmentMapper.toEntity(sound);
                List<IpMarkAttachment> attachments = target.getAttachments();
                if (Objects.isNull(attachments)) {
                    target.setAttachments(new ArrayList<>());
                }
                target.getAttachments().add(soundEntity);
            }
        }
    }

}
