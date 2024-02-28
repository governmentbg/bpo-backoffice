package com.duosoft.ipas.webmodel.structure;


import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.userdoc.grounds.*;
import bg.duosoft.ipas.core.service.nomenclature.*;
import bg.duosoft.ipas.core.service.patent.DesignService;
import bg.duosoft.ipas.enums.ApplTyp;
import bg.duosoft.ipas.enums.MarkSignType;
import bg.duosoft.ipas.util.general.BasicUtils;
import com.duosoft.ipas.util.json.GroundNiceClasses;
import com.duosoft.ipas.webmodel.RelatedUserdocPanelGroundTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserdocRootGroundWebModel {
    private List<RelatedUserdocPanelGroundTypes> legalGroundsTypes;
    private List<String> selectedGroundTypeIds;
    private String motives;
    private String panel;
    private Integer rootGroundId;
    private Integer earlierRightType;
    private Integer applicantAuthority;
    private String groundCommonText;
    private List<GroundSingleDesignWebModel> singleDesigns;
    private MarkGroundDataWebModel markGroundData;
    private PatentGroundDataWebModel patentGroundData;

    public static void initPatentGroundPartialInvalidityWebData(UserdocRootGroundWebModel groundWebModel, CUserdocRootGrounds cUserdocRootGround){
        groundWebModel.setPatentGroundData(new PatentGroundDataWebModel());
        PatentGroundDataWebModel patentGroundData=groundWebModel.getPatentGroundData();
        CPatentGroundData cPatentGroundData=cUserdocRootGround.getPatentGroundData();
        if(Objects.nonNull(cPatentGroundData) && Objects.nonNull(cPatentGroundData.getPartialInvalidity())){
            patentGroundData.setPartialInvalidity(cPatentGroundData.getPartialInvalidity());
        }else{
            patentGroundData.setPartialInvalidity(false);
        }
    }

    public static void initSingleDesignsWebData(UserdocRootGroundWebModel groundWebModel, CUserdocRootGrounds cUserdocRootGround){
        if (!CollectionUtils.isEmpty(cUserdocRootGround.getSingleDesignGroundData())){
            List<GroundSingleDesignWebModel> singleDesings=new ArrayList<>();
            cUserdocRootGround.getSingleDesignGroundData().stream().forEach(r->{
                GroundSingleDesignWebModel groundSingleDesignWebModel=new GroundSingleDesignWebModel(r.getSingleDesign().getFile().getFileId().createFilingNumber(),r.getSingleDesign().getTechnicalData().getTitle());
                singleDesings.add(groundSingleDesignWebModel);
            });
            groundWebModel.setSingleDesigns(singleDesings);
        }
    }

    public static void initSingleDesignsCoreData(CUserdocRootGrounds cUserdocRootGround , UserdocRootGroundWebModel rootGroundModalData, DesignService designService){
        if (!CollectionUtils.isEmpty(rootGroundModalData.getSingleDesigns())){
            List<CSingleDesignGroundData> singleDesigns=new ArrayList<>();
            rootGroundModalData.getSingleDesigns().stream().forEach(r->{
             CFileId singleDesignId= BasicUtils.createCFileId(r.getId());
             CPatent singleDesign = designService.findSingleDesign(singleDesignId.getFileSeq(),singleDesignId.getFileType(),singleDesignId.getFileSeries(),singleDesignId.getFileNbr(),false);
             singleDesigns.add(new CSingleDesignGroundData(singleDesign));
           });
            cUserdocRootGround.setSingleDesignGroundData(singleDesigns);
        }
    }

    public static void initPatentGroundPartialInvalidityCoreData(CUserdocRootGrounds cUserdocRootGround , UserdocRootGroundWebModel rootGroundModalData){
        cUserdocRootGround.setPatentGroundData(new CPatentGroundData());
        cUserdocRootGround.getPatentGroundData().setPartialInvalidity(rootGroundModalData.getPatentGroundData().getPartialInvalidity());
    }



    public static void initCoreOnEarlierTypeActive(CUserdocRootGrounds cUserdocRootGround , UserdocRootGroundWebModel rootGroundModalData,
                                                   CountryService countryService, ApplicantAuthorityService applicantAuthorityService,
                                                   EarlierRightTypesService earlierRightTypesService, MarkGroundTypeService markGroundTypeService,
                                                   ApplicationTypeService applicationTypeService){

        if(Objects.nonNull(rootGroundModalData.getEarlierRightType())){
            if (Objects.isNull(cUserdocRootGround.getMarkGroundData())){
                cUserdocRootGround.setMarkGroundData(new CMarkGroundData());
            }
            CMarkGroundData markGroundData = cUserdocRootGround.getMarkGroundData();
            MarkGroundDataWebModel markGroundDataWebModel = rootGroundModalData.getMarkGroundData();
            markGroundData.setRegistrationCountry(Objects.nonNull(markGroundDataWebModel.getCountryCode())?countryService.findById(markGroundDataWebModel.getCountryCode()):null);
            markGroundData.setNiceClassesInd(markGroundDataWebModel.getNiceClassesInd());
            markGroundData.setMarkImportedInd(markGroundDataWebModel.getMarkImportedInd());
            markGroundData.setUserdocGroundsNiceClasses(new ArrayList<>());
            if (!Objects.isNull(markGroundDataWebModel.getNiceClasses())){
                markGroundDataWebModel.getNiceClasses().forEach(r->{
                    CGroundNiceClasses cGroundNiceClasses=new CGroundNiceClasses();
                    cGroundNiceClasses.setNiceClassCode(r.getNiceClassCode());
                    cGroundNiceClasses.setNiceClassDescription(r.getNiceClassDescription());
                    markGroundData.getUserdocGroundsNiceClasses().add(cGroundNiceClasses);
                });
            }else{
                markGroundData.setUserdocGroundsNiceClasses(new ArrayList<>());
            }
            cUserdocRootGround.setApplicantAuthority(Objects.nonNull(rootGroundModalData.getApplicantAuthority())?applicantAuthorityService.findById(rootGroundModalData.getApplicantAuthority()):null);
            cUserdocRootGround.setEarlierRightType(earlierRightTypesService.findById(rootGroundModalData.getEarlierRightType()));
            markGroundData.setMarkGroundType(Objects.nonNull(markGroundDataWebModel.getMarkGroundType())?markGroundTypeService.findById(markGroundDataWebModel.getMarkGroundType()):null);
            cUserdocRootGround.setGroundCommonText(rootGroundModalData.getGroundCommonText());
            markGroundData.setRegistrationDate(markGroundDataWebModel.getRegistrationDate());
            markGroundData.setRegistrationNbr(markGroundDataWebModel.getRegistrationNbr());
            markGroundData.setFilingNumber(markGroundDataWebModel.getFilingNumber());
            markGroundData.setFilingDate(markGroundDataWebModel.getFilingDate());
            markGroundData.setNameText(markGroundDataWebModel.getNameText());
            markGroundData.setMarkSignTyp(Objects.nonNull(markGroundDataWebModel.getMarkSignTyp()) && !markGroundDataWebModel.getMarkSignTyp().isEmpty()?MarkSignType.selectByCode(markGroundDataWebModel.getMarkSignTyp()):null);
            markGroundData.setGeographicalIndTyp(Objects.nonNull(markGroundDataWebModel.getGeographicalIndTyp()) && !markGroundDataWebModel.getGeographicalIndTyp().isEmpty()?applicationTypeService.getApplicationSubtype(ApplTyp.GEOGRAPHICAL_INDICATION_TYPE,markGroundDataWebModel.getGeographicalIndTyp()):null);
        }

    }

    public static void initWebModelOnEarlierTypeActive(UserdocRootGroundWebModel groundWebModel, CUserdocRootGrounds cUserdocRootGround){
        if (Objects.isNull(groundWebModel.getMarkGroundData())){
            groundWebModel.setMarkGroundData(new MarkGroundDataWebModel());
        }
        CMarkGroundData markGroundData = cUserdocRootGround.getMarkGroundData();
        MarkGroundDataWebModel markGroundDataWebModel = groundWebModel.getMarkGroundData();

        groundWebModel.setGroundCommonText(cUserdocRootGround.getGroundCommonText());
        if (Objects.nonNull(markGroundData)){
            markGroundDataWebModel.setRegistrationNbr(markGroundData.getRegistrationNbr());
            markGroundDataWebModel.setRegistrationDate(markGroundData.getRegistrationDate());
            markGroundDataWebModel.setNiceClassesInd(markGroundData.getNiceClassesInd());
            markGroundDataWebModel.setMarkImportedInd(markGroundData.getMarkImportedInd());
            markGroundDataWebModel.setNameText(markGroundData.getNameText());
            if (Objects.nonNull(markGroundData.getGeographicalIndTyp())){
                markGroundDataWebModel.setGeographicalIndTyp(markGroundData.getGeographicalIndTyp().getApplicationSubType());
            }
            if (Objects.nonNull(markGroundData.getMarkSignTyp())){
                markGroundDataWebModel.setMarkSignTyp(markGroundData.getMarkSignTyp().code());
            }
            List<CGroundNiceClasses> userdocGroundsNiceClasses = markGroundData.getUserdocGroundsNiceClasses();
            if (!Objects.isNull(userdocGroundsNiceClasses)){
                markGroundDataWebModel.setNiceClasses(new ArrayList<>());
                for (CGroundNiceClasses userdocGroundsNiceClass : userdocGroundsNiceClasses) {
                    markGroundDataWebModel.getNiceClasses().add(new GroundNiceClasses(userdocGroundsNiceClass.getNiceClassCode(),userdocGroundsNiceClass.getNiceClassDescription()));
                }
            }
            markGroundDataWebModel.setFilingNumber(markGroundData.getFilingNumber());
            markGroundDataWebModel.setFilingDate(markGroundData.getFilingDate());
            markGroundDataWebModel.setMarkGroundType(markGroundData.getMarkGroundType()!=null?markGroundData.getMarkGroundType().getId():null);
            markGroundDataWebModel.setCountryCode(markGroundData.getRegistrationCountry()!=null ?markGroundData.getRegistrationCountry().getCountryCode():null);
        }

        groundWebModel.setEarlierRightType(cUserdocRootGround.getEarlierRightType() != null?cUserdocRootGround.getEarlierRightType().getId():null);
        groundWebModel.setApplicantAuthority(cUserdocRootGround.getApplicantAuthority()!=null ? cUserdocRootGround.getApplicantAuthority().getId():null);

    }
}
