package com.duosoft.ipas.controller.ipobjects.marklike.mark;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CRelationship;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.nomenclature.CountryService;
import bg.duosoft.ipas.core.service.search.IpoSearchService;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.controller.ipobjects.common.RightsBaseController;
import com.duosoft.ipas.util.CFileRelationshipUtils;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.RedirectUtils;
import com.duosoft.ipas.util.json.MarkClaimData;
import com.duosoft.ipas.util.json.MarkTransformationData;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * Created by Raya
 * 08.03.2019
 */
@Controller
@RequestMapping(value={"/mark", "/international_mark"})
public class MarkClaimController extends RightsBaseController {

    @Autowired
    private CountryService countryService;

    @Autowired
    private IpasValidatorCreator validatorCreator;

    @Autowired
    private IpoSearchService searchService;
    @Autowired
    private MarkService markService;

    @PostMapping("/edit-panel-claims")
    public String editClaimsPanel(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes, @RequestParam(required = false) String data, @RequestParam Boolean isCancel, @RequestParam(required = false) List<String> editedPanels, @RequestParam String sessionIdentifier) {
        CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
        if (!isCancel) {
            boolean redirectToMarkPageFlag = false;
            boolean updateRelationshipData = CFileRelationshipUtils.hasDivisionalApplicationPanelAccess(mark.getFile().getFileId().getFileType());
            MarkClaimData claimData = JsonUtil.readJson(data, MarkClaimData.class);
            if (claimData.getDivisionalData() != null && claimData.getDivisionalData().isRelationshipChanged()) {
                transferClaimsFromParentToChild(claimData, mark);
                redirectToMarkPageFlag=true;
                updateRelationshipData=false;
            }
            if(claimData.getTransformationData()!=null && !StringUtils.isEmpty(claimData.getTransformationData().getFilingNumber())
               && !Objects.isNull(claimData.getTransformationData().getFilingDate())
               && (!Objects.equals(claimData.getTransformationData().getFilingNumber(),Objects.isNull(mark.getRelationshipExtended())?null:mark.getRelationshipExtended().getFilingNumber())
               || !Objects.equals(claimData.getTransformationData().getApplicationType(),Objects.isNull(mark.getRelationshipExtended())?null:mark.getRelationshipExtended().getApplicationType())
               || !Objects.equals(claimData.getTransformationData().getFilingDate(),Objects.isNull(mark.getRelationshipExtended())?null:mark.getRelationshipExtended().getFilingDate()))){
                recalculateEntitlementAndExpirationDate(mark.getFile(),claimData.getTransformationData().getFilingDate());
                redirectToMarkPageFlag=true;
            }
            updateClaimsFormFromRequest(claimData, mark, request, sessionIdentifier,updateRelationshipData);
            if (redirectToMarkPageFlag){
                return RedirectUtils.redirectToMarkPage(redirectAttributes, mark, sessionIdentifier, editedPanels);
            }else{
                return prepareClaimPanel(mark, model);
            }
        } else {
            return prepareClaimPanel(mark, model);
        }
    }

    private void transferClaimsFromParentToChild(MarkClaimData rights, CMark mark) {
        CRelationship divisionalRelationship = updateRelationshipData(rights.getDivisionalData(), mark.getFile(), CFileRelationshipUtils.fileTypeToDivisionalRelationshipType(mark.getFile().getFileId().getFileType()));
        CFileId parentId = divisionalRelationship == null ? null : divisionalRelationship.getFileId();
        if (CoreUtils.isEmptyCFileId(parentId)) {
            return;
        }
        CMark master = markService.findMark(parentId, false);
        if (master != null) {//in theory if the CFileId is not empty, the mark should not be null!
            recalculateEntitlementAndExpirationDate(mark.getFile(),master.getFile().getRegistrationData().getEntitlementDate());
            mark.getFile().setPriorityData(master.getFile().getPriorityData());//transfers parisPriorities and exhibitions
        }
    }


    private void updateClaimsFormFromRequest(MarkClaimData claimData, CMark mark, HttpServletRequest request, String sessionIdentifier,boolean updateRelationshipData) {
        if (updateRelationshipData){
            updateRelationshipData(claimData.getDivisionalData(), mark.getFile(), CFileRelationshipUtils.fileTypeToDivisionalRelationshipType(mark.getFile().getFileId().getFileType()));
        }
        updateExhibitionData(claimData, mark.getFile());
        updateMarkTransformationData(claimData, mark);
        setSessionPrioritiesToObject(claimData.isHasPriority(), mark.getFile(), request, sessionIdentifier);
    }

    private String prepareClaimPanel(CMark mark, Model model) {
        CFile markFile =mark.getFile();
        if (markFile != null && CFileRelationshipUtils.hasDivisionalApplicationPanelAccess(markFile.getFileId().getFileType())) {
            CFileRelationshipUtils.supplyViewWithDivisionalData(searchService, model, mark.getFile());
        }
        model.addAttribute("mark", mark);
        model.addAttribute("countryMap", countryService.getCountryMap());
        return "ipobjects/marklike/common/claims/claims_panel :: claims";
    }


    private void updateMarkTransformationData(MarkClaimData claimData, CMark mark) {
        if (claimData.getTransformationData() != null && claimData.getTransformationData().isHasTransformationData()) {
            MarkTransformationData transformationData = claimData.getTransformationData();
            mark.setRelationshipExtended(transformationData.toTransformationRelationship());
        } else {
            mark.setRelationshipExtended(null);
        }
    }

    @PostMapping("/open-priority-modal")
    public String openPriorityModal(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier,
                                    @RequestParam(required = false) Integer index) {
        CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
        openPriorityModal(request, model, sessionIdentifier, index, countryService, mark.getFile());
        return "ipobjects/common/priority/priority_modal :: priority-form ";
    }

    @PostMapping("/delete-priority")
    public String deletePriority(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier,
                                 @RequestParam Integer index) {
        CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
        deletePriorityBase(request, model, sessionIdentifier, index, mark.getFile());
        return "ipobjects/common/priority/priority_table :: priorities-table";
    }

    @PostMapping("/save-priority")
    public String savePriority(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier,
                               @RequestParam String data, @RequestParam(required = false) Integer index) {
        CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
        return savePriorityAndReturnPage(request, model, sessionIdentifier, mark.getFile(), data, index, validatorCreator, countryService);
    }

}
