package com.duosoft.ipas.controller.ipobjects.common;

import bg.duosoft.ipas.core.model.file.*;
import bg.duosoft.ipas.core.model.search.CSearchParam;
import bg.duosoft.ipas.core.model.search.CSearchResult;
import bg.duosoft.ipas.core.service.nomenclature.CountryService;
import bg.duosoft.ipas.core.service.search.IpoSearchService;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.ipobject.PriorityValidator;
import bg.duosoft.ipas.util.DataConverter;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.default_value.DefaultValueUtils;
import bg.duosoft.ipas.core.model.search.SearchPage;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.util.CFileRelationshipUtils;
import com.duosoft.ipas.util.json.ExhibitionData;
import com.duosoft.ipas.util.json.PriorityData;
import com.duosoft.ipas.util.json.RelationshipData;
import com.duosoft.ipas.util.json.RightsData;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static bg.duosoft.ipas.enums.RelationshipDirection.RELATIONSHIP_CONTAINS_SUPER_OBJECTS_ROLE;

@Controller
public class RightsBaseController {
    @Autowired
    private DefaultValueUtils defaultValueUtils;
    @Autowired
    private IpoSearchService ipoSearchService;
    protected CRelationship updateRelationshipData(RelationshipData relationshipData, CFile file, String relationshipType) {
        CRelationship cRelationship = null;
        if (relationshipData != null) {
            if (file.getRelationshipList() != null && CFileRelationshipUtils.isContainsSuperObjects(file.getRelationshipList(), relationshipType)) {
                cRelationship = CFileRelationshipUtils.findSuperObject(file.getRelationshipList(), relationshipType);
            } else {
                cRelationship = new CRelationship();
                cRelationship.setRelationshipRole(RELATIONSHIP_CONTAINS_SUPER_OBJECTS_ROLE);
                cRelationship.setRelationshipType(relationshipType);
                if (file.getRelationshipList() == null) {
                    file.setRelationshipList(new ArrayList<>());
                }
                file.getRelationshipList().add(cRelationship);
            }
            cRelationship.setFileId(new CFileId());
            cRelationship.getFileId().setFileNbr(relationshipData.getFileNbr());
            cRelationship.getFileId().setFileSeq(relationshipData.getFileSeq());
            cRelationship.getFileId().setFileSeries(relationshipData.getFileSeries());
            cRelationship.getFileId().setFileType(relationshipData.getFileType());

        } else {
            CFileRelationshipUtils.removeSuperObjectRelationshipType(file, relationshipType);
        }
        return cRelationship;
    }

    protected void updateExhibitionData(RightsData rightsData, CFile file) {
        if (rightsData.getExhibitionData() != null && rightsData.getExhibitionData().isHasExhibitionData()) {
            ExhibitionData exhibitionData = rightsData.getExhibitionData();
            if (file.getPriorityData() == null) {
                file.setPriorityData(new CPriorityData());
            }
            file.getPriorityData().setExhibitionDate(exhibitionData.getExhibitionDate());
            file.getPriorityData().setExhibitionNotes(exhibitionData.getExhibitionNotes());
        } else {
            if (file.getPriorityData() != null) {
                file.getPriorityData().setExhibitionNotes(null);
                file.getPriorityData().setExhibitionDate(null);
            }
        }
        ExhibitionData exhibitionData = rightsData.getExhibitionData();
        if (Objects.isNull(exhibitionData)) {
            file.getPriorityData().setHasExhibitionData(false);
        } else {
            file.getPriorityData().setHasExhibitionData(exhibitionData.isHasExhibitionData());
        }

    }


    protected void setSessionPrioritiesToObject(boolean hasPriority, CFile file, HttpServletRequest request, String sessionIdentifier) {
        if (file.getPriorityData() == null) {
            file.setPriorityData(new CPriorityData());
        }
        file.getPriorityData().setHasParisPriorityData(hasPriority);

        if (!hasPriority) {
            file.getPriorityData().setParisPriorityList(new ArrayList<>());
        } else {
            String fullSessionIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
            List<CParisPriority> priorities = new ArrayList<>();
            String sessionObject = null;
            if (SessionObjectUtils.isSessionObjectMark(fullSessionIdentifier)) {
                priorities = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_PRIORITIES, sessionIdentifier, request);
                sessionObject = MarkSessionObjects.SESSION_MARK_PRIORITIES;
            }
            if (SessionObjectUtils.isSessionObjectPatent(fullSessionIdentifier)) {
                priorities = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_PRIORITIES, sessionIdentifier, request);
                sessionObject = PatentSessionObject.SESSION_PATENT_PRIORITIES;
            }

            file.getPriorityData().setParisPriorityList(priorities);

            HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, sessionObject);
        }
    }

    protected void openPriorityModal(HttpServletRequest request, Model model, String sessionIdentifier, Integer index, CountryService countryService, CFile cFile) {
        CParisPriority cParisPriority = new CParisPriority();
        if (index != null) {
            List<CParisPriority> priorities = fillPriority(request, sessionIdentifier);
            if (!CollectionUtils.isEmpty(priorities))
                cParisPriority = priorities.get(index.intValue());
            model.addAttribute("index", index);
        }

        model.addAttribute("fileId", cFile.getFileId());
        model.addAttribute("priority", cParisPriority);
        model.addAttribute("countryMap", countryService.getCountryMap());
    }


    protected void deletePriorityBase(HttpServletRequest request, Model model, String sessionIdentifier,
                                      Integer index, CFile cFile) {
        List<CParisPriority> priorities = fillPriority(request, sessionIdentifier);
        priorities.remove(index.intValue());
        model.addAttribute("priorities", priorities);
        model.addAttribute("fileId", cFile.getFileId());
    }


    protected String savePriorityAndReturnPage(HttpServletRequest request, Model model, String sessionIdentifier, CFile file, String data,
                                               Integer index, IpasValidatorCreator validatorCreator, CountryService countryService) {
        PriorityData priorityData = JsonUtil.readJson(data, PriorityData.class);
        List<CParisPriority> priorities = fillPriority(request, sessionIdentifier);

        if (Objects.nonNull(priorityData)) {
            CParisPriority cPriority = priorityData.convertToCParisPriority();


            IpasTwoArgsValidator<CParisPriority, CFile> validator = validatorCreator.createTwoArgsValidator(true, PriorityValidator.class);
            List<ValidationError> errors = validator.validate(cPriority, file);
            if (CollectionUtils.isEmpty(errors)) {
                if (index != null) {
                    priorities.remove(index.intValue());
                    priorities.add(index, cPriority);
                } else {
                    priorities.add(cPriority);
                }
            } else {
                model.addAttribute("fileId", file.getFileId());
                model.addAttribute("priority", cPriority);
                model.addAttribute("validationErrors", errors);
                model.addAttribute("countryMap", countryService.getCountryMap());
                return "ipobjects/common/priority/priority_modal :: priority-form ";
            }
        }
        model.addAttribute("fileId", file.getFileId());
        model.addAttribute("priorities", priorities);
        return "ipobjects/common/priority/priority_table :: priorities-table";

    }

    private List<CParisPriority> fillPriority(HttpServletRequest request, String sessionIdentifier) {
        String fullSessionIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
        List<CParisPriority> priorities = new ArrayList<>();
        if (SessionObjectUtils.isSessionObjectMark(fullSessionIdentifier)) {
            priorities = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_PRIORITIES, sessionIdentifier, request);
        }
        if (SessionObjectUtils.isSessionObjectPatent(fullSessionIdentifier)) {
            priorities = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_PRIORITIES, sessionIdentifier, request);
        }
        return priorities;
    }

    @GetMapping(value = "/autocomplete-relationship", produces = "application/json")
    @ResponseBody
    public List<RelationshipData> autocompleteRelationshipObject(@RequestParam String fileNbr, @RequestParam String masterFileType) {
        Page<CSearchResult> result = DataConverter.parseInteger(fileNbr, null)  == null ? null : ipoSearchService.search(new CSearchParam(SearchPage.create(0, 10000)).addFileType(masterFileType).fromFileNbr(fileNbr).toFileNbr(fileNbr));
        List<RelationshipData> autocompleteList = result == null ? new ArrayList<>() : result.get().map(RelationshipData::new).collect(Collectors.toList());
        autocompleteList.sort(Comparator.comparing(RelationshipData::getFileSeries).reversed());
        return autocompleteList;
    }

    protected  void recalculateEntitlementAndExpirationDate(CFile file, Date applicationDate){
        try {
            Date dateFromString =null;
            if (!Objects.isNull(applicationDate)){
                String dateAsString = DateUtils.DATE_TIME_FORMATTER.format(applicationDate);
                dateFromString = DateUtils.DATE_TIME_FORMATTER.parse(dateAsString);
            }
            file.getRegistrationData().setEntitlementDate(dateFromString);
            file.getRegistrationData().setExpirationDate(defaultValueUtils.getExpirationDatePlusRegYear(dateFromString,file));
        } catch (ParseException e) {
            throw new RuntimeException("Invalid applicationDate: " + applicationDate);
        }
    }
}
