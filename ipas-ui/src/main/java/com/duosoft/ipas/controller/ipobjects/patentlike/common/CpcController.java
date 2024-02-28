package com.duosoft.ipas.controller.ipobjects.patentlike.common;


import bg.duosoft.ipas.core.model.CConfigParam;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.miscellaneous.CCpcClass;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CPatentCpcClass;
import bg.duosoft.ipas.core.service.nomenclature.ClassCpcService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.patent.cpc.CPatentCpcPkValidator;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.util.PatentCpcsUtils;
import com.duosoft.ipas.util.json.PatentCpcData;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
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
@RequestMapping("/patent-like/cpc")
public class CpcController {

    private final String patentCpcsView = "ipobjects/patentlike/common/cpc/cpc_panel :: patent-cpc";

    private final String validCpcsModalView = "ipobjects/patentlike/common/cpc/valid_cpcs_modal :: valid-cpcs-form-modal";

    @Autowired
    private ClassCpcService classCpcService;

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private IpasValidatorCreator validatorCreator;

    @Autowired
    private ConfigParamService configParamService;


    @PostMapping("/edit-panel-cpc")
    public String editCpcPanel(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {
        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        if (!isCancel) {
            List<CPatentCpcClass> cpcs = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_CPCS, sessionIdentifier, request);
            patent.getTechnicalData().setCpcClassList(cpcs);
        }
        model.addAttribute("cpcList", patent.getTechnicalData().getCpcClassList());
        PatentSessionUtils.removePatentCpcSessionObjects(request, sessionIdentifier);
        return patentCpcsView;
    }


    @GetMapping(value = "/autocomplete", produces = "application/json")
    @ResponseBody
    public List<PatentCpcData> autocompleteCpcs(@RequestParam String cpcNumber, HttpServletRequest request,
                                                @RequestParam String sessionIdentifier) {
        List<PatentCpcData> autocompleteList = new ArrayList<>();
        CPatent sessionPatent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        CFileId fileId = sessionPatent.getFile().getFileId();

        List<CCpcClass> foundCpcs;
        if (fileId.getFileType().equals(FileType.SPC.code())){
            if (Objects.isNull(sessionPatent.getFile().getRelationshipList()) || CollectionUtils.isEmpty(sessionPatent.getFile().getRelationshipList()))
                return autocompleteList;
            CFileId mainPatentFileId = sessionPatent.getFile().getRelationshipList().get(0).getFileId();
            foundCpcs = classCpcService.findCpcClassesByCpcNumberForSpcs(cpcNumber, yamlConfig.getMaxAutoCompletResults(),mainPatentFileId.getFileSeq(),mainPatentFileId.getFileType(),mainPatentFileId.getFileSeries(),mainPatentFileId.getFileNbr());
        }else{
            foundCpcs = classCpcService.findCpcClassesByCpcNumber(cpcNumber, yamlConfig.getMaxAutoCompletResults());
        }
        foundCpcs.stream().forEach(f -> autocompleteList.add(new PatentCpcData(f)));
        return autocompleteList;
    }


    @PostMapping("/delete")
    public String deleteSpecificCpc(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier, @RequestParam String cpcEdition,
                                    @RequestParam String cpcSection, @RequestParam String cpcClass, @RequestParam String cpcSubclass,
                                    @RequestParam String cpcGroup, @RequestParam String cpcSubgroup) {

        List<CPatentCpcClass> cpcs = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_CPCS, sessionIdentifier, request);
        if (!CollectionUtils.isEmpty(cpcs)) {

            cpcs.removeIf(r -> {
                boolean existCpc = r.getCpcEdition().equalsIgnoreCase(cpcEdition) && r.getCpcSection().equalsIgnoreCase(cpcSection)
                        && r.getCpcClass().equalsIgnoreCase(cpcClass) && r.getCpcSubclass().equalsIgnoreCase(cpcSubclass)
                        && r.getCpcGroup().equalsIgnoreCase(cpcGroup) && r.getCpcSubgroup().equalsIgnoreCase(cpcSubgroup);
                return existCpc;
            });

            PatentCpcsUtils.sortCpcList(cpcs);
        }

        model.addAttribute("cpcList", cpcs);
        return patentCpcsView;
    }


    @PostMapping("/valid-cpcs")
    public String extractValidCpcs(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier, @RequestParam String cpcEdition,
                                    @RequestParam String cpcSection, @RequestParam String cpcClass, @RequestParam String cpcSubclass,
                                    @RequestParam String cpcGroup, @RequestParam String cpcSubgroup) {
        List<CCpcClass> validCpcsById = classCpcService.getValidCpcsById(cpcEdition, cpcSection, cpcClass, cpcSubclass, cpcGroup, cpcSubgroup);
        model.addAttribute("cpcList", validCpcsById);
        return validCpcsModalView;
    }


    @PostMapping("/swap-position")
    public String swapCpcPosition(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier, @RequestParam String cpcEdition,
                                  @RequestParam String cpcSection, @RequestParam String cpcClass, @RequestParam String cpcSubclass,
                                  @RequestParam String cpcGroup, @RequestParam String cpcSubgroup, @RequestParam String cpcQualification,
                                  @RequestParam boolean isHigherPosition) {

        List<CPatentCpcClass> cpcList = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_CPCS, sessionIdentifier, request);
        PatentCpcData patentCpcData = new PatentCpcData();
        patentCpcData.setCpcEdition(cpcEdition);
        patentCpcData.setCpcSection(cpcSection);
        patentCpcData.setCpcClass(cpcClass);
        patentCpcData.setCpcSubclass(cpcSubclass);
        patentCpcData.setCpcGroup(cpcGroup);
        patentCpcData.setCpcSubgroup(cpcSubgroup);
        patentCpcData.setCpcQualification(cpcQualification);
        PatentCpcsUtils.swapCpcPosition(cpcList, patentCpcData, isHigherPosition);
        PatentCpcsUtils.sortCpcList(cpcList);
        model.addAttribute("cpcList", cpcList);
        return patentCpcsView;
    }


    @PostMapping("/add")
    public String addPatentCpcClass(HttpServletRequest request, Model model, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {

        List<CPatentCpcClass> cpcList = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_CPCS, sessionIdentifier, request);
        PatentCpcData patentCpcData = JsonUtil.readJson(data, PatentCpcData.class);
        List<ValidationError> errors;
        CConfigParam cConfigParam = configParamService.selectExtConfigByCode(configParamService.CPC_LATEST_VERSION);

        CPatentCpcClass cPatentCpcClass = new CPatentCpcClass();
        cPatentCpcClass.setCpcEdition(patentCpcData.getCpcEdition());
        cPatentCpcClass.setCpcSection(patentCpcData.getCpcSection());
        cPatentCpcClass.setCpcClass(patentCpcData.getCpcClass());
        cPatentCpcClass.setCpcSubclass(patentCpcData.getCpcSubclass());
        cPatentCpcClass.setCpcGroup(patentCpcData.getCpcGroup());
        cPatentCpcClass.setCpcSubgroup(patentCpcData.getCpcSubgroup());
        cPatentCpcClass.setCpcName(patentCpcData.getCpcName());
        cPatentCpcClass.setCpcVersionCalculated(cConfigParam.getValue());

        IpasTwoArgsValidator<CPatentCpcClass, List<CPatentCpcClass>> validatorCpcs = validatorCreator.createTwoArgsValidator(true, CPatentCpcPkValidator.class);
        errors = validatorCpcs.validate(cPatentCpcClass, cpcList);

        if (CollectionUtils.isEmpty(errors)) {
            cpcList.add(cPatentCpcClass);
            PatentCpcsUtils.sortCpcList(cpcList);
            model.addAttribute("cpcList", cpcList);
            return patentCpcsView;
        } else {
            model.addAttribute("errors", errors);
            model.addAttribute("id", "cpcData");
            return "base/validation :: validation-message";
        }

    }
}
