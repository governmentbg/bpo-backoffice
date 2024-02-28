package com.duosoft.ipas.controller.ipobjects.patentlike.design;

import bg.duosoft.ipas.core.model.design.CLocarnoClasses;
import bg.duosoft.ipas.core.model.design.CPatentLocarnoClasses;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.service.nomenclature.LocarnoClassesService;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.design.SingleDesignLocarnoClassesValidatorOnAdd;
import bg.duosoft.ipas.core.validation.design.SingleDesignUniqueLocarnoClassValidator;
import bg.duosoft.ipas.util.DefaultValue;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.json.DesignLocarnoData;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/design/locarno")
public class DesignLocarnoClassesController {

    private final String singleDesignLocarnoClassesView = "ipobjects/patentlike/design/single_design_locarno_classes :: single_design_locarno_classes";

    @Autowired
    private LocarnoClassesService locarnoClassesService;

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private IpasValidatorCreator validatorCreator;

    @PostMapping("/delete")
    public String deleteLocarnoClass(HttpServletRequest request, Model model, @RequestParam String locarnoClassCode,
                                  @RequestParam String locarnoEditionCode, @RequestParam String sessionIdentifier,
                                  @RequestParam String filingNumber) {
        List<CPatent> singleDesignsOnEdit = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_SINGLE_DESIGNS_ON_EDIT, sessionIdentifier, request);
        CFileId cFileId = CoreUtils.createCFileId(filingNumber);
        CPatent singleDesignToEdit = singleDesignsOnEdit.stream().filter(r->r.getFile().getFileId().equals(cFileId)).findFirst().orElse(null);
        singleDesignToEdit.getTechnicalData().getLocarnoClassList().removeIf(r->r.getLocarnoClassCode().equals(locarnoClassCode) && r.getLocarnoEditionCode().equals(locarnoEditionCode));
        model.addAttribute("filingNumber",filingNumber);
        model.addAttribute("singleDesign",singleDesignToEdit);
        return singleDesignLocarnoClassesView;
    }


    @PostMapping("/add")
    public String addLocarnoClass(HttpServletRequest request, Model model, @RequestParam String locarnoClassCode,
                                  @RequestParam(required = false) String locarnoEditionCode, @RequestParam String sessionIdentifier,
                                  @RequestParam String filingNumber) {
        List<CPatent> singleDesignsOnEdit = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_SINGLE_DESIGNS_ON_EDIT, sessionIdentifier, request);
        CFileId cFileId = CoreUtils.createCFileId(filingNumber);
        CPatent singleDesignToEdit = singleDesignsOnEdit.stream().filter(r->r.getFile().getFileId().equals(cFileId)).findFirst().orElse(null);
        List<ValidationError> errors;

        CPatentLocarnoClasses cPatentLocarnoClasses=new CPatentLocarnoClasses();
        if(Objects.isNull(locarnoEditionCode) || locarnoEditionCode.isEmpty()){
            cPatentLocarnoClasses.setLocarnoEditionCode(DefaultValue.DEFAULT_LOCARNO_EDITION_CODE);
        }else{
            cPatentLocarnoClasses.setLocarnoEditionCode(locarnoEditionCode);
        }
        cPatentLocarnoClasses.setLocarnoClassCode(locarnoClassCode);
        if (Objects.isNull(singleDesignToEdit.getTechnicalData().getLocarnoClassList())){
            singleDesignToEdit.getTechnicalData().setLocarnoClassList(new ArrayList<>());
        }

        IpasTwoArgsValidator<CPatentLocarnoClasses, List<CPatentLocarnoClasses>> validatorLocarnoClasses = validatorCreator.createTwoArgsValidator(true, SingleDesignLocarnoClassesValidatorOnAdd.class);

        errors = validatorLocarnoClasses.validate(cPatentLocarnoClasses,  singleDesignToEdit.getTechnicalData().getLocarnoClassList());
        if (CollectionUtils.isEmpty(errors)) {

            singleDesignToEdit.getTechnicalData().getLocarnoClassList().add(cPatentLocarnoClasses);
            model.addAttribute("filingNumber",filingNumber);
            model.addAttribute("singleDesign",singleDesignToEdit);
            return singleDesignLocarnoClassesView;
        }else{
            model.addAttribute("errors", errors);
            model.addAttribute("id", "locarnoClassesData");
            return "base/validation :: validation-message";
        }
    }

    @GetMapping(value = "/autocomplete", produces = "application/json")
    @ResponseBody
    public List<DesignLocarnoData> autocompleteLocarnoClasses(@RequestParam String locarnoClassCode, HttpServletRequest request) {
        List<DesignLocarnoData> autocompleteList = new ArrayList<>();
        List<CLocarnoClasses> cLocarnoClasses = locarnoClassesService.findByLocarnoClassCode(locarnoClassCode,yamlConfig.getMaxAutoCompletResults());
        cLocarnoClasses.stream().forEach(r->autocompleteList.add(new DesignLocarnoData(r)));
        return autocompleteList;
    }



}
