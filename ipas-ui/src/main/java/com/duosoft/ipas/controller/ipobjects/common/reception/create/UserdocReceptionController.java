package com.duosoft.ipas.controller.ipobjects.common.reception.create;

import bg.duosoft.ipas.core.model.IpasApplicationSearchResult;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.process.CProcessEvent;
import bg.duosoft.ipas.core.service.application.SearchApplicationService;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.nomenclature.FileTypeGroupService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.reception.ReceptionTypeService;
import bg.duosoft.ipas.core.service.reception.UserdocReceptionRelationService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.enums.ApplicationSearchType;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.ProcessEventType;
import bg.duosoft.ipas.util.json.JsonUtil;
import bg.duosoft.ipas.util.process.ProcessEventUtils;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.ProcessUtils;
import com.duosoft.ipas.util.json.CreateReceptionData;
import com.duosoft.ipas.util.reception.CreateReceptionUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.reception.ReceptionSessionUtils;
import com.duosoft.ipas.webmodel.ReceptionForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/reception/create")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ReceptionCreator.code())")
public class UserdocReceptionController {

    private static final String SEARCH_USERDOC_RELATED_OBJECT_RADIO_COOKIE = "search-userdoc-related-object-radio";

    @Autowired
    private SearchApplicationService searchApplicationService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private UserdocReceptionRelationService userdocReceptionRelationService;

    @Autowired
    private ReceptionTypeService receptionTypeService;

    @Autowired
    private UserdocService userdocService;

    @Autowired
    private FileService fileService;

    @Autowired
    private DocService docService;
    @Autowired
    private FileTypeGroupService fileTypeGroupService;

    @PostMapping("/open-userdoc-related-object-modal")
    public String openUserdocRelatedObjectModal(Model model, HttpServletRequest request,
                                                @CookieValue(name = SEARCH_USERDOC_RELATED_OBJECT_RADIO_COOKIE, defaultValue = "fileNumber") String checkedSearchTypeRadio,
                                                @RequestParam String sessionIdentifier,
                                                @RequestParam String relatedObjectFileTypeGroup) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);
        model.addAttribute("fileTypeGroup", fileTypeGroupService.getFileTypeGroup(relatedObjectFileTypeGroup));
        model.addAttribute("checkedSearchTypeRadio", checkedSearchTypeRadio);
        return "ipobjects/common/reception/userdoc/search_object_modal :: modal";
    }

    @PostMapping("/search-userdoc-related-object")
    public String searchUserdocRelatedObject(Model model, HttpServletRequest request,
                                             @RequestParam String sessionIdentifier,
                                             @RequestParam String searchText,
                                             @RequestParam String searchType,
                                             @RequestParam String fileTypeGroup) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);
        boolean isSearchTextNumber = CoreUtils.isNumber(searchText);
        if (isSearchTextNumber) {
            List<FileType> fileTypes = fileTypeGroupService.getFileTypesByGroup(fileTypeGroup).stream().map(r -> FileType.selectByCode(r)).collect(Collectors.toList());
            List<IpasApplicationSearchResult> applications = searchApplicationService.selectApplications(fileTypes, CoreUtils.toSixSymbols(searchText), ApplicationSearchType.selectByCode(searchType), true);
            if (!CollectionUtils.isEmpty(applications)) {
                Comparator<IpasApplicationSearchResult> filingNumberComparator = Comparator.comparing(s -> s.getFileId().createFilingNumber());
                applications.sort(filingNumberComparator.reversed());
            }
            model.addAttribute("resultList", applications);
        }
        return "ipobjects/common/reception/userdoc/search_object_modal :: search-result";
    }

    @PostMapping("/change-userdoc-related-object-type")
    public String changeUserdocRelatedObjectType(Model model, HttpServletRequest request, @RequestParam String sessionIdentifier, @RequestParam String data) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);
        CreateReceptionData formData = JsonUtil.readJson(data, CreateReceptionData.class);
        ReceptionForm receptionForm = ReceptionSessionUtils.setFormDataToSessionReception(request, sessionIdentifier, formData,receptionTypeService);
        receptionForm.getUserdoc().setObjectNumber(null);
        model.addAttribute("receptionForm", receptionForm);
        return "ipobjects/common/reception/userdoc/userdoc_related_object :: object";
    }

    @PostMapping("/add-userdoc-related-object")
    public String addUserdocRelatedObject(Model model, HttpServletRequest request, @RequestParam String sessionIdentifier, @RequestParam String filingNumber, @RequestParam String process) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);
        CProcess cProcess = ProcessUtils.selectProcess(process, processService, true);
        model.addAttribute("process", cProcess);
        model.addAttribute("filingNumber", filingNumber);
        model.addAttribute("childrenIds", processService.selectProcessIdsOfUserdocAndOffidocChildren(ProcessUtils.getProcessIdFromString(process)));
        return "ipobjects/common/reception/userdoc/process/process_tree_modal :: modal";
    }

    @PostMapping("/save-related-userdoc-object")
    public String saveRelatedUserdocObject(Model model, HttpServletRequest request, @RequestParam String sessionIdentifier, @RequestParam String filingNumber) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);
        ReceptionForm receptionForm = HttpSessionUtils.getSessionAttribute(sessionIdentifier, request);
        additionalInitializationsOnSaveRelatedUserdocObject(receptionForm, filingNumber);
        model.addAttribute("receptionForm", receptionForm);
        model.addAttribute("userdocReceptionRelations", CreateReceptionUtils.getUserdocTypes(receptionForm, userdocService, receptionTypeService, userdocReceptionRelationService));
        model.addAttribute("relatedUserdocObjectDetails", CreateReceptionUtils.fillRelatedUserdocObjectDetails(receptionForm, fileService, userdocService, docService));
        return "ipobjects/common/reception/userdoc/userdoc_related_object :: object";
    }

    private void additionalInitializationsOnSaveRelatedUserdocObject(ReceptionForm receptionForm, String filingNumber) {
        if (StringUtils.isEmpty(filingNumber))
            return;

        receptionForm.getUserdoc().setObjectNumber(filingNumber);
    }

    @PostMapping("/delete-related-userdoc-object")
    public String deleteRelatedUserdocObject(Model model, HttpServletRequest request, @RequestParam String sessionIdentifier) {
        ReceptionSessionUtils.checkSessionObjectExisting(sessionIdentifier, request);
        ReceptionForm receptionForm = HttpSessionUtils.getSessionAttribute(sessionIdentifier, request);
        receptionForm.getUserdoc().setObjectNumber(null);
        model.addAttribute("receptionForm", receptionForm);
        return "ipobjects/common/reception/userdoc/userdoc_related_object :: object";
    }

    @PostMapping("/load-userdoc-process-tree")
    public String loadProcessTree(Model model,
                                  @RequestParam String mainProcessId,
                                  @RequestParam(required = false) String parentProcess,
                                  @RequestParam String processEventId,
                                  @RequestParam String processEventType) {
        CProcess cProcess;
        if (StringUtils.isEmpty(parentProcess)) {
            cProcess = ProcessUtils.selectProcess(mainProcessId, processService, true);
        } else {
            cProcess = ProcessUtils.selectProcess(parentProcess, processService, true);
        }

        List<CProcessEvent> processEventList = cProcess.getProcessEventList();
        if (CollectionUtils.isEmpty(processEventList))
            throw new RuntimeException("Process Event List is emtpy ! " + mainProcessId);

        ProcessEventType type = Enum.valueOf(ProcessEventType.class, processEventType);
        CProcessEvent cProcessEvent = ProcessEventUtils.getSelectedProcessEvent(processEventId, processEventList, type);

        if (Objects.isNull(cProcessEvent))
            throw new RuntimeException("Cannot find process event - " + mainProcessId + ", " + processEventId);

        model.addAttribute("processEvent", cProcessEvent);
        model.addAttribute("isFullyLoad", true);
        model.addAttribute("childrenIds", processService.selectProcessIdsOfUserdocAndOffidocChildren(ProcessUtils.getProcessIdFromString(processEventId)));
        return "ipobjects/common/reception/userdoc/process/process_tree :: node";
    }

}
