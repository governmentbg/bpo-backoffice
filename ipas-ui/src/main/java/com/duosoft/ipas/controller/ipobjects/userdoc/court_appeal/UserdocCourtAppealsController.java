package com.duosoft.ipas.controller.ipobjects.userdoc.court_appeal;

import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.court_appeal.CUserdocCourtAppeal;
import bg.duosoft.ipas.core.model.userdoc.grounds.CUserdocRootGrounds;
import bg.duosoft.ipas.core.service.nomenclature.CourtsService;
import bg.duosoft.ipas.core.service.nomenclature.JudicialActTypeService;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import com.duosoft.ipas.webmodel.structure.UserdocCourtAppealWebModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/userdoc-court-appeals")
public class UserdocCourtAppealsController {

    @Autowired
    private CourtsService courtsService;

    @Autowired
    private JudicialActTypeService judicialActTypeService;

    @PostMapping("/save-userdoc-court-appeals")
    public String save(HttpServletRequest request, Model model, @RequestParam Boolean isCancel,
                                  @RequestParam String sessionIdentifier) {
        CUserdoc userdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        if (!isCancel) {
             List<CUserdocCourtAppeal> cUserdocCourtAppeals = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_COURT_APPEALS, sessionIdentifier, request);
             userdoc.setUserdocCourtAppeals(cUserdocCourtAppeals);
        }
        HttpSessionUtils.removeSessionAttributes(request,sessionIdentifier,UserdocSessionObjects.SESSION_USERDOC_COURT_APPEALS);
        model.addAttribute("userdocCourtAppeals",userdoc.getUserdocCourtAppeals());
        return "ipobjects/userdoc/court_appeal/court_appeals :: court_appeals";
    }

    @PostMapping("/delete-userdoc-court-appeal")
    public String delete(HttpServletRequest request, Model model,
                                   @RequestParam String sessionIdentifier,
                                   @RequestParam Integer courtAppealId) {
        List<CUserdocCourtAppeal> cUserdocCourtAppeals = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_COURT_APPEALS, sessionIdentifier, request);
        cUserdocCourtAppeals.removeIf(courtAppeal->courtAppeal.getCourtAppealId().equals(courtAppealId));
        model.addAttribute("userdocCourtAppeals",cUserdocCourtAppeals);
        return "ipobjects/userdoc/court_appeal/court_appeals :: court_appeals";
    }

    @PostMapping("/open-edit-court-appeals-modal")
    public String openEditModal(HttpServletRequest request, Model model,
                                             @RequestParam String sessionIdentifier,
                                             @RequestParam(required = false) Integer courtAppealId) {
        UserdocCourtAppealWebModel courtAppealObj= new UserdocCourtAppealWebModel();
        if (Objects.nonNull(courtAppealId)){
            List<CUserdocCourtAppeal> cUserdocCourtAppeals = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_COURT_APPEALS, sessionIdentifier, request);
           CUserdocCourtAppeal cAppeal= cUserdocCourtAppeals.stream().filter(r->r.getCourtAppealId().equals(courtAppealId)).findFirst().orElse(null);
            UserdocCourtAppealWebModel.initWebObject(cAppeal,courtAppealObj);
        }
        model.addAttribute("judicialActTypes",judicialActTypeService.findAll());
        model.addAttribute("courtAppeal",courtAppealObj);
        return "ipobjects/userdoc/court_appeal/court_appeals :: court-appeals-modal";
    }



    private List<ValidationError> validate(UserdocCourtAppealWebModel webModelData){
        List<ValidationError> errors = new ArrayList<>();

        if (StringUtils.isEmpty(webModelData.getCourtName())){
            errors.add(ValidationError.builder().pointer("court.appeal.name").messageCode("required.field").build());
        }

        if (StringUtils.isEmpty(webModelData.getCourtCaseNbr())){
            errors.add(ValidationError.builder().pointer("court.case.nbr").messageCode("required.field").build());
        }else{
            if (!CoreUtils.isNumber(webModelData.getCourtCaseNbr())){
                errors.add(ValidationError.builder().pointer("court.case.nbr").messageCode("required.number").build());
            }
        }

        if (StringUtils.isEmpty(webModelData.getCourtCaseDate())){
            errors.add(ValidationError.builder().pointer("court.case.date").messageCode("required.field").build());
        }

        if (!StringUtils.isEmpty(webModelData.getJudicialActNbr()) && !CoreUtils.isNumber(webModelData.getJudicialActNbr())){
            errors.add(ValidationError.builder().pointer("judicial.act.number").messageCode("required.number").build());
        }

        return errors;
    }

    @PostMapping("/edit-userdoc-court-appeals")
    public String editAppeal(HttpServletRequest request, Model model,
                                  @RequestParam String sessionIdentifier, @RequestParam(required = false) String data) {

        UserdocCourtAppealWebModel userdocRootGroundWebModel = JsonUtil.readJson(data, UserdocCourtAppealWebModel.class);
        List<ValidationError> errors = validate(userdocRootGroundWebModel);
        if (!CollectionUtils.isEmpty(errors)){
            model.addAttribute("judicialActTypes",judicialActTypeService.findAll());
            model.addAttribute("courtAppeal",userdocRootGroundWebModel);
            model.addAttribute("validationErrors",errors);
            return "ipobjects/userdoc/court_appeal/court_appeals :: court-appeals-modal";
        }
        List<CUserdocCourtAppeal> cUserdocCourtAppeals = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_COURT_APPEALS, sessionIdentifier, request);
        CUserdocCourtAppeal courtAppeal= new CUserdocCourtAppeal();
        UserdocCourtAppealWebModel.initCObject(courtAppeal,userdocRootGroundWebModel,courtsService,judicialActTypeService);

        if (Objects.isNull(courtAppeal.getCourtAppealId())) {
            Integer newCourtAppealId= 0;
            if (!CollectionUtils.isEmpty(cUserdocCourtAppeals)){
                newCourtAppealId = cUserdocCourtAppeals
                        .stream()
                        .mapToInt(r -> r.getCourtAppealId())
                        .max().orElseThrow(NoSuchElementException::new);
            }
            courtAppeal.setCourtAppealId(++newCourtAppealId);

        }else{
            cUserdocCourtAppeals.removeIf(r->r.getCourtAppealId().equals(courtAppeal.getCourtAppealId()));
        }
        cUserdocCourtAppeals.add(courtAppeal);
        cUserdocCourtAppeals.sort(Comparator.comparing(CUserdocCourtAppeal::getCourtAppealId));
        model.addAttribute("userdocCourtAppeals",cUserdocCourtAppeals);
        return "ipobjects/userdoc/court_appeal/court_appeals :: court_appeals";

    }


}
