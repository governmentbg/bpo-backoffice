package com.duosoft.ipas.controller.ipobjects.userdoc;

import bg.duosoft.ipas.core.model.IpasApplicationSearchResult;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.service.application.SearchApplicationService;
import bg.duosoft.ipas.core.service.nomenclature.FileTypeService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.reception.ReceptionUserdocRequestService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.userdoc.UserdocChangePositionValidator;
import bg.duosoft.ipas.enums.ApplicationSearchType;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.ProcessUtils;
import com.duosoft.ipas.util.json.UserdocMainData;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
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
@RequestMapping("/userdoc/main-data")
public class UserdocMainDataController {

    @Autowired
    private ReceptionUserdocRequestService receptionUserdocRequestService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private FileTypeService fileTypeService;

    @Autowired
    private SearchApplicationService searchApplicationService;

    @Autowired
    private IpasValidatorCreator validatorCreator;


    @PostMapping("/edit-data")
    public String editDetailPanel(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {
        CUserdoc userdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        if (!isCancel)
            updateSessionObject(data, userdoc, request, sessionIdentifier);

        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        model.addAttribute("userdoc", userdoc);
        model.addAttribute("receptionUserdocRequest", receptionUserdocRequestService.selectReceptionByDocumentId(userdoc.getDocumentId().getDocOrigin(), userdoc.getDocumentId().getDocLog(), userdoc.getDocumentId().getDocSeries(), userdoc.getDocumentId().getDocNbr()));
        return "ipobjects/userdoc/main_data/main_data_panel :: panel";
    }


    private void updateSessionObject(String data, CUserdoc userdoc, HttpServletRequest request, String sessionIdentifier) {
        UserdocMainData formData = JsonUtil.readJson(data, UserdocMainData.class);
        userdoc.setNotes(formData.getNotes());
        userdoc.getDocument().setIndFaxReception(formData.getIndFaxReception());

        Date sessionUserdocFilingDate = userdoc.getDocument().getFilingDate();
        Date formDataFilingDate = formData.getFilingDate();
        if (!DateUtils.isDateEquals(sessionUserdocFilingDate, formDataFilingDate))
            userdoc.getDocument().setFilingDate(formDataFilingDate);

        CProcessParentData sessionUserdocParentData = UserdocSessionUtils.getSessionUserdocParentData(request, sessionIdentifier);
        userdoc.setUserdocParentData(sessionUserdocParentData);
        if (StringUtils.hasText(formData.getEfilingDataLoginName())){
            userdoc.getUserdocEFilingData().setLogUserName(formData.getEfilingDataLoginName());
        }
    }

    @PostMapping("/open-search-objects-modal")
    public String openSearchObjectsModal(Model model) {
        Map<String, String> fileTypesMap = fileTypeService.getFileTypesMap();
        fileTypesMap.remove(FileType.INTERNATIONAL_MARK.code());
        fileTypesMap.remove(FileType.GEOGRAPHICAL_INDICATIONS_V.code());
        fileTypesMap.remove(FileType.SINGLE_DESIGN.code());
        fileTypesMap.remove(FileType.INTERNATIONAL_NP.code());
        fileTypesMap.remove(FileType.INTERNATIONAL_SINGLE_DESIGN.code());
        fileTypesMap.remove(FileType.INTERNATIONAL_DESIGN.code());
        model.addAttribute("fileTypesMap", fileTypeService.getFileTypesMap());
        return "ipobjects/userdoc/main_data/change_position/search_object_modal :: modal";
    }


    @PostMapping("/search-objects")
    public String searchUserdocRelatedObject(Model model, @RequestParam String searchText, @RequestParam String searchType, @RequestParam String fileType) {
        boolean isSearchTextNumber = CoreUtils.isNumber(searchText);
        if (isSearchTextNumber) {
            List<IpasApplicationSearchResult> applications = searchApplicationService.selectApplications(Arrays.asList(FileType.selectByCode(fileType)), CoreUtils.toSixSymbols(searchText), ApplicationSearchType.selectByCode(searchType), false);
            model.addAttribute("resultList", applications);

            FileType fileTypeEnum = FileType.selectByCode(fileType);
            if (fileTypeEnum == FileType.EU_PATENT && CollectionUtils.isEmpty(applications)) {
                model.addAttribute("showEpoPatentsButton", true);
            }
        }
        return "ipobjects/userdoc/main_data/change_position/search_object_modal :: search-result";
    }


    @PostMapping("/open-change-position-modal")
    public String openChangePositionModal(Model model, HttpServletRequest request, @RequestParam String sessionIdentifier, @RequestParam(required = false) String process) {
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        CProcessParentData sessionUserdocParentData = UserdocSessionUtils.getSessionUserdocParentData(request, sessionIdentifier);

        CProcessId topProcessId;
        if (StringUtils.isEmpty(process))
            topProcessId = sessionUserdocParentData.getTopProcessId();
        else
            topProcessId = ProcessUtils.getProcessIdFromString(process);

        CProcess topProcess = processService.selectProcess(topProcessId, true);
        model.addAttribute("process", topProcess);
        model.addAttribute("mainProcessUpperId", ProcessUtils.selectFirstParentOfProcess(sessionUserdocParentData));
        model.addAttribute("mainProcessId", sessionUserdoc.getProcessSimpleData().getProcessId());
        return "ipobjects/userdoc/main_data/change_position/process_tree_modal :: modal";
    }

    @PostMapping("/change-userdoc-position")
    public String changeUserdocParent(Model model, HttpServletRequest request, @RequestParam String sessionIdentifier, @RequestParam String parentProcessId) {
        CProcessId newParentProcessId = ProcessUtils.getProcessIdFromString(parentProcessId);
        if (Objects.isNull(newParentProcessId))
            throw new RuntimeException("New process parent id is empty !");

        CProcessParentData sessionUserdocParentData = UserdocSessionUtils.getSessionUserdocParentData(request, sessionIdentifier);
        CProcessId processId = ProcessUtils.selectFirstParentOfProcess(sessionUserdocParentData);
        if (Objects.isNull(processId))
            throw new RuntimeException("First process parent id is empty !");

        boolean sameProcess = ProcessUtils.isSameProcess(newParentProcessId, processId);
        if (!sameProcess) {
            CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
            IpasValidator<CUserdoc> validator = validatorCreator.create(false, UserdocChangePositionValidator.class);
            List<ValidationError> validationErrors = validator.validate(sessionUserdoc, newParentProcessId);
            if (CollectionUtils.isEmpty(validationErrors)) {
                CProcessParentData cProcessParentData = processService.generateProcessHierarchy(newParentProcessId);
                HttpSessionUtils.setSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_PROCESS_PARENT_DATA, sessionIdentifier, cProcessParentData, request);
            } else {
                model.addAttribute("validationErrors", validationErrors);
                return "ipobjects/userdoc/main_data/change_position/process_tree_modal :: errors";
            }
        }
        model.addAttribute("userdocParentData", UserdocSessionUtils.getSessionUserdocParentData(request, sessionIdentifier));
        return "ipobjects/userdoc/main_data/main_data_panel :: userdoc-parent";
    }
}
