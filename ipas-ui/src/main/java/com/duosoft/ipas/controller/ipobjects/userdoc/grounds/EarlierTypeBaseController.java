package com.duosoft.ipas.controller.ipobjects.userdoc.grounds;

import bg.duosoft.ipas.core.model.CApplicationSubType;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.grounds.*;
import bg.duosoft.ipas.core.model.util.AutocompleteIpoSearchResult;
import bg.duosoft.ipas.core.model.util.CUserdocRootGroundsExt;
import bg.duosoft.ipas.core.service.nomenclature.*;
import bg.duosoft.ipas.core.service.search.AutocompleteIpoSearchService;
import bg.duosoft.ipas.core.service.userdoc.UserdocRootGroundService;
import bg.duosoft.ipas.enums.ApplTyp;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.MarkSignType;
import bg.duosoft.ipas.util.attachment.AttachmentUtils;
import bg.duosoft.ipas.core.model.search.AutocompleteIpoSearchParam;
import bg.duosoft.ipas.core.model.search.SearchPage;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.util.UserdocGroundNationalMark;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import com.duosoft.ipas.webmodel.structure.MarkGroundDataWebModel;
import com.duosoft.ipas.webmodel.structure.UserdocRootGroundWebModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class EarlierTypeBaseController extends GroundsBaseController {
    protected final String panelLegalGroundsModalContent = "ipobjects/userdoc/grounds/common_elements/userdoc_ground_edit_modal :: legal-ground-types-modal-content";
    private final String groundImageSubsection = "ipobjects/userdoc/grounds/common_elements/subsections/mark_earlier_right_type_subsection :: not-national-mark-earlier-right-type-image-subsection";

    @Autowired
    private UserdocRootGroundService userdocRootGroundService;

    @Autowired
    private AutocompleteIpoSearchService autocompleteIpoSearchService;

    @Autowired
    private ApplicantAuthorityService applicantAuthorityService;

    @Autowired
    private MarkGroundTypeService markGroundTypeService;

    @Autowired
    private EarlierRightTypesService earlierRightTypesService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private LegalGroundTypesService legalGroundTypesService;

    @Autowired
    private SignTypeService signTypeService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @PostMapping("/change-earlier-type")
    public String changeEarlierType(HttpServletRequest request, Model model,
                                    @RequestParam String sessionIdentifier, @RequestParam(required = false) String data) {

        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        HttpSessionUtils.setSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUND_UPLOADED_IMAGE, sessionIdentifier, null, request);
        UserdocRootGroundWebModel rootGroundModalData = JsonUtil.readJson(data, UserdocRootGroundWebModel.class);
        initializeLegalGroundsModal(sessionIdentifier,rootGroundModalData.getRootGroundId(),rootGroundModalData.getPanel(),model,request,rootGroundModalData);
        return panelLegalGroundsModalContent;
    }

    @GetMapping(value = "/autocomplete-ground-national-mark", produces = "application/json")
    @ResponseBody
    public List<UserdocGroundNationalMark> autocompleteGroundNationalMark(@RequestParam String fileNbr) {
        AutocompleteIpoSearchParam searchParam = new AutocompleteIpoSearchParam(SearchPage.create(0, 10000));
        searchParam.addFileType(FileType.MARK.code()).fileNbr(fileNbr);
        Page<AutocompleteIpoSearchResult> result=autocompleteIpoSearchService.search(searchParam);
        List<UserdocGroundNationalMark> autocompleteList = result == null ? new ArrayList<>() : result.get().map(UserdocGroundNationalMark::new).collect(Collectors.toList());
        return autocompleteList;
    }

    @Override
    protected void additionalActionsOnValidateUserdocRootGround(CUserdoc userdocForValidation, UserdocRootGroundWebModel rootGroundModalData,HttpServletRequest request,String sessionIdentifier) {
        CUserdocRootGrounds cUserdocRootGround = userdocForValidation.getUserdocRootGrounds().get(0);
        cUserdocRootGround.setGroundCommonText(rootGroundModalData.getGroundCommonText());
        cUserdocRootGround.setEarlierRightType(new CEarlierRightTypes());
        cUserdocRootGround.getEarlierRightType().setId(rootGroundModalData.getEarlierRightType());
        cUserdocRootGround.setApplicantAuthority(new CApplicantAuthority());
        cUserdocRootGround.getApplicantAuthority().setId(rootGroundModalData.getApplicantAuthority());

        CMarkGroundData markGroundData=new CMarkGroundData();
        MarkGroundDataWebModel markGroundDataWebModel = rootGroundModalData.getMarkGroundData();

        markGroundData.setNiceClassesInd(markGroundDataWebModel.getNiceClassesInd());
        markGroundData.setMarkImportedInd(markGroundDataWebModel.getMarkImportedInd());
        if (Objects.nonNull(markGroundDataWebModel.getGeographicalIndTyp()) && !markGroundDataWebModel.getGeographicalIndTyp().isEmpty()){
            markGroundData.setGeographicalIndTyp(new CApplicationSubType());
        }
        if (Objects.nonNull(markGroundDataWebModel.getMarkSignTyp()) && !markGroundDataWebModel.getMarkSignTyp().isEmpty()){
            markGroundData.setMarkSignTyp(MarkSignType.selectByCode(markGroundDataWebModel.getMarkSignTyp()));
        }
        markGroundData.setNameText(markGroundDataWebModel.getNameText());
        markGroundData.setFilingNumber(markGroundDataWebModel.getFilingNumber());
        markGroundData.setRegistrationNbr(markGroundDataWebModel.getRegistrationNbr());
        markGroundData.setRegistrationDate(markGroundDataWebModel.getRegistrationDate());
        markGroundData.setFilingDate(markGroundDataWebModel.getFilingDate());
        byte[] nameData = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUND_UPLOADED_IMAGE, sessionIdentifier, request);
        markGroundData.setNameData(nameData);
        markGroundData.setMarkGroundType(new CMarkGroundType(markGroundDataWebModel.getMarkGroundType(),null));
        markGroundData.setUserdocGroundsNiceClasses(new ArrayList<>());
        if (Objects.nonNull(markGroundDataWebModel.getNiceClasses())){
            markGroundDataWebModel.getNiceClasses().forEach(r->{
                markGroundData.getUserdocGroundsNiceClasses().add(new CGroundNiceClasses(r.getNiceClassCode(),r.getNiceClassDescription()));
            });
        }
        cUserdocRootGround.setMarkGroundData(markGroundData);
    }
    @Override
    protected void additionalActionsOnEditRootGrounds(CUserdocRootGroundsExt cUserdocRootGround , UserdocRootGroundWebModel rootGroundModalData,HttpServletRequest request,String sessionIdentifier) {
        byte[] nameData = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUND_UPLOADED_IMAGE, sessionIdentifier, request);
        if (Objects.isNull(cUserdocRootGround.getMarkGroundData())){
            cUserdocRootGround.setMarkGroundData(new CMarkGroundData());
        }
        cUserdocRootGround.getMarkGroundData().setNameData(nameData);
        cUserdocRootGround.setNameDataChanged(true);
        UserdocRootGroundWebModel.initCoreOnEarlierTypeActive(cUserdocRootGround,rootGroundModalData,countryService,applicantAuthorityService,earlierRightTypesService,markGroundTypeService,applicationTypeService);
    }

    @Override
    protected List<CLegalGroundTypes> fillLegalGroundTypes(UserdocRootGroundWebModel groundWebModel) {
        return legalGroundTypesService.findAllLegalGroundsForSpecificPanelAndEarlierRight(yamlConfig.getUserDocsLegalGroundTypesVersion(),groundWebModel.getPanel(),groundWebModel.getEarlierRightType());
    }

    @Override
    protected void additionalActionsOnInitializeLegalGroundsModal(CUserdocRootGrounds cUserdocRootGround, UserdocRootGroundWebModel groundWebModel, HttpServletRequest request, String sessionIdentifier,Model model) {
        if(!Objects.isNull(cUserdocRootGround)){
            byte[] nameData = getRootGroundNameData(request, sessionIdentifier, cUserdocRootGround.getRootGroundId());
            HttpSessionUtils.setSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUND_UPLOADED_IMAGE, sessionIdentifier,nameData, request);
            UserdocRootGroundWebModel.initWebModelOnEarlierTypeActive(groundWebModel,cUserdocRootGround);
        }
    }

    private  byte[]  getRootGroundNameData(HttpServletRequest request,String sessionIdentifier,Integer rootGroundId){
        if (!Objects.isNull(rootGroundId)){
            byte[] nameData = null;
            CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
            List<CUserdocRootGrounds> rootGrounds=sessionUserdoc.getUserdocRootGrounds();
            List<CUserdocRootGrounds> sessionRootGrounds = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUNDS, sessionIdentifier, request);
            if (!Objects.isNull(sessionRootGrounds)){
                rootGrounds = sessionRootGrounds;
            }
            CUserdocRootGrounds cUserdocRootGround = rootGrounds.stream().filter(r->r.getRootGroundId().equals(rootGroundId)).findFirst().orElse(null);
            if (Objects.isNull(cUserdocRootGround)){
                throw new RuntimeException("userdoc root ground not found on image downloading");
            }

            if (cUserdocRootGround instanceof CUserdocRootGroundsExt){
                CUserdocRootGroundsExt cUserdocRootGroundsExt = (CUserdocRootGroundsExt)cUserdocRootGround;
                if (cUserdocRootGroundsExt.isNameDataChanged()){
                    nameData = cUserdocRootGroundsExt.getMarkGroundData()!=null?cUserdocRootGroundsExt.getMarkGroundData().getNameData():null;
                }
            }else{
                CUserdocRootGrounds rootGround = userdocRootGroundService.findById(cUserdocRootGround.getRootGroundId(), sessionUserdoc.getDocumentId().getDocOrigin(),
                        sessionUserdoc.getDocumentId().getDocLog(), sessionUserdoc.getDocumentId().getDocSeries(), sessionUserdoc.getDocumentId().getDocNbr());
                nameData = rootGround.getMarkGroundData()!=null?rootGround.getMarkGroundData().getNameData():null;
            }
            return nameData;
        }
        return null;
    }

    @Override
    public void setModelAttributesOnOpenEditModal(Model model, UserdocRootGroundWebModel groundWebModel,HttpServletRequest request,String sessionIdentifier) {
        model.addAttribute("hasEarlierRightTypes",true);
        model.addAttribute("hasLegalGroundTypes",true);
        model.addAttribute("hasLegalGroundCategories",false);
        if (Objects.nonNull(groundWebModel.getEarlierRightType())){
            model.addAttribute("applicantAuthorities",applicantAuthorityService.findAllApplicantAuthoritiesForSpecificRight(groundWebModel.getEarlierRightType()));
            model.addAttribute("countryMap",countryService.getCountryMap());
            model.addAttribute("signTypesMap", signTypeService.getSignTypesMap());
            model.addAttribute("geographicalIndTypes", applicationTypeService.findAllApplicationSubTypesByApplTyp(ApplTyp.GEOGRAPHICAL_INDICATION_TYPE));
            model.addAttribute("markGroundTypes",markGroundTypeService.findAll());
        }
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        model.addAttribute("earlierRightTypes",earlierRightTypesService.findAllEarlierRightTypesForSpecificPanel(groundWebModel.getPanel(),yamlConfig.getUserDocsLegalGroundTypesVersion()));

    }

    @PostMapping("/upload-ground-image")
    public String uploadGroundImage(HttpServletRequest request, Model model, @RequestParam(value = "uploadGroundImage") MultipartFile uploadFile,
                                    @RequestParam String sessionIdentifier, @RequestParam String panel) {

        UserdocRootGroundWebModel groundWebModel = new UserdocRootGroundWebModel();
        try {
            HttpSessionUtils.setSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUND_UPLOADED_IMAGE, sessionIdentifier, uploadFile.getBytes(), request);
        } catch (IOException e) {
            throw new RuntimeException("Upload of userdoc ground file file failed with IOException. Filename: " + uploadFile.getOriginalFilename(), e);
        }

        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        model.addAttribute("rootGround",groundWebModel);
        model.addAttribute("panel",panel);
        return groundImageSubsection;
    }

    @RequestMapping(value = "/download-ground-image")
    public ResponseEntity<byte[]> getImage(HttpServletRequest request,
                                           @RequestParam String sessionIdentifier,
                                           @RequestParam(required = false) Integer rootGroundId, @RequestParam String isEditMode) {
        byte[] nameData = null;
        if (!Boolean.valueOf(isEditMode)){
            nameData = getRootGroundNameData(request,sessionIdentifier,rootGroundId);
        }else{
            nameData = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUND_UPLOADED_IMAGE, sessionIdentifier, request);
        }
        if (Objects.isNull(nameData)){
           return null;
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(AttachmentUtils.getContentType(nameData)));
        return new ResponseEntity<>(nameData, headers, HttpStatus.OK);
    }
}
