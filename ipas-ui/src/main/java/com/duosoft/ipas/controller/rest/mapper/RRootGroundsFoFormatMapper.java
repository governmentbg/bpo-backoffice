package com.duosoft.ipas.controller.rest.mapper;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.userdoc.grounds.*;
import bg.duosoft.ipas.core.service.nomenclature.*;
import bg.duosoft.ipas.enums.GeographicalIndType;
import bg.duosoft.ipas.enums.MarkSignType;
import bg.duosoft.ipas.rest.custommodel.userdoc.grounds.RMarkGroundDataFoFormat;
import bg.duosoft.ipas.rest.custommodel.userdoc.grounds.RRootGroundsFoFormatNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class RRootGroundsFoFormatMapper {

    @Autowired
    private RPatentMapper patentMapper;

    @Autowired
    private EarlierRightTypesService earlierRightTypesService;

    @Autowired
    private MarkGroundTypeService markGroundTypeService;

    @Autowired
    private LegalGroundCategoriesService legalGroundCategoriesService;

    @Autowired
    private ApplicantAuthorityService applicantAuthorityService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private LegalGroundTypesService legalGroundTypesService;

    @Autowired
    private RGroundNiceClassesMapper rGroundNiceClassesMapper;


    public List<CUserdocRootGrounds> toCoreList(List<RRootGroundsFoFormatNew> foGrounds){
        List<CUserdocRootGrounds> grounds= new ArrayList<>();
        for (RRootGroundsFoFormatNew foGround:foGrounds) {
            CUserdocRootGrounds ground=new CUserdocRootGrounds();
            convertToCGroundData(ground,foGround);
            grounds.add(ground);
        }
        return grounds;
    }

    private void fillMarkGroundData(CUserdocRootGrounds ground, RRootGroundsFoFormatNew foGround){
        CMarkGroundData markGroundData=new CMarkGroundData();
        RMarkGroundDataFoFormat markGroundDatFoFormat = foGround.getMarkGroundDataFoFormat();
        ground.setMarkGroundData(markGroundData);

        markGroundData.setRegistrationNbr(markGroundDatFoFormat.getRegistrationNbr());
        markGroundData.setRegistrationDate(markGroundDatFoFormat.getRegistrationDate());
        markGroundData.setNiceClassesInd(markGroundDatFoFormat.getNiceClassesInd());
        markGroundData.setMarkImportedInd(markGroundDatFoFormat.getMarkImported());
        markGroundData.setNameText(markGroundDatFoFormat.getNameText());
        markGroundData.setNameData(markGroundDatFoFormat.getNameData());
        markGroundData.setFilingNumber(markGroundDatFoFormat.getFilingNumber());
        markGroundData.setFilingDate(markGroundDatFoFormat.getFilingDate());

        if (Objects.nonNull(markGroundDatFoFormat.getMarkGroundType())){
            markGroundData.setMarkGroundType(markGroundTypeService.findById(markGroundDatFoFormat.getMarkGroundType()));
        }
        if (Objects.nonNull(markGroundDatFoFormat.getGroundCategoryCode()) && !markGroundDatFoFormat.getGroundCategoryCode().isEmpty()){
            markGroundData.setLegalGroundCategory(legalGroundCategoriesService.findById(markGroundDatFoFormat.getGroundCategoryCode()));
        }
        if (Objects.nonNull(markGroundDatFoFormat.getRegCountry()) && !markGroundDatFoFormat.getRegCountry().isEmpty()){
            markGroundData.setRegistrationCountry(countryService.findById(markGroundDatFoFormat.getRegCountry()));
        }
        if (Objects.nonNull(markGroundDatFoFormat.getGeographicalIndicationTypeDescription()) && !markGroundDatFoFormat.getGeographicalIndicationTypeDescription().isEmpty()){
            GeographicalIndType geographicalIndType = GeographicalIndType.selectByDescription(markGroundDatFoFormat.getGeographicalIndicationTypeDescription());
            markGroundData.setGeographicalIndTyp(applicationTypeService.getApplicationSubtype(geographicalIndType.applTyp(),geographicalIndType.applSubTyp()));
        }
        if (Objects.nonNull(markGroundDatFoFormat.getMarkSignTypeDescription()) && !markGroundDatFoFormat.getMarkSignTypeDescription().isEmpty()){
            MarkSignType markSignType = MarkSignType.selectByDescriptionEn(markGroundDatFoFormat.getMarkSignTypeDescription());
            markGroundData.setMarkSignTyp(markSignType);
        }
        markGroundData.setUserdocGroundsNiceClasses(rGroundNiceClassesMapper.toCoreList(markGroundDatFoFormat.getUserdocGroundsNiceClasses()));

    }
    private void fillCommonGroundData(CUserdocRootGrounds ground, RRootGroundsFoFormatNew foGround){
        ground.setMotives(foGround.getMotives());
        ground.setRootGroundId(foGround.getRootGroundId());
        ground.setGroundCommonText(foGround.getCommonText());
        if (Objects.nonNull(foGround.getEarlierEntitlementRightType()) && !foGround.getEarlierEntitlementRightType().isEmpty()){
            ground.setEarlierRightType(earlierRightTypesService.findTypeByVersionAndCode(foGround.getLegalActVersion(),foGround.getEarlierEntitlementRightType()));
        }
        if (Objects.nonNull(foGround.getOpponentEntitlementKind()) && !foGround.getOpponentEntitlementKind().isEmpty()){
            ground.setApplicantAuthority(applicantAuthorityService.findByCodeAndVersion(foGround.getLegalActVersion(),foGround.getOpponentEntitlementKind()));
        }
        ground.setUserdocSubGrounds(new ArrayList<>());
        for (String articleReference:foGround.getArticleReferences()) {
            List<CLegalGroundTypes> allLegalGroundsByVersionAndCode = legalGroundTypesService.findAllLegalGroundsByVersionAndCode(foGround.getLegalActVersion(), articleReference);
            for (CLegalGroundTypes legalGroundType:allLegalGroundsByVersionAndCode) {
                CUserdocSubGrounds subGround=new CUserdocSubGrounds();
                subGround.setLegalGroundTypeId(legalGroundType.getId());
                subGround.setRootGroundId(ground.getRootGroundId());
                ground.getUserdocSubGrounds().add(subGround);
            }
        }
    }

    private void fillPatentGroundData(CUserdocRootGrounds ground, RRootGroundsFoFormatNew foGround){
        ground.setPatentGroundData(new CPatentGroundData(foGround.getPatentGroundDataFoFormat().getPartialInvalidity()));
    }


    private void fillSingleDesignGroundData(CUserdocRootGrounds ground, RRootGroundsFoFormatNew foGround){
       List<CSingleDesignGroundData> singleDesignsGroundData = new ArrayList<>();
        foGround.getSingleDesignGroundDataFoFormat().stream().forEach(r->{
            CPatent singleDesign = patentMapper.toCore(r.getSingleDesign());
            singleDesignsGroundData.add(new CSingleDesignGroundData(singleDesign));
        });
        ground.setSingleDesignGroundData(singleDesignsGroundData);
    }

    private void convertToCGroundData(CUserdocRootGrounds ground, RRootGroundsFoFormatNew foGround){
        fillCommonGroundData(ground,foGround);
        if(Objects.nonNull(foGround.getMarkGroundDataFoFormat())){
            fillMarkGroundData(ground,foGround);
        }
        if (Objects.nonNull(foGround.getPatentGroundDataFoFormat())){
            fillPatentGroundData(ground,foGround);
        }
        if (!CollectionUtils.isEmpty(foGround.getSingleDesignGroundDataFoFormat())){
            fillSingleDesignGroundData(ground,foGround);
        }

    }
}
