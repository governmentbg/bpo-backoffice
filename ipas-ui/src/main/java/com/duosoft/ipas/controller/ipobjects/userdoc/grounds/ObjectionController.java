package com.duosoft.ipas.controller.ipobjects.userdoc.grounds;

import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.grounds.CLegalGroundTypes;
import bg.duosoft.ipas.core.model.userdoc.grounds.CUserdocRootGrounds;
import bg.duosoft.ipas.core.model.util.CUserdocRootGroundsExt;
import bg.duosoft.ipas.core.service.nomenclature.LegalGroundTypesService;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.webmodel.structure.UserdocRootGroundWebModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/objection")
public class ObjectionController extends GroundsBaseController {

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private LegalGroundTypesService legalGroundTypesService;

    @Override
    protected List<CLegalGroundTypes> fillLegalGroundTypes(UserdocRootGroundWebModel groundWebModel) {
        return legalGroundTypesService.findAllLegalGroundTypesForSpecificPanel(yamlConfig.getUserDocsLegalGroundTypesVersion(), groundWebModel.getPanel());
    }

    @Override
    protected void additionalActionsOnInitializeLegalGroundsModal(CUserdocRootGrounds cUserdocRootGround, UserdocRootGroundWebModel groundWebModel, HttpServletRequest request, String sessionIdentifier,Model model) {

    }

    @Override
    protected void additionalActionsOnEditRootGrounds(CUserdocRootGroundsExt cUserdocRootGround, UserdocRootGroundWebModel rootGroundModalData,HttpServletRequest request,String sessionIdentifier) {

    }

    @Override
    protected void additionalActionsOnValidateUserdocRootGround(CUserdoc userdocForValidation,UserdocRootGroundWebModel rootGroundModalData,HttpServletRequest request,String sessionIdentifier) {

    }

    @Override
    public String getViewPage() {
        return "ipobjects/userdoc/grounds/objection_panel :: objection";
    }

    @Override
    public void setModelAttributes(Model model, String sessionIdentifier) {
        model.addAttribute("hasMultipleRootGrouds",false);
    }

    @Override
    public void setModelAttributesOnOpenEditModal(Model model, UserdocRootGroundWebModel groundWebModel, HttpServletRequest request, String sessionIdentifier) {
        model.addAttribute("hasEarlierRightTypes",false);
        model.addAttribute("hasLegalGroundTypes",true);
        model.addAttribute("hasLegalGroundCategories",false);
    }

    @Override
    public String getPanelPointer() {
        return "objection";
    }
}
