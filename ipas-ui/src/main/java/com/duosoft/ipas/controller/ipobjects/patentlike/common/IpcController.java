package com.duosoft.ipas.controller.ipobjects.patentlike.common;


import bg.duosoft.ipas.core.model.CConfigParam;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.miscellaneous.CIpcClass;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CPatentIpcClass;
import bg.duosoft.ipas.core.service.nomenclature.ClassIpcService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.patent.ipcs.CPatentIpcPkValidator;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.util.PatentIpcsUtils;
import com.duosoft.ipas.util.json.PatentIpcData;
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
@RequestMapping("/patent-like/ipc")
public class IpcController {

    private final String patentIpcsView = "ipobjects/patentlike/common/ipc/ipc_panel :: patent-ipc";

    private final String validIpcsModalView = "ipobjects/patentlike/common/ipc/valid_ipcs_modal :: valid-ipcs-form-modal";

    @Autowired
    private ClassIpcService classIpcService;

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private IpasValidatorCreator validatorCreator;

    @Autowired
    private ConfigParamService configParamService;


    @PostMapping("/edit-panel-ipc")
    public String editIpcPanel(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {
        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        if (!isCancel) {
            List<CPatentIpcClass> ipcs = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_IPCS, sessionIdentifier, request);
            patent.getTechnicalData().setIpcClassList(ipcs);
        }
        model.addAttribute("ipcList", patent.getTechnicalData().getIpcClassList());
        PatentSessionUtils.removePatentIpcSessionObjects(request, sessionIdentifier);
        return patentIpcsView;
    }


    @PostMapping("/copy-from-cpc-list")
    public String addPatentIpcClass(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier) {
        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        List<CPatentIpcClass> ipcList = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_IPCS, sessionIdentifier, request);
        List<CPatentIpcClass> ipcListCopy = classIpcService.copyFromCpcList(ipcList, patent.getTechnicalData().getCpcClassList());
        ipcList.clear();
        ipcList.addAll(ipcListCopy);
        PatentIpcsUtils.sortIpcList(ipcList);
        model.addAttribute("ipcList", ipcList);
        return patentIpcsView;
    }


    @GetMapping(value = "/autocomplete", produces = "application/json")
    @ResponseBody
    public List<PatentIpcData> autocompleteIpcs(@RequestParam String ipcNumber,HttpServletRequest request,
                                                @RequestParam String sessionIdentifier) {
        List<PatentIpcData> autocompleteList = new ArrayList<>();
        CPatent sessionPatent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        CFileId fileId = sessionPatent.getFile().getFileId();

        List<CIpcClass> foundIpcs;
        if (fileId.getFileType().equals(FileType.SPC.code())){
            if (Objects.isNull(sessionPatent.getFile().getRelationshipList()) || CollectionUtils.isEmpty(sessionPatent.getFile().getRelationshipList()))
                return autocompleteList;
            CFileId mainPatentFileId = sessionPatent.getFile().getRelationshipList().get(0).getFileId();
            foundIpcs = classIpcService.findIpcClassesByIpcNumberForSpcs(ipcNumber, yamlConfig.getMaxAutoCompletResults(),mainPatentFileId.getFileSeq(),mainPatentFileId.getFileType(),mainPatentFileId.getFileSeries(),mainPatentFileId.getFileNbr());
        }else{
            foundIpcs = classIpcService.findIpcClassesByIpcNumber(ipcNumber, yamlConfig.getMaxAutoCompletResults());
        }
        foundIpcs.stream().forEach(f -> autocompleteList.add(new PatentIpcData(f)));
        return autocompleteList;
    }


    @PostMapping("/delete")
    public String deleteSpecificIpc(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier, @RequestParam String ipcEdition,
                                    @RequestParam String ipcSection, @RequestParam String ipcClass, @RequestParam String ipcSubclass,
                                    @RequestParam String ipcGroup, @RequestParam String ipcSubgroup) {

        List<CPatentIpcClass> ipcs = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_IPCS, sessionIdentifier, request);
        if (!CollectionUtils.isEmpty(ipcs)) {

            ipcs.removeIf(r -> {
                boolean existIpc = r.getIpcEdition().equalsIgnoreCase(ipcEdition) && r.getIpcSection().equalsIgnoreCase(ipcSection)
                        && r.getIpcClass().equalsIgnoreCase(ipcClass) && r.getIpcSubclass().equalsIgnoreCase(ipcSubclass)
                        && r.getIpcGroup().equalsIgnoreCase(ipcGroup) && r.getIpcSubgroup().equalsIgnoreCase(ipcSubgroup);
                return existIpc;
            });

            PatentIpcsUtils.sortIpcList(ipcs);
        }

        model.addAttribute("ipcList", ipcs);
        return patentIpcsView;
    }


    @PostMapping("/valid-ipcs")
    public String extractValidIpcs(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier, @RequestParam String ipcEdition,
                                    @RequestParam String ipcSection, @RequestParam String ipcClass, @RequestParam String ipcSubclass,
                                    @RequestParam String ipcGroup, @RequestParam String ipcSubgroup) {
        List<CIpcClass> validIpcsById = classIpcService.getValidIpcsById(ipcEdition, ipcSection, ipcClass, ipcSubclass, ipcGroup, ipcSubgroup);
        model.addAttribute("ipcList", validIpcsById);
        return validIpcsModalView;
    }


    @PostMapping("/swap-position")
    public String swapIpcPosition(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier, @RequestParam String ipcEdition,
                                  @RequestParam String ipcSection, @RequestParam String ipcClass, @RequestParam String ipcSubclass,
                                  @RequestParam String ipcGroup, @RequestParam String ipcSubgroup, @RequestParam String ipcQualification,
                                  @RequestParam boolean isHigherPosition) {

        List<CPatentIpcClass> ipcList = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_IPCS, sessionIdentifier, request);
        PatentIpcData patentIpcData = new PatentIpcData();
        patentIpcData.setIpcEdition(ipcEdition);
        patentIpcData.setIpcSection(ipcSection);
        patentIpcData.setIpcClass(ipcClass);
        patentIpcData.setIpcSubclass(ipcSubclass);
        patentIpcData.setIpcGroup(ipcGroup);
        patentIpcData.setIpcSubgroup(ipcSubgroup);
        patentIpcData.setIpcQualification(ipcQualification);
        PatentIpcsUtils.swapIpcPosition(ipcList, patentIpcData, isHigherPosition);
        PatentIpcsUtils.sortIpcList(ipcList);
        model.addAttribute("ipcList", ipcList);
        return patentIpcsView;
    }


    @PostMapping("/add")
    public String addPatentIpcClass(HttpServletRequest request, Model model, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {

        List<CPatentIpcClass> ipcList = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_IPCS, sessionIdentifier, request);
        PatentIpcData patentIpcData = JsonUtil.readJson(data, PatentIpcData.class);
        List<ValidationError> errors;
        CConfigParam cConfigParam = configParamService.selectExtConfigByCode(configParamService.IPC_LATEST_VERSION);

        CPatentIpcClass cPatentIpcClass = new CPatentIpcClass();
        cPatentIpcClass.setIpcEdition(patentIpcData.getIpcEdition());
        cPatentIpcClass.setIpcSection(patentIpcData.getIpcSection());
        cPatentIpcClass.setIpcClass(patentIpcData.getIpcClass());
        cPatentIpcClass.setIpcSubclass(patentIpcData.getIpcSubclass());
        cPatentIpcClass.setIpcGroup(patentIpcData.getIpcGroup());
        cPatentIpcClass.setIpcSubgroup(patentIpcData.getIpcSubgroup());
        cPatentIpcClass.setIpcSymbolDescription(patentIpcData.getIpcName());
        cPatentIpcClass.setIpcVersionCalculated(cConfigParam.getValue());

        IpasTwoArgsValidator<CPatentIpcClass, List<CPatentIpcClass>> validatorIpcs = validatorCreator.createTwoArgsValidator(true, CPatentIpcPkValidator.class);
        errors = validatorIpcs.validate(cPatentIpcClass, ipcList);

        if (CollectionUtils.isEmpty(errors)) {
            ipcList.add(cPatentIpcClass);
            PatentIpcsUtils.sortIpcList(ipcList);
            model.addAttribute("ipcList", ipcList);
            return patentIpcsView;
        } else {
            model.addAttribute("errors", errors);
            model.addAttribute("id", "ipcData");
            return "base/validation :: validation-message";
        }

    }
}
