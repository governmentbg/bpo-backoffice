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
@RequestMapping("/patent-annulment-request")
public class PatentAnnulmentRequestController extends GroundsBaseController {

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private LegalGroundTypesService legalGroundTypesService;

    private static final String DB_PANEL_NAME = "PatentAnnulmentRequest";

    @Override
    protected List<CLegalGroundTypes> fillLegalGroundTypes(UserdocRootGroundWebModel groundWebModel) {
        return legalGroundTypesService.findAllLegalGroundTypesForSpecificPanel(yamlConfig.getUserDocsLegalGroundTypesVersion(), DB_PANEL_NAME);
    }

    @Override
    protected void additionalActionsOnInitializeLegalGroundsModal(CUserdocRootGrounds cUserdocRootGround, UserdocRootGroundWebModel groundWebModel, HttpServletRequest request, String sessionIdentifier,Model model) {
        groundWebModel.initPatentGroundPartialInvalidityWebData(groundWebModel,cUserdocRootGround);
    }

    @Override
    protected void additionalActionsOnEditRootGrounds(CUserdocRootGroundsExt cUserdocRootGround, UserdocRootGroundWebModel rootGroundModalData, HttpServletRequest request, String sessionIdentifier) {
        rootGroundModalData.initPatentGroundPartialInvalidityCoreData(cUserdocRootGround,rootGroundModalData);
    }

    @Override
    protected void additionalActionsOnValidateUserdocRootGround(CUserdoc userdocForValidation, UserdocRootGroundWebModel rootGroundModalData, HttpServletRequest request, String sessionIdentifier) {
    }

    @Override
    public String getViewPage() {
        return "ipobjects/userdoc/grounds/patent_annulment_request_panel :: patent-annulment";
    }

    @Override
    public void setModelAttributes(Model model, String sessionIdentifier) {
        model.addAttribute("hasMultipleRootGrouds",true);
    }

    @Override
    public void setModelAttributesOnOpenEditModal(Model model, UserdocRootGroundWebModel groundWebModel, HttpServletRequest request, String sessionIdentifier) {
        model.addAttribute("hasEarlierRightTypes",false);
        model.addAttribute("hasLegalGroundTypes",true);
        model.addAttribute("hasLegalGroundCategories",false);
        model.addAttribute("hasPatentGroundData",true);
    }

    @Override
    public String getPanelPointer() {
        return "patent-annulment-request";
    }
}
