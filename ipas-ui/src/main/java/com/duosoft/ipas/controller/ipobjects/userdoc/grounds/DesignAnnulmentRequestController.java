package com.duosoft.ipas.controller.ipobjects.userdoc.grounds;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.patent.CDrawing;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.userdoc.grounds.CLegalGroundTypes;
import bg.duosoft.ipas.core.model.userdoc.grounds.CUserdocRootGrounds;
import bg.duosoft.ipas.core.model.util.AutocompleteIpoSearchResult;
import bg.duosoft.ipas.core.model.util.CUserdocRootGroundsExt;
import bg.duosoft.ipas.core.service.nomenclature.*;
import bg.duosoft.ipas.core.service.patent.DesignService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.search.AutocompleteIpoSearchService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.LegalGroundTypes;
import bg.duosoft.ipas.core.model.search.AutocompleteIpoSearchParam;
import bg.duosoft.ipas.core.model.search.SearchPage;
import bg.duosoft.ipas.util.general.BasicUtils;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.util.UserdocGroundNationalDesign;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import com.duosoft.ipas.webmodel.RelatedUserdocPanelGroundTypes;
import com.duosoft.ipas.webmodel.structure.GroundSingleDesignWebModel;
import com.duosoft.ipas.webmodel.structure.UserdocRootGroundWebModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/design-annulment-request")
public class DesignAnnulmentRequestController extends EarlierTypeBaseController{

    @Autowired
    private PatentService patentService;

    @Autowired
    private DesignService designService;

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private LegalGroundTypesService legalGroundTypesService;

    @Autowired
    private EarlierRightTypesService earlierRightTypesService;

    @Autowired
    private ApplicantAuthorityService applicantAuthorityService;

    @Autowired
    private MarkGroundTypeService markGroundTypeService;

    @Autowired
    private AutocompleteIpoSearchService autocompleteIpoSearchService;

    @Autowired
    private SignTypeService signTypeService;

    private static final String DB_PANEL_NAME = "DesignAnnulmentRequest";

    protected final String panelImportedSingleDesigns = "ipobjects/userdoc/grounds/common_elements/subsections/single_designs_subsection :: single-designs-subsection";

    @Override
    protected List<CLegalGroundTypes> fillLegalGroundTypes(UserdocRootGroundWebModel groundWebModel) {
        return legalGroundTypesService.findAllLegalGroundsForSpecificPanelAndEarlierRight(yamlConfig.getUserDocsLegalGroundTypesVersion(),DB_PANEL_NAME,groundWebModel.getEarlierRightType());
    }

    @GetMapping(value = "/autocomplete-ground-design", produces = "application/json")
    @ResponseBody
    public List<UserdocGroundNationalDesign> autocompleteGroundNationalDesign(@RequestParam String registrationNbr) {
        AutocompleteIpoSearchParam searchParam = new AutocompleteIpoSearchParam(SearchPage.create(0, 10000));
        searchParam.addFileType(FileType.DESIGN.code()).registrationNbr(registrationNbr);
        Page<AutocompleteIpoSearchResult> result=autocompleteIpoSearchService.search(searchParam);
        List<UserdocGroundNationalDesign> autocompleteList = result == null ? new ArrayList<>() : result.get().map(UserdocGroundNationalDesign::new).collect(Collectors.toList());
        return autocompleteList;
    }



    @RequestMapping(value = "/content")
    public ResponseEntity<byte[]> getImage(HttpServletRequest request,
                                           @RequestParam String sessionIdentifier,
                                           @RequestParam Long drawingn, @RequestParam(required = false) String filingNumber) {
        final HttpHeaders headers = new HttpHeaders();
        CFileId singleDesignId=BasicUtils.createCFileId(filingNumber);
        CDrawing databaseDrawing = patentService.selectDrawing(singleDesignId, Math.toIntExact(drawingn));
        if (Objects.isNull(databaseDrawing))
            return null;
        return new ResponseEntity<>(databaseDrawing.getDrawingData(), headers, HttpStatus.OK);
    }


    @PostMapping("/add-single-designs")
    public String addSingleDesigns(HttpServletRequest request, Model model,
                                       @RequestParam String sessionIdentifier,
                                       @RequestParam String nationalDesignFilingNumber,
                                       @RequestParam(required = false) String data) {

        UserdocRootGroundWebModel rootGroundModalData = JsonUtil.readJson(data, UserdocRootGroundWebModel.class);
        List<GroundSingleDesignWebModel>singleDesignsWebModel =  rootGroundModalData.getSingleDesigns();
        if (CollectionUtils.isEmpty(singleDesignsWebModel)){
            singleDesignsWebModel=new ArrayList<>();
        }
        CFileId nationalDesignPK= BasicUtils.createCFileId(nationalDesignFilingNumber);
        CPatent nationalDesign = patentService.findPatent(nationalDesignPK,false);
        List<CPatent> singleDesigns = designService.getAllSingleDesignsForIndustrialDesign(nationalDesign,false);

        if (!CollectionUtils.isEmpty(singleDesigns)) {
            for (CPatent singleDesign : singleDesigns) {
               GroundSingleDesignWebModel groundSingleDesignWebModel=new GroundSingleDesignWebModel(singleDesign.getFile().getFileId().createFilingNumber(),singleDesign.getTechnicalData().getTitle());
                if (!singleDesignsWebModel.contains(groundSingleDesignWebModel)){
                    singleDesignsWebModel.add(groundSingleDesignWebModel);
                }
            }
        }
        model.addAttribute("singleDesignsWebModel",singleDesignsWebModel);
        return panelImportedSingleDesigns;
    }



    @PostMapping("/check-legal-ground-type")
    public String checkLegalGroundType(HttpServletRequest request, Model model,
                                    @RequestParam String sessionIdentifier, @RequestParam(required = false) String data) {

        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        HttpSessionUtils.setSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUND_UPLOADED_IMAGE, sessionIdentifier, null, request);
        UserdocRootGroundWebModel rootGroundModalData = JsonUtil.readJson(data, UserdocRootGroundWebModel.class);
        showMarkGroundDataIfExist(model,rootGroundModalData);
        showDesignImportSubsection(model,rootGroundModalData);
        initializeLegalGroundsModal(sessionIdentifier,rootGroundModalData.getRootGroundId(),rootGroundModalData.getPanel(),model,request,rootGroundModalData);
        return panelLegalGroundsModalContent;
    }

    @Override
    protected void additionalActionsOnInitializeLegalGroundsModal(CUserdocRootGrounds cUserdocRootGround, UserdocRootGroundWebModel groundWebModel, HttpServletRequest request, String sessionIdentifier, Model model) {
        super.additionalActionsOnInitializeLegalGroundsModal(cUserdocRootGround,groundWebModel,request,sessionIdentifier,model);
        groundWebModel.initSingleDesignsWebData(groundWebModel,cUserdocRootGround);
    }

    @Override
    protected void additionalActionsOnEditRootGrounds(CUserdocRootGroundsExt cUserdocRootGround, UserdocRootGroundWebModel rootGroundModalData, HttpServletRequest request, String sessionIdentifier) {
        super.additionalActionsOnEditRootGrounds(cUserdocRootGround, rootGroundModalData, request, sessionIdentifier);
        String legalGroundTypeId = rootGroundModalData.getSelectedGroundTypeIds().stream().filter(r -> Integer.valueOf(r).equals(LegalGroundTypes.EARLIER_MARK.code()))
                .findFirst()
                .orElse(null);
        if (Objects.isNull(legalGroundTypeId)){
            cUserdocRootGround.setMarkGroundData(null);
        }
        rootGroundModalData.initSingleDesignsCoreData(cUserdocRootGround,rootGroundModalData,designService);
    }


    @Override
    public void setModelAttributesOnOpenEditModal(Model model, UserdocRootGroundWebModel groundWebModel,HttpServletRequest request,String sessionIdentifier) {
        model.addAttribute("hasEarlierRightTypes",true);
        model.addAttribute("hasLegalGroundTypes",true);
        model.addAttribute("hasLegalGroundCategories",false);
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        model.addAttribute("earlierRightTypes",earlierRightTypesService.findAllEarlierRightTypesForSpecificPanel(DB_PANEL_NAME,yamlConfig.getUserDocsLegalGroundTypesVersion()));
        showMarkGroundDataIfExist(model,groundWebModel);
        showDesignImportSubsection(model,groundWebModel);

    }

    @Override
    public String getViewPage() {
        return "ipobjects/userdoc/grounds/design_annulment_request_panel :: design-annulment";
    }

    @Override
    public void setModelAttributes(Model model, String sessionIdentifier) {
        model.addAttribute("hasMultipleRootGrouds",true);
    }

    @Override
    public String getPanelPointer() {
        return "design-annulment-request";
    }


    private void showDesignImportSubsection(Model model,UserdocRootGroundWebModel rootGroundModalData){
        boolean showDesignImportSubsection = false;
        if (!CollectionUtils.isEmpty(rootGroundModalData.getLegalGroundsTypes())){
            RelatedUserdocPanelGroundTypes legalGroundType= rootGroundModalData.getLegalGroundsTypes().stream().filter(r ->r.isAvailableType() && (r.getId().equals(LegalGroundTypes.NOT_NEW_DESIGN.code()) || r.getId().equals(LegalGroundTypes.NOT_ORIGINAL_DESIGN_13.code())
                    || r.getId().equals(LegalGroundTypes.NOT_ORIGINAL_DESIGN_13A.code()) || r.getId().equals(LegalGroundTypes.PUBLIC_BEFORE_FILING_DATE.code())
                    || r.getId().equals(LegalGroundTypes.DECLARED_BEFORE_REGISTRATION.code()))).findFirst().orElse(null);
            if (Objects.nonNull(legalGroundType)){
                showDesignImportSubsection=true;
            }
        }

        model.addAttribute("hasNationalDesignImport",showDesignImportSubsection);

    }

    private void showMarkGroundDataIfExist(Model model,UserdocRootGroundWebModel rootGroundModalData){
        boolean activateMarkGroundData = false;
        if (!CollectionUtils.isEmpty(rootGroundModalData.getLegalGroundsTypes())){
            RelatedUserdocPanelGroundTypes legalGroundType= rootGroundModalData.getLegalGroundsTypes().stream().filter(r->r.isAvailableType() && r.getId().equals(LegalGroundTypes.EARLIER_MARK.code())).findFirst().orElse(null);
            if (Objects.nonNull(legalGroundType)){
                activateMarkGroundData=true;
            }
        }
        if (activateMarkGroundData){
            model.addAttribute("activeEarlierMarkLegalGround",true);
            model.addAttribute("markGroundTypes",markGroundTypeService.findAll());
            model.addAttribute("signTypesMap", signTypeService.getSignTypesMap());
            model.addAttribute("applicantAuthorities",applicantAuthorityService.findAllApplicantAuthoritiesForSpecificRight(rootGroundModalData.getEarlierRightType()));

        }
    }
}


