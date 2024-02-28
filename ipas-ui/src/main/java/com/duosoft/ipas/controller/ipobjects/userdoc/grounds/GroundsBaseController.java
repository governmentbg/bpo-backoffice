package com.duosoft.ipas.controller.ipobjects.userdoc.grounds;

import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPanel;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.model.userdoc.grounds.*;
import bg.duosoft.ipas.core.model.util.CUserdocRootGroundsExt;
import bg.duosoft.ipas.core.service.nomenclature.LegalGroundTypesService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.userdoc.grounds.RootGroundsValidator;
import bg.duosoft.ipas.util.json.JsonUtil;
import bg.duosoft.ipas.util.userdoc.UserdocGroundsUtils;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocInitPanelSessionObjectsUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import com.duosoft.ipas.webmodel.RelatedUserdocPanelGroundTypes;
import com.duosoft.ipas.webmodel.structure.UserdocRootGroundWebModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public abstract class GroundsBaseController {

    private final String panelLegalGroundsModalUrl = "ipobjects/userdoc/grounds/common_elements/userdoc_ground_edit_modal :: legal-ground-types-modal";

    @Autowired
    private IpasValidatorCreator validatorCreator;

    @Autowired
    private LegalGroundTypesService legalGroundTypesService;

    @Autowired
    private YAMLConfig yamlConfig;

    protected abstract List<CLegalGroundTypes> fillLegalGroundTypes(UserdocRootGroundWebModel groundWebModel);

    protected abstract void additionalActionsOnInitializeLegalGroundsModal(CUserdocRootGrounds cUserdocRootGround, UserdocRootGroundWebModel groundWebModel, HttpServletRequest request, String sessionIdentifier,Model model);

    protected abstract  void additionalActionsOnEditRootGrounds(CUserdocRootGroundsExt cUserdocRootGround,UserdocRootGroundWebModel rootGroundModalData,HttpServletRequest request,String sessionIdentifier);

    protected abstract  void additionalActionsOnValidateUserdocRootGround(CUserdoc userdocForValidation,UserdocRootGroundWebModel rootGroundModalData,HttpServletRequest request,String sessionIdentifier);

    protected abstract String getViewPage();

    protected abstract void setModelAttributes(Model model, String sessionIdentifier);

    protected abstract void setModelAttributesOnOpenEditModal(Model model, UserdocRootGroundWebModel groundWebModel,HttpServletRequest request,String sessionIdentifier);

    protected abstract String getPanelPointer();

    @PostMapping("/init-panel-session-objects")
    @ResponseStatus(value = HttpStatus.OK)
    public void initPanelSessionObjects(HttpServletRequest request, @RequestParam String panel, @RequestParam String sessionIdentifier) {
        CUserdoc userdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        UserdocInitPanelSessionObjectsUtils.initGroundTypesPanelSessionObjects(request,sessionIdentifier,userdoc);
    }

    @PostMapping("/save-userdoc-root-grounds")
    public String saveRootGrounds(HttpServletRequest request, Model model, @RequestParam Boolean isCancel,
                                 @RequestParam String sessionIdentifier) {
        CUserdoc userdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        if (!isCancel) {
            List<CUserdocRootGrounds> rootGrounds = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUNDS, sessionIdentifier, request);
            userdoc.setUserdocRootGrounds(rootGrounds);
        }
        HttpSessionUtils.removeSessionAttributes(request,sessionIdentifier,UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUNDS);
        HttpSessionUtils.removeSessionAttributes(request,sessionIdentifier,UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUND_UPLOADED_IMAGE);
        model.addAttribute("userdocRootGrounds",userdoc.getUserdocRootGrounds());
        model.addAttribute("panelPointer",getPanelPointer());
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        model.addAttribute("groundsVersion",yamlConfig.getUserDocsLegalGroundTypesVersion());
        setModelAttributes(model,sessionIdentifier);
        return getViewPage();
    }

    @PostMapping("/delete-root-ground")
    public String deleteRootGround(HttpServletRequest request, Model model,
                                   @RequestParam String sessionIdentifier,
                                   @RequestParam Integer rootGroundId) {

        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        List<CUserdocRootGrounds> rootGrounds = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUNDS, sessionIdentifier, request);
        rootGrounds.removeIf(ground->ground.getRootGroundId().equals(rootGroundId));
        model.addAttribute("userdocRootGrounds",rootGrounds);
        model.addAttribute("panelPointer",getPanelPointer());
        setModelAttributes(model,sessionIdentifier);
        return getViewPage();
    }

    @PostMapping("/edit-userdoc-root-grounds")
    public String editRootGrounds(HttpServletRequest request, Model model,
                                 @RequestParam String sessionIdentifier, @RequestParam(required = false) String data) {

        UserdocRootGroundWebModel rootGroundModalData = JsonUtil.readJson(data, UserdocRootGroundWebModel.class);
        List<ValidationError> errors = validateUserdocRootGround(rootGroundModalData,request,sessionIdentifier);
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);

        if (!CollectionUtils.isEmpty(errors)){
            model.addAttribute("validationErrors",errors);
             initializeLegalGroundsModal(sessionIdentifier,rootGroundModalData.getRootGroundId(),rootGroundModalData.getPanel(),model,request,rootGroundModalData);
             return panelLegalGroundsModalUrl;
        }

        CUserdocRootGroundsExt cUserdocRootGround = new CUserdocRootGroundsExt();
        List<CUserdocRootGrounds> rootGrounds = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUNDS, sessionIdentifier, request);
        if (Objects.nonNull(rootGroundModalData.getRootGroundId())){
            CUserdocRootGrounds existedRootGround = rootGrounds.stream().filter(r -> r.getRootGroundId().equals(rootGroundModalData.getRootGroundId())).findFirst().orElse(null);
            rootGrounds.removeIf(r->r.getRootGroundId().equals(existedRootGround.getRootGroundId()));
            cUserdocRootGround.initGroundsExt(existedRootGround);
        }else{
            cUserdocRootGround = initNewRootGroundId(request,sessionIdentifier);
        }
        rootGrounds.add(cUserdocRootGround);
        rootGrounds.sort(Comparator.comparing(CUserdocRootGrounds::getRootGroundId));
        cUserdocRootGround.setMotives(rootGroundModalData.getMotives());
        cUserdocRootGround.setUserdocSubGrounds(fillSubGrounds(cUserdocRootGround.getRootGroundId(),rootGroundModalData.getSelectedGroundTypeIds()));
        additionalActionsOnEditRootGrounds(cUserdocRootGround,rootGroundModalData,request,sessionIdentifier);
        model.addAttribute("userdocRootGrounds",rootGrounds);
        model.addAttribute("panelPointer",getPanelPointer());
        setModelAttributes(model,sessionIdentifier);
        return getViewPage();
    }


    @PostMapping("/open-panel-legal-grounds-modal")
    public String openPanelLegalGroundsModal(HttpServletRequest request, Model model,
                                 @RequestParam String sessionIdentifier,
                                 @RequestParam String panel,
                                 @RequestParam(required = false) Integer rootGroundId) {

      initializeLegalGroundsModal(sessionIdentifier,rootGroundId,panel,model,request,null);
      return panelLegalGroundsModalUrl;
    }


    protected void initializeLegalGroundsModal(String sessionIdentifier,Integer rootGroundId,String panel,Model model,HttpServletRequest request,UserdocRootGroundWebModel rootGroundModalData){
        List<CUserdocRootGrounds> rootGrounds = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUNDS, sessionIdentifier, request);
        List<CLegalGroundTypes> allLegalGroundTypesForSpecificPanel;
        CUserdocRootGrounds cUserdocRootGround = null;

        if(Objects.nonNull(rootGroundModalData)){
            cUserdocRootGround= new CUserdocRootGrounds();
            List<CUserdocSubGrounds> userdocSubGrounds = new ArrayList<>();
            if (!CollectionUtils.isEmpty(rootGroundModalData.getSelectedGroundTypeIds())){
                rootGroundModalData.getSelectedGroundTypeIds().forEach(id->{
                    CUserdocSubGrounds cUserdocSubGround = new CUserdocSubGrounds();
                    cUserdocSubGround.setLegalGroundTypeId(Integer.valueOf(id));
                    userdocSubGrounds.add(cUserdocSubGround);
                });
            }
            cUserdocRootGround.setUserdocSubGrounds(userdocSubGrounds);
        }
        else if(Objects.nonNull(rootGroundId)){
            rootGroundModalData=new UserdocRootGroundWebModel();
            cUserdocRootGround = rootGrounds.stream().filter(r->r.getRootGroundId().equals(rootGroundId)).findFirst().orElse(null);
            rootGroundModalData.setMotives(cUserdocRootGround.getMotives());
            additionalActionsOnInitializeLegalGroundsModal(cUserdocRootGround,rootGroundModalData,request,sessionIdentifier,model);
        }else{
            rootGroundModalData=new UserdocRootGroundWebModel();
        }
        rootGroundModalData.setRootGroundId(rootGroundId);
        rootGroundModalData.setPanel(panel);
        allLegalGroundTypesForSpecificPanel = fillLegalGroundTypes(rootGroundModalData);
        rootGroundModalData.setLegalGroundsTypes(fillRelatedUserdocPanelGroundsTypes(cUserdocRootGround,allLegalGroundTypesForSpecificPanel));
        model.addAttribute("rootGround", rootGroundModalData);
        setModelAttributesOnOpenEditModal(model,rootGroundModalData,request,sessionIdentifier);
    }

    private List<ValidationError> validateUserdocRootGround(UserdocRootGroundWebModel rootGroundModalData,HttpServletRequest request,String sessionIdentifier){
        CUserdoc userdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        CUserdoc userdocForValidation = new CUserdoc();
        userdocForValidation.setUserdocParentData(userdoc.getUserdocParentData());
        userdocForValidation.setUserdocRootGrounds(new ArrayList<>());
        CUserdocRootGrounds cUserdocRootGround = new CUserdocRootGrounds();
        userdocForValidation.getUserdocRootGrounds().add(cUserdocRootGround);

        cUserdocRootGround.setUserdocSubGrounds(new ArrayList<>());
        cUserdocRootGround.setMotives(rootGroundModalData.getMotives());
        if(!CollectionUtils.isEmpty(rootGroundModalData.getSelectedGroundTypeIds())){
            for (String selectedGround:rootGroundModalData.getSelectedGroundTypeIds()) {
                CUserdocSubGrounds  subGround=  new CUserdocSubGrounds();
                subGround.setLegalGroundTypeId(Integer.valueOf(selectedGround));
                cUserdocRootGround.getUserdocSubGrounds().add(subGround);
            }
        }
        additionalActionsOnValidateUserdocRootGround(userdocForValidation,rootGroundModalData,request,sessionIdentifier);
        CUserdocType cUserdocType = new CUserdocType();
        cUserdocType.setPanels(new ArrayList<>());
        CUserdocPanel cUserdocPanel = new CUserdocPanel();
        cUserdocPanel.setPanel(rootGroundModalData.getPanel());
        cUserdocType.getPanels().add(cUserdocPanel);
        userdocForValidation.setUserdocType(cUserdocType);
        IpasValidator<CUserdoc> validator = validatorCreator.create(false, RootGroundsValidator.class);
        return validator.validate(userdocForValidation);
    }

    private List<CUserdocSubGrounds> fillSubGrounds(Integer rootGroundId, List<String> selectedGroundTypeIds){
        List<CUserdocSubGrounds> cUserdocSubGrounds=new ArrayList<>();
        selectedGroundTypeIds.stream().forEach(subId->{
            CUserdocSubGrounds cUserdocSubGround = new CUserdocSubGrounds();
            CLegalGroundTypes cLegalGroundType = legalGroundTypesService.findById(Integer.valueOf(subId));
            cUserdocSubGround.setLegalGroundTypeId(cLegalGroundType.getId());
            cUserdocSubGround.setLegalGroundTypeTitle(cLegalGroundType.getTitle());
            cUserdocSubGround.setLegalGroundTypeDescription(cLegalGroundType.getDescription());
            cUserdocSubGround.setRootGroundId(rootGroundId);
            cUserdocSubGround.setVersion(yamlConfig.getUserDocsLegalGroundTypesVersion());
            cUserdocSubGrounds.add(cUserdocSubGround);
        });

        return cUserdocSubGrounds;
    }

    private List<RelatedUserdocPanelGroundTypes> fillRelatedUserdocPanelGroundsTypes(CUserdocRootGrounds cUserdocRootGround,
                                                                                     List<CLegalGroundTypes> allLegalGroundTypesForSpecificPanel){
        List<RelatedUserdocPanelGroundTypes> relatedUserdocPanelGroundTypes = new ArrayList<>();

        for (CLegalGroundTypes cLegalGroundType:allLegalGroundTypesForSpecificPanel) {
            boolean isAvailableType = false;
            if (!Objects.isNull(cUserdocRootGround) && !CollectionUtils.isEmpty(cUserdocRootGround.getUserdocSubGrounds())){
                for (CUserdocSubGrounds cUserdocSubGround:cUserdocRootGround.getUserdocSubGrounds()) {
                    if (cUserdocSubGround.getLegalGroundTypeId().equals(cLegalGroundType.getId())){
                        isAvailableType = true;
                        break;
                    }
                }
            }
            relatedUserdocPanelGroundTypes.add(new RelatedUserdocPanelGroundTypes(isAvailableType,cLegalGroundType.getId(),cLegalGroundType.getTitle(),cLegalGroundType.getDescription()));
        }
        return relatedUserdocPanelGroundTypes;
    }

    private CUserdocRootGroundsExt initNewRootGroundId(HttpServletRequest request,String sessionIdentifier){
        CUserdocRootGroundsExt cUserdocRootGround = new CUserdocRootGroundsExt();
        List<CUserdocRootGrounds> rootGroundsForMaxGround = new ArrayList<>();
        List<CUserdocRootGrounds> rootGrounds = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUNDS, sessionIdentifier, request);
        CUserdoc userdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        rootGroundsForMaxGround.addAll(rootGrounds);
        rootGroundsForMaxGround.addAll(userdoc.getUserdocRootGrounds());
        cUserdocRootGround.setRootGroundId(UserdocGroundsUtils.getNewGroundId(rootGroundsForMaxGround));
        return cUserdocRootGround;
    }
}
