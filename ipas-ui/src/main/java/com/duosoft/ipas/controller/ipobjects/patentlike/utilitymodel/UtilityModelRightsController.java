package com.duosoft.ipas.controller.ipobjects.patentlike.utilitymodel;

import bg.duosoft.ipas.core.model.ebddownload.CEbdPatent;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CRelationship;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.util.TempID;
import bg.duosoft.ipas.core.service.nomenclature.CountryService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.search.IpoSearchService;
import bg.duosoft.ipas.enums.PatentRelationshipExtApplType;
import bg.duosoft.ipas.enums.RelationshipDirection;
import bg.duosoft.ipas.enums.RelationshipType;
import bg.duosoft.ipas.integration.ebddownload.service.EbdPatentService;
import bg.duosoft.ipas.util.DataConverter;
import com.duosoft.ipas.controller.ipobjects.patentlike.common.PatentLikeRightsController;
import com.duosoft.ipas.util.CFileRelationshipUtils;
import com.duosoft.ipas.util.PersonUtils;
import com.duosoft.ipas.util.json.UtilityModelParallelData;
import com.duosoft.ipas.util.json.UtilityModelRightsData;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import static bg.duosoft.ipas.enums.RelationshipType.PARALLEL_PATENT_TYPE;
import static bg.duosoft.ipas.enums.RelationshipType.TRANSFORMED_NATIONAL_PATENT_TYPE;

/**
 * User: Georgi
 * Date: 5.12.2019 г.
 * Time: 14:34
 */
@Controller
@RequestMapping("/utility_model")
public class UtilityModelRightsController extends PatentLikeRightsController<UtilityModelRightsData> {

    @Autowired
    private CountryService countryService;

    @Autowired
    private IpoSearchService searchService;

    @Autowired
    private PatentService patentService;

    @Autowired
    private EbdPatentService ebdPatentService;

    @Autowired
    private TempID tempID;

    public UtilityModelRightsController() {
        super(UtilityModelRightsData.class);
    }


    @PostMapping("/load-transformation-body")
    public String loadTransformationBody(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier) {
        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        model.addAttribute("countryMap",countryService.getCountryMap());
        model.addAttribute("patent",patent);
        model.addAttribute("relationshipType",TRANSFORMED_NATIONAL_PATENT_TYPE);
        CFileRelationshipUtils.supplyViewWithNationalPatentTransformationData(searchService, model, patent.getFile(),"nationalPatentRelationshipData");
        return "ipobjects/patentlike/common/rights/transformation_like_body :: transformation_like_body";
    }

    @PostMapping("/load-parallel-um-body")
    public String loadParallelUmBody(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier) {
        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        model.addAttribute("countryMap",countryService.getCountryMap());
        model.addAttribute("patent",patent);
        model.addAttribute("relationshipType", PARALLEL_PATENT_TYPE);
        CFileRelationshipUtils.supplyViewWithParallelUtilityModelData(searchService, model, patent.getFile(),"nationalPatentRelationshipData");;
        return "ipobjects/patentlike/common/rights/transformation_like_body :: transformation_like_body";
    }


    @Override
    protected void processAdditionalTransformation(CPatent patent, UtilityModelRightsData rights) {
        updateRelationshipData(rights.getTransformationData() != null && rights.getTransformationData().isHasTransformationData() && "BG".equals(rights.getTransformationData().getApplicationType()) ? rights.getTransformationData().getNationalPatentTransformationData() : null, patent.getFile(), TRANSFORMED_NATIONAL_PATENT_TYPE);
    }

    private void processNationalParallelRelationshipData(CPatent patent, UtilityModelRightsData rights){
        UtilityModelParallelData utilityModelParallelData = rights.getUtilityModelParallelData();
        CFileId cFileId = new CFileId(utilityModelParallelData.getFileSeq(),utilityModelParallelData.getFileType(),utilityModelParallelData.getFileSeries(),utilityModelParallelData.getFileNbr());
        CPatent dbPatent = patentService.findPatent(cFileId,false);
        if (dbPatent!=null){
            updateAuthorsOnRightsPanelSave(patent,dbPatent.getAuthorshipData());
            recalculateEntitlementAndExpirationDate(patent.getFile(),dbPatent.getFile().getFilingData()==null? null:dbPatent.getFile().getFilingData().getFilingDate());
        }
    }

    private void processEpParallelRelationshipData(CPatent patent, UtilityModelRightsData rights){
        CEbdPatent ebdPatent = ebdPatentService.selectByFileNumber(StringUtils.isEmpty(rights.getUtilityModelParallelData().getFilingNumber()) ? null : DataConverter.parseInteger(rights.getUtilityModelParallelData().getFilingNumber(), null));
        if (ebdPatent != null) {
            recalculateEntitlementAndExpirationDate(patent.getFile(),ebdPatent.getPctApplicationData() == null || ebdPatent.getPctApplicationData().getPctApplicationDate() == null ? ebdPatent.getFilingDate() : ebdPatent.getPctApplicationData().getPctApplicationDate());
            updateAuthorsOnRightsPanelSave(patent,ebdPatent.getAuthorshipData());
        }
    }

    @Override
    protected boolean processSpecificObjectChanges(CPatent patent, UtilityModelRightsData rights) {
        boolean hasChanges = false;
        if (isValidEpParallelRelationshipData(patent,rights)){
            processEpParallelRelationshipData(patent,rights);
            hasChanges = true;
        }
        if (isValidNationalParallelRelationshipData(patent,rights)){
            processNationalParallelRelationshipData(patent,rights);
            hasChanges = true;
        }
        if (isValidInternationalParallelRelationshipData(patent,rights)){
            recalculateEntitlementAndExpirationDate(patent.getFile(),rights.getUtilityModelParallelData().getFilingDate());
            hasChanges = true;
        }
        updatePatentParallelRelationshipData(patent,rights);
        return hasChanges;
    }

    private void updatePatentParallelRelationshipData(CPatent patent, UtilityModelRightsData rightsData){
        if (rightsData.getUtilityModelParallelData() != null && rightsData.getUtilityModelParallelData().isHasParallelData() && !"BG".equals(rightsData.getUtilityModelParallelData().getApplicationType())) {//bg transformaciqta otiva kato relation, zatova se maha ot tuk!
            UtilityModelParallelData utilityModelParallelData = rightsData.getUtilityModelParallelData();
            patent.setRelationshipExtended(utilityModelParallelData.toParallelUtilityModelRelationship());
        }
        else {
            if (!Objects.isNull(patent.getRelationshipExtended()) &&
                !Objects.isNull(patent.getRelationshipExtended().getRelationshipType()) &&
                patent.getRelationshipExtended().getRelationshipType().equals(PARALLEL_PATENT_TYPE)){
                patent.setRelationshipExtended(null);
            }
        }
        updateRelationshipData(rightsData.getUtilityModelParallelData() != null && rightsData.getUtilityModelParallelData().isHasParallelData() && "BG".equals(rightsData.getUtilityModelParallelData().getApplicationType()) ? rightsData.getUtilityModelParallelData().getNationalParallelData() : null, patent.getFile(), PARALLEL_PATENT_TYPE);
    }

    private boolean isValidEpParallelRelationshipData(CPatent patent, UtilityModelRightsData rights){
        return rights.getUtilityModelParallelData() != null && PatentRelationshipExtApplType.EUROPEAN_PATENT.code().equals(rights.getUtilityModelParallelData().getApplicationType()) && !StringUtils.isEmpty(rights.getUtilityModelParallelData().getFilingNumber()) && !Objects.equals(rights.getUtilityModelParallelData().getFilingNumber(), patent.getRelationshipExtended() == null ? null : PatentRelationshipExtApplType.EUROPEAN_PATENT.code().equals(patent.getRelationshipExtended().getApplicationType()) ? patent.getRelationshipExtended().getFilingNumber() : null);
    }

    private boolean isValidNationalParallelRelationshipData(CPatent patent, UtilityModelRightsData rights){
        if ((rights.getUtilityModelParallelData() != null && PatentRelationshipExtApplType.NATIONAL_PATENT.code().equals(rights.getUtilityModelParallelData().getApplicationType()))){
            UtilityModelRightsData utilityModelRightsData= rights;
            UtilityModelParallelData utilityModelParallelData = utilityModelRightsData.getUtilityModelParallelData();
            if (!Objects.isNull(utilityModelParallelData.getFileNbr())){
                if(CollectionUtils.isEmpty(patent.getFile().getRelationshipList())){
                    return true;
                }
                for (CRelationship r: patent.getFile().getRelationshipList()) {
                    if (r.getRelationshipRole().equals(RelationshipDirection.RELATIONSHIP_CONTAINS_SUPER_OBJECTS_ROLE)
                            && r.getRelationshipType().equals(RelationshipType.PARALLEL_PATENT_TYPE)
                            && r.getFileId().getFileNbr().equals(utilityModelParallelData.getFileNbr())
                            && r.getFileId().getFileSeq().equals(utilityModelParallelData.getFileSeq())
                            && r.getFileId().getFileSeries().equals(utilityModelParallelData.getFileSeries())
                            && r.getFileId().getFileType().equals(utilityModelParallelData.getFileType())){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean isValidInternationalParallelRelationshipData(CPatent patent, UtilityModelRightsData rights){
        return rights.getUtilityModelParallelData() != null && PatentRelationshipExtApplType.INTERNATIONAL_PATENT.code().equals(rights.getUtilityModelParallelData().getApplicationType())
                && !StringUtils.isEmpty(rights.getUtilityModelParallelData().getFilingNumber())
                && !Objects.isNull(rights.getUtilityModelParallelData().getFilingDate())
                && (!Objects.equals(rights.getUtilityModelParallelData().getFilingDate(),patent.getRelationshipExtended() == null ? null : PatentRelationshipExtApplType.INTERNATIONAL_PATENT.code().equals(patent.getRelationshipExtended().getApplicationType()) ? patent.getRelationshipExtended().getFilingDate() : null)
                || !Objects.equals(rights.getUtilityModelParallelData().getFilingNumber(), patent.getRelationshipExtended() == null ? null : PatentRelationshipExtApplType.INTERNATIONAL_PATENT.code().equals(patent.getRelationshipExtended().getApplicationType()) ? patent.getRelationshipExtended().getFilingNumber() : null));
    }


    @Override
    protected void initAdditionalData(CPatent patent, Model model) {
        CFileRelationshipUtils.supplyViewWithNationalPatentTransformationData(searchService, model, patent.getFile());
        CFileRelationshipUtils.supplyViewWithParallelUtilityModelData(searchService, model, patent.getFile());
    }
}
