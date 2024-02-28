package com.duosoft.ipas.controller.ipobjects.patentlike.spc;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CFilingData;
import bg.duosoft.ipas.core.model.file.CRegistrationData;
import bg.duosoft.ipas.core.model.file.CRelationship;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CPatentDetails;
import bg.duosoft.ipas.core.model.util.AutocompleteIpoSearchResult;
import bg.duosoft.ipas.core.service.nomenclature.ApplicationTypeService;
import bg.duosoft.ipas.core.service.nomenclature.LawService;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.search.AutocompleteIpoSearchService;
import bg.duosoft.ipas.enums.RelationshipType;
import bg.duosoft.ipas.util.default_value.DefaultValueUtils;
import bg.duosoft.ipas.util.default_value.patentlike.spc.SpcDefaultValues;
import bg.duosoft.ipas.core.model.search.AutocompleteIpoSearchParam;
import bg.duosoft.ipas.core.model.search.SearchPage;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.util.ApplicationTypeUtils;
import com.duosoft.ipas.util.RedirectUtils;
import com.duosoft.ipas.util.json.SpcIdentityData;
import com.duosoft.ipas.util.json.SpcMainPatentDataPK;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import de.danielbechler.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/spc/identity")
public class SpcIdentityController {

    private final String spcIdentityView = "ipobjects/patentlike/spc/spc_identity_panel :: spc-identity";

    @Autowired
    private PatentService patentService;

    @Autowired
    private AutocompleteIpoSearchService autocompleteIpoSearchService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private LawService lawService;

    @Autowired
    private DefaultValueUtils defaultValueUtils;

    @PostMapping("/edit-panel-identity")
    public String editIdentityPanel(HttpServletRequest request,Model model, RedirectAttributes redirectAttributes,
                                    @RequestParam(required = false) List<String> editedPanels,
                                    @RequestParam Boolean isCancel, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {
        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        if (!isCancel){
            SpcIdentityData spcIdentityData = JsonUtil.readJson(data, SpcIdentityData.class);
            if (Boolean.parseBoolean(spcIdentityData.getIsPageForReload())){
                fillSpcPanelsOnReload(spcIdentityData,patent,true);
                setRedirectAttributes(redirectAttributes,patent,sessionIdentifier,editedPanels);
                return RedirectUtils.redirectToObjectViewPage(patent.getFile().getFileId(), patent.isReception());
            }else{
                fillSpcIdentityData(spcIdentityData,patent,false);
                setModelAttributes(model,patent,request,sessionIdentifier);
                return spcIdentityView;
            }
        }else{
            setModelAttributes(model,patent,request,sessionIdentifier);
            return spcIdentityView;
        }
    }

    @GetMapping(value = "/autocomplete-main-spc-patent", produces = "application/json")
    @ResponseBody
    public List<SpcMainPatentDataPK> autocompleteRelationshipObject(@RequestParam String fileNbr, @RequestParam String fileType) {

        AutocompleteIpoSearchParam searchParam = new AutocompleteIpoSearchParam(SearchPage.create(0, 10000));
        searchParam.addFileType(fileType).fileNbr(fileNbr);
        Page<AutocompleteIpoSearchResult> result=autocompleteIpoSearchService.search(searchParam);

        List<SpcMainPatentDataPK> autocompleteList = result == null ? new ArrayList<>() : result.get().map(SpcMainPatentDataPK::new).collect(Collectors.toList());
        autocompleteList.stream().forEach(r->r.setStatusTitle(statusService.findById(String.valueOf(r.getProcTyp()),r.getStatusCode()).getStatusName()));

        return autocompleteList;
    }

    private void fillSpcIdentityData(SpcIdentityData spcIdentityData,CPatent patent,boolean isPageForReload){

        CFilingData filingData = patent.getFile().getFilingData();
        filingData.setApplicationType(spcIdentityData.getApplicationType());
        filingData.setApplicationSubtype(spcIdentityData.getApplicationSubtype());
        filingData.setFilingDate(spcIdentityData.getFilingDate());
        filingData.setPublicationTyp(spcIdentityData.getPublicationTyp());
        CRegistrationData registrationData = patent.getFile().getRegistrationData();
        if (Objects.isNull(registrationData))
            registrationData = new CRegistrationData();

        if (!isPageForReload){
            registrationData.setEntitlementDate(spcIdentityData.getEntitlementDate());
            registrationData.setExpirationDate(spcIdentityData.getExpirationDate());
        }
        registrationData.setRegistrationDate(spcIdentityData.getRegistrationDate());
        patent.getFile().setRegistrationData(registrationData);
        if (StringUtils.hasText(spcIdentityData.getEfilingDataLoginName())){
            patent.getPatentEFilingData().setLogUserName(spcIdentityData.getEfilingDataLoginName());
        }
        patent.getFile().setNotes(spcIdentityData.getNotes());
        fillNotInForceDate(patent,spcIdentityData);
    }

    private void fillNotInForceDate(CPatent patent, SpcIdentityData spcIdentityData){
        if (Objects.isNull(patent.getPatentDetails()) && Objects.isNull(spcIdentityData.getNotInForceDate())) {
            return;
        }
        if (Objects.isNull(patent.getPatentDetails())) {
            patent.setPatentDetails(new CPatentDetails());
        }
        patent.getPatentDetails().setNotInForceDate(spcIdentityData.getNotInForceDate());
    }

    private void fillSpcPanelsOnReload(SpcIdentityData spcIdentityData,CPatent patent,boolean isPageForReload){
        fillSpcIdentityData(spcIdentityData,patent,isPageForReload);
        CRegistrationData registrationData = patent.getFile().getRegistrationData();

        if (!Objects.isNull(spcIdentityData.getMainPatent()) && !StringUtils.isEmpty(spcIdentityData.getMainPatent())){
            patent.getFile().setRelationshipList(new ArrayList<>());
            spcIdentityData.splitMainPatent();

            CFileId fileId = new CFileId();
            fileId.setFileNbr(spcIdentityData.getFileNbr());
            fileId.setFileSeq(spcIdentityData.getFileSeq());
            fileId.setFileSeries(spcIdentityData.getFileSeries());
            fileId.setFileType(spcIdentityData.getFileType());

            CRelationship relationship = new CRelationship();
            relationship.setFileId(fileId);
            relationship.setRelationshipRole("2");
            relationship.setRelationshipType(RelationshipType.SPC_MAIN_PATENT_TYPE);
            patent.getFile().getRelationshipList().add(relationship);

            CPatent mainSpcPatent = patentService.findPatent(spcIdentityData.getFileSeq(), spcIdentityData.getFileType(), spcIdentityData.getFileSeries(), spcIdentityData.getFileNbr(), false);
            if (Objects.isNull(patent))
                throw new RuntimeException("Cannot find patent !");

            patent.getTechnicalData().setIpcClassList(mainSpcPatent.getTechnicalData().getIpcClassList());
            if (!Objects.isNull(patent.getTechnicalData().getIpcClassList()) && !CollectionUtils.isEmpty(patent.getTechnicalData().getIpcClassList())){
                patent.getTechnicalData().setHasIpc(true);
            }

            patent.getTechnicalData().setTitle(mainSpcPatent.getTechnicalData().getTitle());
            patent.getTechnicalData().setEnglishTitle(mainSpcPatent.getTechnicalData().getEnglishTitle());

            SpcDefaultValues spcDefaultValues = defaultValueUtils.createSpcDefaultValuesObjectWithGivenExpirationDate(patent,
                    mainSpcPatent.getFile().getRegistrationData()!=null ? mainSpcPatent.getFile().getRegistrationData().getExpirationDate():null);
            registrationData.setEntitlementDate(spcDefaultValues.getEntitlementDate());
            registrationData.setExpirationDate(spcDefaultValues.getExpirationDate());
        }
        else{
            patent.getFile().setRelationshipList(null);
            patent.getTechnicalData().setIpcClassList(new ArrayList<>());
            patent.getTechnicalData().setTitle(null);
            patent.getTechnicalData().setEnglishTitle(null);
            registrationData.setEntitlementDate(null);
            registrationData.setExpirationDate(null);

        }
    }


    private void setRedirectAttributes(RedirectAttributes redirectAttributes,CPatent patent,String sessionIdentifier,List<String> editedPanels){
        redirectAttributes.addFlashAttribute("patent", patent);
        redirectAttributes.addFlashAttribute("sessionObjectIdentifier", sessionIdentifier);
        redirectAttributes.addFlashAttribute("editedPanels", editedPanels);
    }

    private void setModelAttributes(Model model,CPatent patent,HttpServletRequest request,String sessionIdentifier){
        if (!Objects.isNull(patent.getFile().getRelationshipList()) && !Collections.isEmpty(patent.getFile().getRelationshipList())){
            CFileId fileId = patent.getFile().getRelationshipList().get(0).getFileId();
            CStatus status = statusService.getStatus(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
            model.addAttribute("spcMainPatentStatus",status);
        }
        model.addAttribute("applicationTypesMap", ApplicationTypeUtils.getApplicationTypeMap(request, applicationTypeService, sessionIdentifier));
        model.addAttribute("applicationSubTypes", applicationTypeService.findAllApplicationSubTypesByApplTyp(patent.getFile().getFilingData().getApplicationType()));
        model.addAttribute("lawMap", lawService.getLawMap());
        model.addAttribute("file", patent.getFile());
        model.addAttribute("defaultValues", defaultValueUtils.createSpcDefaultValuesObject(patent));
        model.addAttribute("efilingData",patent.getPatentEFilingData());
        model.addAttribute("notInForceDate", Objects.isNull(patent.getPatentDetails()) ? null : patent.getPatentDetails().getNotInForceDate());
    }
}
