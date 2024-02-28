package com.duosoft.ipas.controller.ipobjects.patentlike.common;

import bg.duosoft.ipas.core.model.ebddownload.CEbdPatent;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CPriorityData;
import bg.duosoft.ipas.core.model.file.CRelationship;
import bg.duosoft.ipas.core.model.patent.CAuthorshipData;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CPctApplicationData;
import bg.duosoft.ipas.core.model.util.TempID;
import bg.duosoft.ipas.core.service.nomenclature.CountryService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.search.IpoSearchService;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.enums.PatentRelationshipExtApplType;
import bg.duosoft.ipas.enums.RelationshipDirection;
import bg.duosoft.ipas.enums.RelationshipType;
import bg.duosoft.ipas.integration.ebddownload.service.EbdPatentService;
import bg.duosoft.ipas.util.DataConverter;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.controller.ipobjects.common.RightsBaseController;
import com.duosoft.ipas.util.CFileRelationshipUtils;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.PersonUtils;
import com.duosoft.ipas.util.RedirectUtils;
import com.duosoft.ipas.util.json.*;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * User: Georgi
 * Date: 5.12.2019 г.
 * Time: 14:33
 */
public abstract class PatentLikeRightsController<T extends PatentRightsData> extends RightsBaseController {
    private Class<T> cls;
    @Autowired
    protected CountryService countryService;

    @Autowired
    protected IpasValidatorCreator validatorCreator;

    @Autowired
    private IpoSearchService searchService;

    @Autowired
    private PatentService patentService;
    @Autowired
    private EbdPatentService ebdPatentService;
    @Autowired
    private TempID tempID;

    protected PatentLikeRightsController(Class<T> cls) {
        this.cls = cls;
    }

    private boolean isForRedirectOnDivisionalDataChanged(T rights, CPatent patent){
        return rights.getDivisionalData() != null && rights.getDivisionalData().isRelationshipChanged();
    }

    private boolean isForRedirectOnPctDataChanged(T rights, CPatent patent){

     String dbApplicationId   = Objects.isNull(patent.getPctApplicationData())?null:patent.getPctApplicationData().getPctApplicationId().trim();
     Date dbApplicationDate = Objects.isNull(patent.getPctApplicationData())?null:patent.getPctApplicationData().getPctApplicationDate();

        if ((rights.getDataPct().isHasPctData()==true) && !Objects.isNull(rights.getDataPct().getPctApplicationId())
                && !Objects.isNull(rights.getDataPct().getPctApplicationDate())
                && (!Objects.equals(rights.getDataPct().getPctApplicationId(),dbApplicationId)
                || !Objects.equals(rights.getDataPct().getPctApplicationDate(),dbApplicationDate))){
            return true;
        }
        return false;
    }

    private boolean isForRedirectOnEpoPatentTransformation(T rights, CPatent patent){
        return rights.getTransformationData() != null && PatentRelationshipExtApplType.EUROPEAN_PATENT.code().equals(rights.getTransformationData().getApplicationType()) && !StringUtils.isEmpty(rights.getTransformationData().getFilingNumber()) && !Objects.equals(rights.getTransformationData().getFilingNumber(), patent.getRelationshipExtended() == null ? null : PatentRelationshipExtApplType.EUROPEAN_PATENT.code().equals(patent.getRelationshipExtended().getApplicationType()) ? patent.getRelationshipExtended().getFilingNumber() : null);
    }

    private boolean isForRedirectOnInternationalPatentTransformation(T rights, CPatent patent){
        return rights.getTransformationData() != null && PatentRelationshipExtApplType.INTERNATIONAL_PATENT.code().equals(rights.getTransformationData().getApplicationType())
                && !StringUtils.isEmpty(rights.getTransformationData().getFilingNumber())
                && !Objects.isNull(rights.getTransformationData().getFilingDate())
                && (!Objects.equals(rights.getTransformationData().getFilingDate(),patent.getRelationshipExtended() == null ? null : PatentRelationshipExtApplType.INTERNATIONAL_PATENT.code().equals(patent.getRelationshipExtended().getApplicationType()) ? patent.getRelationshipExtended().getFilingDate() : null)
                || !Objects.equals(rights.getTransformationData().getFilingNumber(), patent.getRelationshipExtended() == null ? null : PatentRelationshipExtApplType.INTERNATIONAL_PATENT.code().equals(patent.getRelationshipExtended().getApplicationType()) ? patent.getRelationshipExtended().getFilingNumber() : null));
    }

    private boolean isForRedirectOnNationalPatentTransformation(T rights, CPatent patent){
        if ((rights.getTransformationData() != null && PatentRelationshipExtApplType.NATIONAL_PATENT.code().equals(rights.getTransformationData().getApplicationType()))){
            UtilityModelRightsData utilityModelRightsData= (UtilityModelRightsData) rights;
            UtilityModelTransformationData utilityModelTransformationData = utilityModelRightsData.getTransformationData();
            if (!Objects.isNull(utilityModelTransformationData.getFileNbr())){
                if(CollectionUtils.isEmpty(patent.getFile().getRelationshipList())){
                    return true;
                }
                for (CRelationship r: patent.getFile().getRelationshipList()) {
                    if (r.getRelationshipRole().equals(RelationshipDirection.RELATIONSHIP_CONTAINS_SUPER_OBJECTS_ROLE)
                            && r.getRelationshipType().equals(RelationshipType.TRANSFORMED_NATIONAL_PATENT_TYPE)
                            && r.getFileId().getFileNbr().equals(utilityModelTransformationData.getFileNbr())
                            && r.getFileId().getFileSeq().equals(utilityModelTransformationData.getFileSeq())
                            && r.getFileId().getFileSeries().equals(utilityModelTransformationData.getFileSeries())
                            && r.getFileId().getFileType().equals(utilityModelTransformationData.getFileType())){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @PostMapping("/edit-panel-rights")
    public String editRightsPanel(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes,
                                  @RequestParam(required = false) List<String> editedPanels,
                                  @RequestParam(required = false) String data, @RequestParam Boolean isCancel,
                                  @RequestParam String sessionIdentifier) {
        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        if (!isCancel) {
            T rights = JsonUtil.readJson(data, cls);
            boolean redirectToPatentPageFlag = false;
            boolean updateRelationshipData = true;

            if (isForRedirectOnEpoPatentTransformation(rights,patent)) {
                processEpoPatentTransformation(rights, patent);
                redirectToPatentPageFlag = true;
            }
            if (isForRedirectOnNationalPatentTransformation(rights,patent)){
                UtilityModelRightsData utilityModelRightsData= (UtilityModelRightsData) rights;
                UtilityModelTransformationData utilityModelTransformationData = utilityModelRightsData.getTransformationData();
                processNationalPatentTransformation(utilityModelTransformationData,patent);
                redirectToPatentPageFlag = true;
            }
            if(isForRedirectOnInternationalPatentTransformation(rights,patent)){
                recalculateEntitlementAndExpirationDate(patent.getFile(),rights.getTransformationData().getFilingDate());
                redirectToPatentPageFlag = true;
            }
            if (isForRedirectOnPctDataChanged(rights,patent)){
                recalculateEntitlementAndExpirationDate(patent.getFile(),rights.getDataPct().getPctApplicationDate());
                redirectToPatentPageFlag = true;
            }
            if (isForRedirectOnDivisionalDataChanged(rights,patent)) {
                processDivisionalApplication(rights, patent);
                redirectToPatentPageFlag = true;
                updateRelationshipData=false;
            }

            if (processSpecificObjectChanges(patent,rights)){
                redirectToPatentPageFlag = true;
            }

            updateClaimsPanelFromRequestForm(rights, patent, request, sessionIdentifier,updateRelationshipData);
            if(redirectToPatentPageFlag){
                return redirectToPatentPage(redirectAttributes, patent, sessionIdentifier, editedPanels);
            }else{
                return prepareAjaxPage(patent, model);
            }
        } else {
            return prepareAjaxPage(patent, model);
        }
    }




    //transfers the claims section from the master to the child patent
    private void processDivisionalApplication(T rights, CPatent patent) {
        CRelationship divisionalRelationship = updateRelationshipData(rights.getDivisionalData(), patent.getFile(), CFileRelationshipUtils.fileTypeToDivisionalRelationshipType(patent.getFile().getFileId().getFileType()));
        transferClaimsDataFromMasterPatentToChildPatent(divisionalRelationship.getFileId(), patent);
        processAdditionalTransformation(patent, rights);
    }

    private void processNationalPatentTransformation(UtilityModelTransformationData utilityModelTransformationData, CPatent patent) {
        CFileId cFileId = new CFileId(utilityModelTransformationData.getFileSeq(),utilityModelTransformationData.getFileType(),utilityModelTransformationData.getFileSeries(),utilityModelTransformationData.getFileNbr());
        CPatent dbPatent = patentService.findPatent(cFileId,false);
        if (dbPatent!=null){
            updateAuthorsOnRightsPanelSave(patent,dbPatent.getAuthorshipData());
            recalculateEntitlementAndExpirationDate(patent.getFile(),dbPatent.getFile().getFilingData()==null? null:dbPatent.getFile().getFilingData().getFilingDate());
        }
    }

    private void processEpoPatentTransformation(T rights, CPatent patent) {
        CEbdPatent ebdPatent = ebdPatentService.selectByFileNumber(StringUtils.isEmpty(rights.getTransformationData().getFilingNumber()) ? null : DataConverter.parseInteger(rights.getTransformationData().getFilingNumber(), null));
        if (ebdPatent != null) {
            recalculateEntitlementAndExpirationDate(patent.getFile(),ebdPatent.getPctApplicationData() == null || ebdPatent.getPctApplicationData().getPctApplicationDate() == null ? ebdPatent.getFilingDate() : ebdPatent.getPctApplicationData().getPctApplicationDate());
            if (patent.getFile().getPriorityData() == null) {
                patent.getFile().setPriorityData(new CPriorityData());
            }
            patent.getFile().getPriorityData().setParisPriorityList(ebdPatent.getParisPriorities());
            patent.getFile().getPriorityData().setHasParisPriorityData(!CollectionUtils.isEmpty(ebdPatent.getParisPriorities()));
            updateAuthorsOnRightsPanelSave(patent,ebdPatent.getAuthorshipData());
        }
    }


    private String redirectToPatentPage(RedirectAttributes redirectAttributes, CPatent patent, String sessionIdentifier, List<String> editedPanels) {
        redirectAttributes.addFlashAttribute("patent", patent);
        redirectAttributes.addFlashAttribute("sessionObjectIdentifier", sessionIdentifier);
        redirectAttributes.addFlashAttribute("editedPanels", editedPanels);
        return RedirectUtils.redirectToObjectViewPage(patent.getFile().getFileId(), patent.isReception());
    }


    //reads the claims data from the submitted form, updates the claim section and returns its fragment
    private void updateClaimsPanelFromRequestForm(T rights, CPatent patent, HttpServletRequest request, String sessionIdentifier,boolean updateRelationshipData) {
        if (updateRelationshipData){
            updateRelationshipData(rights.getDivisionalData(), patent.getFile(), CFileRelationshipUtils.fileTypeToDivisionalRelationshipType(patent.getFile().getFileId().getFileType()));
        }
        updateExhibitionData(rights, patent.getFile());
        fillPatentPctData(rights.getDataPct(), patent);
        setSessionPrioritiesToObject(rights.isHasPriority(), patent.getFile(), request, sessionIdentifier);
        updatePatentTransformationData(rights, patent);
        processAdditionalTransformation(patent, rights);
    }


    private String prepareAjaxPage(CPatent patent, Model model) {
        if (patent.getFile() != null) {
            CFileRelationshipUtils.supplyViewWithDivisionalData(searchService, model, patent.getFile());
            initAdditionalData(patent, model);
        }
        model.addAttribute("patent", patent);
        model.addAttribute("countryMap", countryService.getCountryMap());
        return "ipobjects/patentlike/common/rights/rights_panel :: rights";
    }


    protected void updatePatentTransformationData(PatentRightsData rightsData, CPatent cPatent) {
        if (rightsData.getTransformationData() != null && rightsData.getTransformationData().isHasTransformationData() && !"BG".equals(rightsData.getTransformationData().getApplicationType())) {//bg transformaciqta otiva kato relation, zatova se maha ot tuk!
            PatentTransformationData transformationData = rightsData.getTransformationData();
            cPatent.setRelationshipExtended(transformationData.toTransformationRelationship());
        } else {
            if (!Objects.isNull(cPatent.getRelationshipExtended()) &&
                    !Objects.isNull(cPatent.getRelationshipExtended().getRelationshipType()) &&
                    cPatent.getRelationshipExtended().getRelationshipType().equals(RelationshipType.TRANSFORMED_NATIONAL_PATENT_TYPE)){
                cPatent.setRelationshipExtended(null);
            }

        }
    }

    @PostMapping("/open-priority-modal")
    public String openPriorityModal(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier,
                                    @RequestParam(required = false) Integer index) {
        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        openPriorityModal(request, model, sessionIdentifier, index, countryService, patent.getFile());
        return "ipobjects/common/priority/priority_modal :: priority-form ";
    }

    @PostMapping("/delete-priority")
    public String deletePriority(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier,
                                 @RequestParam Integer index) {
        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        deletePriorityBase(request, model, sessionIdentifier, index, patent.getFile());
        return "ipobjects/common/priority/priority_table :: priorities-table";
    }

    @PostMapping("/save-priority")
    public String savePriority(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier,
                               @RequestParam String data, @RequestParam(required = false) Integer index) {
        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        return savePriorityAndReturnPage(request, model, sessionIdentifier, patent.getFile(), data, index, validatorCreator, countryService);
    }


    protected void fillPatentPctData(PatentPctData patentPctData, CPatent cPatent) {
        if (patentPctData.isHasPctData()) {
            if (cPatent.getPctApplicationData() == null) {
                cPatent.setPctApplicationData(new CPctApplicationData());
            }
            cPatent.getPctApplicationData().setPctApplicationId(patentPctData.getPctApplicationId());
            cPatent.getPctApplicationData().setPctApplicationDate(patentPctData.getPctApplicationDate());
            cPatent.getPctApplicationData().setPctPublicationCountryCode(patentPctData.getPctPublicationCountryCode());
            cPatent.getPctApplicationData().setPctPublicationId(patentPctData.getPctPublicationId());
            cPatent.getPctApplicationData().setPctPublicationDate(patentPctData.getPctPublicationDate());
        } else {
            cPatent.setPctApplicationData(null);
        }
    }

    protected void updateAuthorsOnRightsPanelSave(CPatent patent,CAuthorshipData authorshipData){
        patent.setAuthorshipData(authorshipData);
        if (patent.getAuthorshipData() != null && !CollectionUtils.isEmpty(patent.getAuthorshipData().getAuthorList() )) {
            patent.getAuthorshipData().getAuthorList().forEach(a -> PersonUtils.setTemporaryPersonId(a.getPerson(), tempID));
        }
    }

    protected abstract void processAdditionalTransformation(CPatent patent, T rights);


    protected abstract boolean processSpecificObjectChanges(CPatent patent, T rights);

    protected abstract void initAdditionalData(CPatent patent, Model model);


    protected void transferClaimsDataFromMasterPatentToChildPatent(CFileId masterPatentId, CPatent childPatent) {
        if (CoreUtils.isEmptyCFileId(masterPatentId)) {
            return;
        }
        CPatent masterPatent = patentService.findPatent(masterPatentId, false);
        if (masterPatent != null) {//in theory if the CFileId is not empty, the patent should not be null!
            recalculateEntitlementAndExpirationDate(childPatent.getFile(),masterPatent.getFile().getRegistrationData().getEntitlementDate());
            childPatent.setAuthorshipData(masterPatent.getAuthorshipData());
            childPatent.getFile().setPriorityData(masterPatent.getFile().getPriorityData());//transfers parisPriorities and exhibitions
        }
    }
}
