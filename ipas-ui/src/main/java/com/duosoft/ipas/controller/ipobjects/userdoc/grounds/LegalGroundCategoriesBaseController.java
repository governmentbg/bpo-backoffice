package com.duosoft.ipas.controller.ipobjects.userdoc.grounds;

import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.grounds.CLegalGroundCategories;
import bg.duosoft.ipas.core.model.userdoc.grounds.CLegalGroundTypes;
import bg.duosoft.ipas.core.model.userdoc.grounds.CMarkGroundData;
import bg.duosoft.ipas.core.model.userdoc.grounds.CUserdocRootGrounds;
import bg.duosoft.ipas.core.model.util.CUserdocRootGroundsExt;
import bg.duosoft.ipas.core.service.nomenclature.LegalGroundCategoriesService;
import bg.duosoft.ipas.core.service.nomenclature.LegalGroundTypesService;
import bg.duosoft.ipas.enums.GroundCategoriesTypes;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import com.duosoft.ipas.webmodel.structure.MarkGroundDataWebModel;
import com.duosoft.ipas.webmodel.structure.UserdocRootGroundWebModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class LegalGroundCategoriesBaseController extends EarlierTypeBaseController {

    @Autowired
    private LegalGroundTypesService legalGroundTypesService;

    @Autowired
    private LegalGroundCategoriesService legalGroundCategoriesService;

    @Autowired
    private YAMLConfig yamlConfig;

    @PostMapping("/change-category-type")
    public String changeCategoryType(HttpServletRequest request, Model model,
                                    @RequestParam String sessionIdentifier, @RequestParam(required = false) String data) {

        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        HttpSessionUtils.setSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUND_UPLOADED_IMAGE, sessionIdentifier, null, request);
        UserdocRootGroundWebModel rootGroundModalData = JsonUtil.readJson(data, UserdocRootGroundWebModel.class);
        initializeLegalGroundsModal(sessionIdentifier,rootGroundModalData.getRootGroundId(),rootGroundModalData.getPanel(),model,request,rootGroundModalData);
        return panelLegalGroundsModalContent;
    }

    @Override
    protected void additionalActionsOnInitializeLegalGroundsModal(CUserdocRootGrounds cUserdocRootGround, UserdocRootGroundWebModel groundWebModel, HttpServletRequest request, String sessionIdentifier,Model model) {
        if (Objects.nonNull(cUserdocRootGround) && Objects.nonNull(cUserdocRootGround.getMarkGroundData().getLegalGroundCategory())
            && Objects.nonNull(cUserdocRootGround.getMarkGroundData().getLegalGroundCategory().getCode())){
            if (cUserdocRootGround.getMarkGroundData().getLegalGroundCategory().getCode().equalsIgnoreCase(GroundCategoriesTypes.RELATIVE_GROUNDS.code())){
                super.additionalActionsOnInitializeLegalGroundsModal(cUserdocRootGround, groundWebModel, request, sessionIdentifier,model);

            }
            if(Objects.isNull(groundWebModel.getMarkGroundData())){
                groundWebModel.setMarkGroundData(new MarkGroundDataWebModel());
            }
            groundWebModel.getMarkGroundData().setLegalGroundCategory(cUserdocRootGround.getMarkGroundData().getLegalGroundCategory().getCode());
        }
    }

    @Override
    protected List<CLegalGroundTypes> fillLegalGroundTypes(UserdocRootGroundWebModel groundWebModel) {
        if (Objects.nonNull(groundWebModel.getMarkGroundData()) && Objects.nonNull(groundWebModel.getMarkGroundData().getLegalGroundCategory()) && groundWebModel.getMarkGroundData().getLegalGroundCategory().equalsIgnoreCase(GroundCategoriesTypes.RELATIVE_GROUNDS.code())){
            return super.fillLegalGroundTypes(groundWebModel);
        }
        if (Objects.nonNull(groundWebModel.getMarkGroundData()) && Objects.nonNull(groundWebModel.getMarkGroundData().getLegalGroundCategory()) && groundWebModel.getMarkGroundData().getLegalGroundCategory().equalsIgnoreCase(GroundCategoriesTypes.ABSOLUTE_GROUNDS.code())){
            return legalGroundTypesService.findAllLegalGroundTypesForSpecificPanel(yamlConfig.getUserDocsLegalGroundTypesVersion(), groundWebModel.getPanel());
        }
        return new ArrayList<>();
    }

    @Override
    public void setModelAttributesOnOpenEditModal(Model model, UserdocRootGroundWebModel groundWebModel, HttpServletRequest request, String sessionIdentifier) {

        if (Objects.nonNull(groundWebModel.getMarkGroundData()) && Objects.nonNull(groundWebModel.getMarkGroundData().getLegalGroundCategory()) && groundWebModel.getMarkGroundData().getLegalGroundCategory().equalsIgnoreCase(GroundCategoriesTypes.RELATIVE_GROUNDS.code())){
            super.setModelAttributesOnOpenEditModal(model, groundWebModel, request, sessionIdentifier);
        }
        else if (Objects.nonNull(groundWebModel.getMarkGroundData()) && Objects.nonNull(groundWebModel.getMarkGroundData().getLegalGroundCategory()) && groundWebModel.getMarkGroundData().getLegalGroundCategory().equalsIgnoreCase(GroundCategoriesTypes.ABSOLUTE_GROUNDS.code())){
            model.addAttribute("hasEarlierRightTypes",false);
            model.addAttribute("hasLegalGroundTypes",true);
        }else{
            model.addAttribute("hasEarlierRightTypes",false);
            model.addAttribute("hasLegalGroundTypes",false);
        }
        model.addAttribute("hasLegalGroundCategories",true);
        model.addAttribute("earlierLegalGroundCategories",legalGroundCategoriesService.findAll());
    }

    @Override
    protected void additionalActionsOnEditRootGrounds(CUserdocRootGroundsExt cUserdocRootGround , UserdocRootGroundWebModel rootGroundModalData, HttpServletRequest request, String sessionIdentifier) {
        if (rootGroundModalData.getMarkGroundData().getLegalGroundCategory().equalsIgnoreCase(GroundCategoriesTypes.RELATIVE_GROUNDS.code())){
            super.additionalActionsOnEditRootGrounds(cUserdocRootGround, rootGroundModalData, request, sessionIdentifier);
        }
        if (Objects.isNull(cUserdocRootGround.getMarkGroundData())){
            cUserdocRootGround.setMarkGroundData(new CMarkGroundData());
        }
        cUserdocRootGround.getMarkGroundData().setLegalGroundCategory(legalGroundCategoriesService.findById(rootGroundModalData.getMarkGroundData().getLegalGroundCategory()));
}

    @Override
    protected void additionalActionsOnValidateUserdocRootGround(CUserdoc userdocForValidation, UserdocRootGroundWebModel rootGroundModalData, HttpServletRequest request, String sessionIdentifier) {
        super.additionalActionsOnValidateUserdocRootGround(userdocForValidation, rootGroundModalData, request, sessionIdentifier);
        CUserdocRootGrounds cUserdocRootGround = userdocForValidation.getUserdocRootGrounds().get(0);
        cUserdocRootGround.getMarkGroundData().setLegalGroundCategory(new CLegalGroundCategories());
        cUserdocRootGround.getMarkGroundData().getLegalGroundCategory().setCode(rootGroundModalData.getMarkGroundData().getLegalGroundCategory());
    }
}
