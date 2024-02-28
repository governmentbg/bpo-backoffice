package com.duosoft.ipas.controller.ipobjects.userdoc.single_design;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CUserdocSingleDesign;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.service.nomenclature.ApplicationTypeService;
import bg.duosoft.ipas.core.service.patent.DesignService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.util.design.SingleDesignUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import com.duosoft.ipas.util.DesignUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Slf4j
@Controller
@RequestMapping("/userdoc/single-design")
public class UserdocSingleDesignController {

    @Autowired
    private DesignService designService;

    @Autowired
    private PatentService patentService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @PostMapping("/update")
    public String update(HttpServletRequest request, Model model,
                         @RequestParam String sessionIdentifier,
                         @RequestParam Boolean isAllSingleDesignsIncluded) {
        CFileId fileId = selectMainDesignFileId(request, sessionIdentifier);

        List<CUserdocSingleDesign> singleDesignsList = null;
        if (!isAllSingleDesignsIncluded) {
            singleDesignsList = getSessionSingleDesigns(request, sessionIdentifier);
            if (CollectionUtils.isEmpty(singleDesignsList)) {
                singleDesignsList = UserdocSingleDesignController.selectOriginalOrSessionSingleDesigns(request, sessionIdentifier, true, fileId, designService, patentService, applicationTypeService);
                setSessionSingleDesignsList(request, sessionIdentifier, singleDesignsList);
            }
        }
        return selectSingleDesignsTable(model, singleDesignsList, fileId);
    }

    @PostMapping("/delete")
    public String delete(HttpServletRequest request, Model model,
                         @RequestParam String sessionIdentifier,
                         @RequestParam String filingNumber) {
        CFileId fileId = selectMainDesignFileId(request, sessionIdentifier);
        List<CUserdocSingleDesign> sessionSingleDesigns = getSessionSingleDesigns(request, sessionIdentifier);
        if (!CollectionUtils.isEmpty(sessionSingleDesigns)) {
            CUserdocSingleDesign toBeDeleted = findSingleDesignInList(filingNumber, sessionSingleDesigns);
            sessionSingleDesigns.remove(toBeDeleted);
        }
        return selectSingleDesignsTable(model, sessionSingleDesigns, fileId);
    }

    @PostMapping("/delete-all")
    public String deleteAll(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier) {
        CFileId fileId = selectMainDesignFileId(request, sessionIdentifier);
        List<CUserdocSingleDesign> currentSessionList = getSessionSingleDesigns(request, sessionIdentifier);
        currentSessionList.clear();
        return selectSingleDesignsTable(model, currentSessionList, fileId);
    }

    @PostMapping("/restore-original")
    public String restoreOriginalNiceClasses(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier) {
        CFileId fileId = selectMainDesignFileId(request, sessionIdentifier);

        List<CUserdocSingleDesign> singleDesignsList = UserdocSingleDesignController.selectOriginalOrSessionSingleDesigns(request, sessionIdentifier, true, fileId, designService, patentService, applicationTypeService);
        setSessionSingleDesignsList(request, sessionIdentifier, singleDesignsList);

        List<CUserdocSingleDesign> sessionSingleDesigns = getSessionSingleDesigns(request, sessionIdentifier);
        return selectSingleDesignsTable(model, sessionSingleDesigns, fileId);
    }

    @PostMapping("/select-add-original-design")
    public String restoreFromOriginalClassesList(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier) {
        List<CUserdocSingleDesign> currentSessionList = getSessionSingleDesigns(request, sessionIdentifier);
        CFileId mainDesignFileId = selectMainDesignFileId(request, sessionIdentifier);

        Map<String, String> map = new LinkedHashMap<>();
        List<CUserdocSingleDesign> originalDesignsList = UserdocSingleDesignController.selectOriginalOrSessionSingleDesigns(request, sessionIdentifier, true, mainDesignFileId, designService, patentService, applicationTypeService);
        originalDesignsList.forEach(original -> {
            String filingNumber = original.getFileId().createFilingNumber();
            if (findSingleDesignInList(filingNumber, currentSessionList) == null) {
                map.put(filingNumber, DesignUtils.formatSingleDesignIdentityNumber(mainDesignFileId.getFileNbr(), filingNumber));
            }
        });

        model.addAttribute("map", map.size() > 0 ? map : null);
        return "ipobjects/userdoc/extra_data/single_designs :: single-design-choose-modal";
    }

    @PostMapping("/save-design-choice")
    public String saveClassChoice(HttpServletRequest request, Model model,
                                  @RequestParam String sessionIdentifier,
                                  @RequestParam String filingNumber) {
        List<CUserdocSingleDesign> currentSessionList = getSessionSingleDesigns(request, sessionIdentifier);
        CFileId fileId = selectMainDesignFileId(request, sessionIdentifier);

        List<CUserdocSingleDesign> originalDesignsList = UserdocSingleDesignController.selectOriginalOrSessionSingleDesigns(request, sessionIdentifier, true, fileId, designService, patentService, applicationTypeService);
        CUserdocSingleDesign toAdd = findSingleDesignInList(filingNumber, originalDesignsList);
        if (toAdd != null) {
            CUserdocSingleDesign cloned = (CUserdocSingleDesign) SerializationUtils.clone(toAdd);
            currentSessionList.add(cloned);
            currentSessionList.sort(Comparator.comparing(userdocSingleDesign -> userdocSingleDesign.getFileId().createFilingNumber()));
        }

        return selectSingleDesignsTable(model, currentSessionList, fileId);
    }

    private List<CUserdocSingleDesign> getSessionSingleDesigns(HttpServletRequest request, String sessionIdentifier) {
        return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_SINGLE_DESIGNS, sessionIdentifier, request);
    }

    private void setSessionSingleDesignsList(HttpServletRequest request, String sessionIdentifier, List<CUserdocSingleDesign> singleDesignsList) {
        HttpSessionUtils.setSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_SINGLE_DESIGNS, sessionIdentifier, singleDesignsList, request);
    }

    public static List<CUserdocSingleDesign> selectOriginalOrSessionSingleDesigns(HttpServletRequest request, String sessionIdentifier, Boolean isAllSingleDesignsIncluded, CFileId fileId, DesignService designService, PatentService patentService, ApplicationTypeService applicationTypeService) {
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        List<CUserdocSingleDesign> singleDesignsList;
        if (Objects.nonNull(isAllSingleDesignsIncluded) && isAllSingleDesignsIncluded) {
            CPatent mainDesign = patentService.findPatent(fileId, false);
            List<CPatent> originalSingleDesigns = designService.getAllSingleDesignsForIndustrialDesign(mainDesign, false);
            singleDesignsList = SingleDesignUtils.convertToUserdocSingleDesign(originalSingleDesigns, sessionUserdoc.getDocumentId(), applicationTypeService);
        } else {
            singleDesignsList = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_SINGLE_DESIGNS, sessionIdentifier, request);
        }
        return singleDesignsList;
    }

    private String selectSingleDesignsTable(Model model, List<CUserdocSingleDesign> singleDesignsList, CFileId mainDesignFileId) {
        model.addAttribute("mainDesignFileId", mainDesignFileId);
        model.addAttribute("singleDesignsList", singleDesignsList);
        return "ipobjects/userdoc/extra_data/single_designs :: single-designs-table";
    }

    private CUserdocSingleDesign findSingleDesignInList(String filingNumber, List<CUserdocSingleDesign> singleDesignList) {
        return singleDesignList.stream()
                .filter(design -> Objects.equals(design.getFileId().createFilingNumber(), filingNumber))
                .findFirst()
                .orElse(null);
    }

    private CFileId selectMainDesignFileId(HttpServletRequest request, String sessionIdentifier) {
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        return UserdocUtils.selectUserdocMainObject(sessionUserdoc.getUserdocParentData());
    }
}
