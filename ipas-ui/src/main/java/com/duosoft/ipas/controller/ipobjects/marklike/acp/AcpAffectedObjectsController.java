package com.duosoft.ipas.controller.ipobjects.marklike.acp;

import bg.duosoft.ipas.core.model.acp.CAcpAffectedObject;
import bg.duosoft.ipas.core.model.acp.CAcpDetails;
import bg.duosoft.ipas.core.model.acp.CAcpExternalAffectedObject;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.search.AutocompleteIpoSearchParam;
import bg.duosoft.ipas.core.model.search.SearchPage;
import bg.duosoft.ipas.core.model.util.AutocompleteIpoSearchResult;
import bg.duosoft.ipas.core.model.util.TempID;
import bg.duosoft.ipas.core.service.mark.AcpAffectedObjectService;
import bg.duosoft.ipas.core.service.nomenclature.ExtFileTypeService;
import bg.duosoft.ipas.core.service.search.AutocompleteIpoSearchService;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.integration.tmview.model.TMViewAutocompleteResult;
import bg.duosoft.ipas.integration.tmview.service.TMViewService;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.general.BasicUtils;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.util.json.AcpAffectedObjectData;
import com.duosoft.ipas.util.json.AcpDetailsJson;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import org.apache.pdfbox.cos.COSObjectKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/acp/affected-objects")
public class AcpAffectedObjectsController {

    @Autowired
    private AutocompleteIpoSearchService autocompleteIpoSearchService;

    @Autowired
    private AcpAffectedObjectService acpAffectedObjectService;

    @Autowired
    private ExtFileTypeService extFileTypeService;

    @Autowired
    private TMViewService tmViewService;

    @Autowired
    private TempID tempID;

    @PostMapping("/edit")
    public String edit(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam String sessionIdentifier, @RequestParam(required = false) String data) {
        CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
        AcpDetailsJson formData = new AcpDetailsJson();
        if (Objects.nonNull(data)) {
            formData = JsonUtil.readJson(data, AcpDetailsJson.class);
        }
        if (!isCancel) {
//            Add affected objects list
            List<CAcpAffectedObject> affectedObjects = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ACP_AFFECTED_OBJECTS, sessionIdentifier, request);
            mark.setAcpAffectedObjects(affectedObjects);
//            Add text field "others"
            if (Objects.isNull(mark.getAcpDetails())) {
                mark.setAcpDetails(new CAcpDetails());
            }
            mark.getAcpDetails().setAffectedObjectOthers(formData.getOthers());
        }
        model.addAttribute("affectedObjectOthers", mark.getAcpDetails() != null ? mark.getAcpDetails().getAffectedObjectOthers() : null);
        model.addAttribute("mark", mark);
        model.addAttribute("affectedObjects", mark.getAcpAffectedObjects());
        model.addAttribute("extFileTypes", extFileTypeService.getFileTypesAsMapAndOrdered());
        MarkSessionUtils.removeAcpAffectedObjectsPanelSessionObjects(request, sessionIdentifier);
        return "ipobjects/marklike/acp/acp-affected-objects/affected_objects_main_panel :: main_panel";
    }


    @GetMapping(value = "/autocomplete-affected-object", produces = "application/json")
    @ResponseBody
    public List<AcpAffectedObjectData> autocompleteAffectedObject(@RequestParam(required = false) String fileNbr, @RequestParam(required = false) String registrationNbr, @RequestParam String fileType) {
        AutocompleteIpoSearchParam searchParam = new AutocompleteIpoSearchParam(SearchPage.create(0, 10000));

        String[] fileTypes = null;
        if (fileType.contains(DefaultValue.COMMA)) {
            fileTypes = fileType.split(DefaultValue.COMMA);
        }

        if (Objects.nonNull(fileTypes)) {
            for (int i = 0; i < fileTypes.length; i++) {
                searchParam.addFileType(fileTypes[i]);
            }
        } else {
            searchParam.addFileType(fileType);
        }

        searchParam.fileNbr(fileNbr).registrationNbr(registrationNbr);
        Page<AutocompleteIpoSearchResult> result = autocompleteIpoSearchService.search(searchParam);
        List<AcpAffectedObjectData> autocompleteList = result == null ? new ArrayList<>() : result.get().map(AcpAffectedObjectData::new).collect(Collectors.toList());
        return autocompleteList;
    }

    @GetMapping(value = "/tmview-autocomplete-affected-object", produces = "application/json")
    @ResponseBody
    public List<TMViewAutocompleteResult> tmviewAutocompleteAffectedObject(@RequestParam(required = false) String registrationNbr) {
        return tmViewService.getEuTrademarksAutocomplete(registrationNbr);
    }


    @PostMapping("/add-object")
    public String add(HttpServletRequest request, Model model, @RequestParam String filingNumber, @RequestParam String sessionIdentifier) {
        List<ValidationError> errors = new ArrayList<>();
        List<CAcpAffectedObject> affectedObjects = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ACP_AFFECTED_OBJECTS, sessionIdentifier, request);
        if (StringUtils.isEmpty(filingNumber)) {
            errors.add(ValidationError.builder().pointer("affected.objects.errors").messageCode("acp.affected.objects.empty.filing.number.error").build());
        }

        if (CollectionUtils.isEmpty(errors)) {
            CAcpAffectedObject affectedObjectById = acpAffectedObjectService.constructAffectedObjectByFileId(BasicUtils.createCFileId(filingNumber));
            CFileId cFileId = affectedObjects.stream().map(CAcpAffectedObject::getFileId).filter(Objects::nonNull).filter(e -> e.equals(affectedObjectById.getFileId())).findFirst().orElse(null);

            if (Objects.isNull(cFileId)) {
                setTemporaryAffectedObjectId(affectedObjectById, tempID);
                affectedObjects.add(affectedObjectById);
            }
        }

        model.addAttribute("validationErrors", errors);
        model.addAttribute("affectedObjects", affectedObjects);
        return "ipobjects/marklike/acp/acp-affected-objects/affected_objects_table :: table";
    }

    @PostMapping("/add-external-object")
    public String add(HttpServletRequest request, Model model, @RequestParam String registrationNumber, @RequestParam String name, @RequestParam String externalId, @RequestParam String sessionIdentifier) {
        List<ValidationError> errors = new ArrayList<>();
        List<CAcpAffectedObject> affectedObjects = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ACP_AFFECTED_OBJECTS, sessionIdentifier, request);
        if (StringUtils.isEmpty(registrationNumber)) {
            errors.add(ValidationError.builder().pointer("affected.objects.errors").messageCode("acp.affected.objects.empty.registration.number.error").build());
        }

        if (CollectionUtils.isEmpty(errors)) {
            CAcpAffectedObject cAcpAffectedObject = affectedObjects.stream().filter(e -> Objects.nonNull(e.getExternalAffectedObject())).filter(e -> e.getExternalAffectedObject().getExternalId().equals(externalId)).findFirst().orElse(null);
            if (Objects.isNull(cAcpAffectedObject)) {
                CAcpAffectedObject affectedObject = CAcpAffectedObject.builder().externalAffectedObject(CAcpExternalAffectedObject.builder().externalId(externalId).name(name).registrationNbr(registrationNumber).build()).build();
                setTemporaryAffectedObjectId(affectedObject, tempID);
                affectedObjects.add(affectedObject);
            }
        }

        model.addAttribute("validationErrors", errors);
        model.addAttribute("affectedObjects", affectedObjects);
        return "ipobjects/marklike/acp/acp-affected-objects/affected_objects_table :: table";
    }


    @PostMapping("/delete-object")
    public String delete(HttpServletRequest request, Model model, @RequestParam Integer affectedObjectId, @RequestParam String sessionIdentifier) {
        List<CAcpAffectedObject> affectedObjects = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ACP_AFFECTED_OBJECTS, sessionIdentifier, request);
        affectedObjects.removeIf(r -> r.getId().equals(affectedObjectId));
        model.addAttribute("affectedObjects", affectedObjects);
        return "ipobjects/marklike/acp/acp-affected-objects/affected_objects_table :: table";
    }

    private static void setTemporaryAffectedObjectId(CAcpAffectedObject affectedObject, TempID tempID) {
        //Set temporary person and address number! Negative values !
        Integer value = tempID.getValue();
        affectedObject.setId(value);
        tempID.setValue(--value);
    }
}
