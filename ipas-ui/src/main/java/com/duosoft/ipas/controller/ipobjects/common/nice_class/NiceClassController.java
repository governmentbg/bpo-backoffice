package com.duosoft.ipas.controller.ipobjects.common.nice_class;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.mark.CNiceClassList;
import bg.duosoft.ipas.core.model.mark.CTerm;
import bg.duosoft.ipas.core.model.miscellaneous.CommonTerm;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.util.LocalizationProperties;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.nomenclature.NiceListService;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.UserdocType;
import bg.duosoft.ipas.integration.tmclass.service.TMClassService;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.factory.LocalizationPropertiesFactory;
import com.duosoft.ipas.util.factory.NiceConfigFactory;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import com.duosoft.ipas.webmodel.NiceConfig;
import com.duosoft.ipas.webmodel.NiceListType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Controller
@RequestMapping("/nice-class/{niceListType}")
public class NiceClassController {

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private TMClassService tmClassService;

    @Autowired
    private MarkService markService;

    @Autowired
    private NiceListService niceListService;

    private CNiceClass findNiceClassInList(@RequestParam Integer niceClassNbr, List<CNiceClass> niceClassList) {
        return niceClassList.stream()
                .filter(cNiceClass -> Objects.equals(cNiceClass.getNiceClassNbr(), niceClassNbr))
                .findFirst().orElse(null);
    }

    @PostMapping("/open-modal")
    public String openModal(HttpServletRequest request, Model model,
                            @PathVariable NiceListType niceListType) {
        return selectNiceClassesModal(model, niceListType);
    }

    @PostMapping("/verify-panel")
    public String verifyNiceClassesPanel(HttpServletRequest request, Model model,
                                         @RequestParam boolean isEdit,
                                         @RequestParam(required = false) Boolean isAllNiceClassesIncluded,
                                         @RequestParam String sessionIdentifier,
                                         @PathVariable NiceListType niceListType) {
        List<CNiceClass> niceClassList = resolveViewNiceClasses(request, isEdit, sessionIdentifier, niceListType);
        LocalizationProperties localizationProperties = resolveLocalizationProperties(request, sessionIdentifier, niceListType);
        try {
            niceClassList.forEach(e -> verifyExistingClass(e, localizationProperties));
        } catch (Exception e) {
            model.addAttribute("tmClassError", true);
            log.error("Exception in nice-class/verify-panel", e);
        }
        return selectNiceClassesTable(model, request, sessionIdentifier, niceClassList, niceListType, isAllNiceClassesIncluded);
    }

    @PostMapping("/verify")
    public String verifyNiceClass(HttpServletRequest request, Model model,
                                  @RequestParam String sessionIdentifier,
                                  @RequestParam Integer niceClassNbr,
                                  @RequestParam boolean isEdit,
                                  @RequestParam(required = false) Boolean isAllNiceClassesIncluded,
                                  @PathVariable NiceListType niceListType) {

        List<CNiceClass> niceClassList = resolveViewNiceClasses(request, isEdit, sessionIdentifier, niceListType);
        CNiceClass existingClass = findNiceClassInList(niceClassNbr, niceClassList);
        LocalizationProperties localizationProperties = resolveLocalizationProperties(request, sessionIdentifier, niceListType);
        try {
            verifyExistingClass(existingClass, localizationProperties);
        } catch (Exception e) {
            model.addAttribute("tmClassError", true);
            log.error("Exception in verify-nice-class", e);
        }
        return selectNiceClassesTable(model, request, sessionIdentifier, niceClassList, niceListType, isAllNiceClassesIncluded);
    }

    private void verifyExistingClass(CNiceClass existingClass, LocalizationProperties localizationProperties) {
        if (existingClass != null) {
            CNiceClass validated = tmClassService.verifyClassTerms(localizationProperties,
                    existingClass.getNiceClassDescription(), existingClass.getNiceClassNbr() + "");

            if (validated != null && validated.getTerms() != null && validated.getTerms().size() > 0) {
                existingClass.setTerms(new ArrayList<>());
                existingClass.addTerms(null, validated.getTerms());
            }
        }
    }

    @PostMapping("/class-declaration-change")
    public String niceClassDeclarationChange(HttpServletRequest request, Model model,
                                             @RequestParam String sessionIdentifier,
                                             @RequestParam Integer niceClassNbr,
                                             @PathVariable NiceListType niceListType) {

        List<CNiceClass> niceClassList = getSessionNiceClasses(request, sessionIdentifier, niceListType);

        niceClassList.stream()
                .filter(cNiceClass -> Objects.equals(cNiceClass.getNiceClassNbr(), niceClassNbr))
                .findFirst().ifPresent(existingClass -> existingClass.setAllTermsDeclaration(
                        existingClass.getAllTermsDeclaration() == null || !existingClass.getAllTermsDeclaration()
                ));

        return selectNiceClassesTable(model, request, sessionIdentifier, niceClassList, niceListType);

    }

    @PostMapping("/fetch-class-terms")
    public String fetchClassTermsAndReplace(HttpServletRequest request, Model model,
                                            @RequestParam String sessionIdentifier,
                                            @RequestParam Integer niceClassNbr,
                                            @RequestParam String fetch,
                                            @PathVariable NiceListType niceListType) {

        List<CNiceClass> niceClassList = getSessionNiceClasses(request, sessionIdentifier, niceListType);
        CNiceClass existingClass = findNiceClassInList(niceClassNbr, niceClassList);
        LocalizationProperties localizationProperties = resolveLocalizationProperties(request, sessionIdentifier, niceListType);

        if (existingClass != null) {
            CNiceClass fetchedClass;
            switch (fetch) {
                case "alpha":
                    fetchedClass = getClassAlphaList(localizationProperties, niceClassNbr);
                    break;
                case "heading":
                    fetchedClass = getClassHeading(localizationProperties, niceClassNbr);
                    break;
                default:
                    throw new RuntimeException("Bad fetch type passed");
            }

            if (fetchedClass != null && fetchedClass.getTerms() != null &&
                    !fetchedClass.getTerms().isEmpty()) {
                existingClass.setNiceClassDescription(fetchedClass.getNiceClassDescription());
                existingClass.setTerms(fetchedClass.getTerms());
                setClassDeclaration(request, sessionIdentifier, existingClass, true);
            } else {
                log.warn("NO alpha list found for " + existingClass.getNiceClassNbr());
                model.addAttribute("fetchClassEmpty", true);
            }
        }
        return selectNiceClassesTable(model, request, sessionIdentifier, niceClassList, niceListType);
    }

    private void setClassDeclaration(HttpServletRequest request, String sessionIdentifier, CNiceClass niceClass,
                                     Boolean classDeclarationValue) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        if (sessionObjectType != SessionObjectType.USERDOC && niceClass != null) {
            CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
            String fileType = mark.getFile().getFileId().getFileType();
            if (!fileType.equalsIgnoreCase(FileType.INTERNATIONAL_MARK_R.code()) && !fileType.equalsIgnoreCase(FileType.INTERNATIONAL_MARK_I.code()) && !fileType.equalsIgnoreCase(FileType.INTERNATIONAL_MARK_B.code())) {
                niceClass.setAllTermsDeclaration(classDeclarationValue);
            }
        }
    }

    @PostMapping("/add-nice-classes")
    public String addNiceClasses(HttpServletRequest request, Model model,
                                 @RequestParam String sessionIdentifier,
                                 @RequestParam(required = false) String niceDescription,
                                 @RequestParam(required = false) Integer classNbr,
                                 @RequestParam boolean heading,
                                 @RequestParam boolean alpha,
                                 @PathVariable NiceListType niceListType) {
        if (validateNiceClassesForm(model, niceDescription, classNbr, heading, alpha))
            return selectNiceClassesModal(model, niceListType);

        List<CNiceClass> niceClassList = getSessionNiceClasses(request, sessionIdentifier, niceListType);
        CNiceClass classToAdd = null;
        LocalizationProperties localizationProperties = resolveLocalizationProperties(request, sessionIdentifier, niceListType);
        try {
            if (heading) {
                classToAdd = getClassHeading(localizationProperties, classNbr);
                setClassDeclaration(request, sessionIdentifier, classToAdd, true);
            } else if (alpha) {
                classToAdd = getClassAlphaList(localizationProperties, classNbr);
                setClassDeclaration(request, sessionIdentifier, classToAdd, true);
            } else {
                if (niceDescription == null || niceDescription.isEmpty()) {
                    classToAdd = null;
                } else {
                    classToAdd = new CNiceClass();
                    classToAdd.setNiceClassNbr(classNbr);
                    classToAdd.setNiceClassDescription(niceDescription);
                    classToAdd.generateInitialTermsFromDescription(CommonTerm.UNKNOWN);
                }
            }
        } catch (Exception e) {
            model.addAttribute("tmClassError", true);
            log.error("Exception in add-nice-classes", e);
        }

        if (classToAdd != null && classToAdd.getTerms() != null && classToAdd.getTerms().size() > 0) {
            CNiceClass existingClass = findNiceClassInList(classNbr, niceClassList);

            if (Objects.isNull(existingClass)) {
                setClassDefaults(classToAdd);
                niceClassList.add(classToAdd);
                Collections.sort(niceClassList, (n1, n2) -> n1.getNiceClassNbr() > n2.getNiceClassNbr() ? 1 : -1);
            } else {
                if (existingClass.getTerms() == null) {
                    existingClass.setTerms(new ArrayList<>());
                }
                existingClass.addTerms(null, classToAdd.getTerms());
                existingClass.duplicatedTermsIdentify();
            }
        }
        if (classToAdd == null) {
            return selectNiceClassesModalForNull(model, niceDescription, classNbr, heading, alpha, niceListType);
        }
        return selectNiceClassesTable(model, request, sessionIdentifier, niceClassList, niceListType);
    }

    private String selectNiceClassesModalForNull(Model model, String niceDescription, Integer classNbr, boolean heading, boolean alpha, NiceListType niceListType) {
        List<ValidationError> errors = new ArrayList<>();
        errors.add(ValidationError.builder().pointer("class.class.input").messageCode("no.terms.found").build());
        model.addAttribute("validationErrors", errors);
        model.addAttribute("classNbr", classNbr);
        model.addAttribute("heading", heading);
        model.addAttribute("alpha", alpha);
        model.addAttribute("niceDescription", niceDescription);
        return selectNiceClassesModal(model, niceListType);
    }

    private boolean validateNiceClassesForm(Model model, String niceDescription, Integer classNbr, boolean heading, boolean alpha) {
        List<ValidationError> errors = new ArrayList<>();
        if (Objects.isNull(classNbr)) {
            errors.add(ValidationError.builder().pointer("class.class.input").messageCode("required.field").build());
        } else {
            if (classNbr < yamlConfig.getNiceClassMin() || classNbr > yamlConfig.getNiceClassMax()) {
                errors.add(ValidationError.builder().pointer("class.class.input").messageCode("mark.nice.wrong.class").build());
            }
        }
        if (heading && alpha) {
            errors.add(ValidationError.builder().pointer("class.header.input").messageCode("heading.alpha.canNotSelectBoth").build());
        }

        if (!heading && !alpha && StringUtils.isEmpty(niceDescription)) {
            errors.add(ValidationError.builder().pointer("class.description.textarea").messageCode("required.field").build());
        }

        if (!CollectionUtils.isEmpty(errors)) {
            model.addAttribute("validationErrors", errors);
            model.addAttribute("classNbr", classNbr);
            model.addAttribute("heading", heading);
            model.addAttribute("alpha", alpha);
            model.addAttribute("niceDescription", niceDescription);
            return true;
        }

        return false;
    }

    private void setClassDefaults(CNiceClass niceClass) {
        niceClass.setNiceClassEdition(0); //TODO - what is the meaning of that?
        niceClass.setNiceClassDetailedStatus("R"); //TODO - what is the meaning of that?
        niceClass.setNiceClassVersion("0"); //TODO - what is the meaning of that?
    }

    @PostMapping("/delete-nice-class-term")
    public String deleteNiceClassTerm(HttpServletRequest request, Model model,
                                      @RequestParam String sessionIdentifier,
                                      @RequestParam Integer niceClassNbr,
                                      @RequestParam int termIndex,
                                      @PathVariable NiceListType niceListType) {

        List<CNiceClass> niceClassList = getSessionNiceClasses(request, sessionIdentifier, niceListType);
        if (!CollectionUtils.isEmpty(niceClassList)) {
            CNiceClass toBeDeletedFrom = findNiceClassInList(niceClassNbr, niceClassList);
            if (toBeDeletedFrom != null && toBeDeletedFrom.getTerms() != null) {
                toBeDeletedFrom.removeTerm(termIndex);
                toBeDeletedFrom.duplicatedTermsIdentify();

                if (toBeDeletedFrom.getTerms().size() == 0) {
                    niceClassList.remove(toBeDeletedFrom);
                }
            }
        }
        return selectNiceClassesTable(model, request, sessionIdentifier, niceClassList, niceListType);
    }

    @PostMapping("/edit-nice-class-term")
    public String editNiceClassTerm(HttpServletRequest request, Model model,
                                    @RequestParam String sessionIdentifier,
                                    @RequestParam Integer niceClassNbr,
                                    @RequestParam int termIndex,
                                    @PathVariable NiceListType niceListType) {

        List<CNiceClass> niceClassList = getSessionNiceClasses(request, sessionIdentifier, niceListType);
        if (!CollectionUtils.isEmpty(niceClassList)) {
            CNiceClass toBeEditedFrom = findNiceClassInList(niceClassNbr, niceClassList);
            if (toBeEditedFrom != null && toBeEditedFrom.getTerms() != null) {
                CTerm toBeEdited = toBeEditedFrom.getTerms().get(termIndex);
                toBeEditedFrom.removeTerm(termIndex);

                if (toBeEdited.getMatchedTerms() != null && toBeEdited.getMatchedTerms().size() > 0) {
                    Map<Integer, List<CTerm>> matchedMap = createMapFormMatchedTerms(toBeEdited);
                    matchedMap.forEach((matchedClass, matchedTerms) -> {
                        CNiceClass toBeAddedTo = findNiceClassInList(matchedClass, niceClassList);

                        if (toBeAddedTo == null) {
                            toBeAddedTo = createEmptyClass(matchedClass);
                            niceClassList.add(toBeAddedTo);
                            Collections.sort(niceClassList, (n1, n2) -> n1.getNiceClassNbr() > n2.getNiceClassNbr() ? 1 : -1);
                        }
                        if (toBeAddedTo.getNiceClassNbr().equals(toBeEditedFrom.getNiceClassNbr())) {
                            toBeAddedTo.addTerms(termIndex, matchedTerms);
                        } else {
                            toBeAddedTo.addTerms(null, matchedTerms);
                            toBeAddedTo.duplicatedTermsIdentify();
                        }
                    });
                }

                toBeEditedFrom.duplicatedTermsIdentify();
                if (toBeEditedFrom.getTerms().size() == 0) {
                    niceClassList.remove(toBeEditedFrom);
                }
            }
        }
        return selectNiceClassesTable(model, request, sessionIdentifier, niceClassList, niceListType);
    }

    private Map<Integer, List<CTerm>> createMapFormMatchedTerms(CTerm term) {
        Map<Integer, List<CTerm>> theMap = new HashMap<>();
        term.getMatchedTerms().stream().forEach(matched -> {
            if (theMap.containsKey(matched.getClassNumber())) {
                theMap.get(matched.getClassNumber()).add(matched);
            } else {
                List<CTerm> matchedList = new ArrayList<>();
                matchedList.add(matched);
                theMap.put(matched.getClassNumber(), matchedList);
            }
        });
        return theMap;
    }

    private CNiceClass createEmptyClass(Integer classNbr) {
        CNiceClass emptyClass = new CNiceClass();
        emptyClass.setNiceClassNbr(classNbr);
        setClassDefaults(emptyClass);
        return emptyClass;
    }

    @PostMapping("/delete-nice-classes")
    public String deleteNiceClasses(HttpServletRequest request, Model model,
                                    @RequestParam String sessionIdentifier,
                                    @RequestParam Integer niceClassNbr,
                                    @PathVariable NiceListType niceListType) {

        List<CNiceClass> niceClassList = getSessionNiceClasses(request, sessionIdentifier, niceListType);
        if (!CollectionUtils.isEmpty(niceClassList)) {
            CNiceClass toBeDeleted = findNiceClassInList(niceClassNbr, niceClassList);
            niceClassList.remove(toBeDeleted);
        }
        return selectNiceClassesTable(model, request, sessionIdentifier, niceClassList, niceListType);
    }

    @PostMapping("/update-recordal-nice-classes")
    public String updateRecordalNiceClasses(HttpServletRequest request, Model model,
                                            @RequestParam String sessionIdentifier,
                                            @RequestParam Boolean isAllNiceClassesIncluded,
                                            @PathVariable NiceListType niceListType) {
        List<CNiceClass> niceClassList = null;
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        if (!isAllNiceClassesIncluded) {
            niceClassList = getSessionNiceClasses(request, sessionIdentifier, niceListType);
            if (CollectionUtils.isEmpty(niceClassList)) {
                CFileId fileId = UserdocUtils.selectUserdocMainObject(sessionUserdoc.getUserdocParentData());
                niceClassList = NiceClassController.selectOriginalOrSessionNiceClasses(request, sessionIdentifier, true, markService, fileId, niceListType);
                setSessionNiceClassList(request, sessionIdentifier, niceClassList, niceListType);
            }
        } else if (UserdocType.MARK_INTERNATIONAL_REGISTRATION_REQUEST.equals(sessionUserdoc.getUserdocType().getUserdocType())) {
            CFileId fileId = UserdocUtils.selectUserdocMainObject(sessionUserdoc.getUserdocParentData());
            niceClassList = NiceClassController.selectOriginalOrSessionNiceClasses(request, sessionIdentifier, true, markService, fileId, niceListType);
            setSessionNiceClassList(request, sessionIdentifier, niceClassList, niceListType);
        }
        return selectNiceClassesTable(model, request, sessionIdentifier, niceClassList, niceListType, isAllNiceClassesIncluded);
    }

    @PostMapping("/update-mark-recordal-nice-classes")
    public String updateMarkRecordalNiceClasses(HttpServletRequest request, Model model,
                                                @RequestParam String sessionIdentifier,
                                                @RequestParam Boolean isAllNiceClassesIncluded,
                                                @PathVariable NiceListType niceListType) {
        List<CNiceClass> niceClassList = null;
        CMark sessionMark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
        if (!isAllNiceClassesIncluded) {
            niceClassList = getSessionNiceClasses(request, sessionIdentifier, niceListType);
            if (CollectionUtils.isEmpty(niceClassList)) {
                CFileId fileId = sessionMark.getFile().getFileId();
                niceClassList = NiceClassController.selectOriginalOrSessionNiceClasses(request, sessionIdentifier, true, markService, fileId, niceListType);
                setSessionNiceClassList(request, sessionIdentifier, niceClassList, niceListType);
            }
        }

        return selectNiceClassesTable(model, request, sessionIdentifier, niceClassList, niceListType, isAllNiceClassesIncluded);
    }

    @PostMapping("/restore-original-nice-classes")
    public String restoreOriginalNiceClasses(HttpServletRequest request, Model model,
                                             @RequestParam String sessionIdentifier,
                                             @PathVariable NiceListType niceListType) {

        CFileId fileId = getOriginalFileId(request, sessionIdentifier, niceListType);
        List<CNiceClass> niceClassList = NiceClassController.selectOriginalOrSessionNiceClasses(request, sessionIdentifier, true, markService, fileId, niceListType);
        setSessionNiceClassList(request, sessionIdentifier, niceClassList, niceListType);

        List<CNiceClass> sessionNiceClassList = getSessionNiceClasses(request, sessionIdentifier, niceListType);
        return selectNiceClassesTable(model, request, sessionIdentifier, sessionNiceClassList, niceListType);
    }

    @PostMapping("/restore-requested-nice-classes")
    public String restoreRequestedNiceClasses(HttpServletRequest request, Model model,
                                              @RequestParam String sessionIdentifier,
                                              @PathVariable NiceListType niceListType) {

        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        if (sessionUserdoc.getProtectionData() == null || CollectionUtils.isEmpty(sessionUserdoc.getProtectionData().getNiceClassList())) {
            model.addAttribute("emptyRequestedListWarn", true);
        } else {
            List<CNiceClass> niceClasses = new ArrayList<>();
            sessionUserdoc.getProtectionData().getNiceClassList().forEach(niceClass ->
                    niceClasses.add((CNiceClass) SerializationUtils.clone(niceClass)));
            setSessionNiceClassList(request, sessionIdentifier, niceClasses, niceListType);
        }

        List<CNiceClass> sessionNiceClassList = getSessionNiceClasses(request, sessionIdentifier, niceListType);
        return selectNiceClassesTable(model, request, sessionIdentifier, sessionNiceClassList, niceListType);
    }

    @PostMapping("/select-from-original-class")
    public String restoreFromOriginalClass(HttpServletRequest request, Model model,
                                           @RequestParam String sessionIdentifier,
                                           @RequestParam Integer niceClassNbr,
                                           @PathVariable NiceListType niceListType) {

        List<CNiceClass> currentSessionList = getSessionNiceClasses(request, sessionIdentifier, niceListType);
        CFileId fileId = getOriginalFileId(request, sessionIdentifier, niceListType);
        List<CNiceClass> niceClassList = NiceClassController.selectOriginalOrSessionNiceClasses(request, sessionIdentifier, true, markService, fileId, niceListType);
        CNiceClass cNiceClass = findNiceClassInList(niceClassNbr, niceClassList);
        return selectNiceClassTermsChooseModal(model, cNiceClass, niceClassNbr, niceListType, currentSessionList);
    }

    private CFileId getOriginalFileId(HttpServletRequest request, String sessionIdentifier, NiceListType niceListType) {
        switch (niceListType) {
            case OBJECT_INTL_REPLACEMENT_LIST:
                CMark sessionMark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
                return sessionMark.getFile().getFileId();
            default:
                CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
                return UserdocUtils.selectUserdocMainObject(sessionUserdoc.getUserdocParentData());

        }
    }

    @PostMapping("/select-from-requested-class")
    public String restoreFromRequestedClass(HttpServletRequest request, Model model,
                                            @RequestParam String sessionIdentifier,
                                            @RequestParam Integer niceClassNbr,
                                            @PathVariable NiceListType niceListType) {

        List<CNiceClass> currentSessionList = getSessionNiceClasses(request, sessionIdentifier, niceListType);
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        CNiceClass cNiceClass = null;
        if (sessionUserdoc.getProtectionData() == null || CollectionUtils.isEmpty(sessionUserdoc.getProtectionData().getNiceClassList())) {
            cNiceClass = new CNiceClass();
            cNiceClass.setNiceClassNbr(niceClassNbr);
        } else {
            cNiceClass = findNiceClassInList(niceClassNbr, sessionUserdoc.getProtectionData().getNiceClassList());
        }
        return selectNiceClassTermsChooseModal(model, cNiceClass, niceClassNbr, niceListType, currentSessionList);
    }

    @PostMapping("/save-terms-choice")
    public String saveTermsChoice(HttpServletRequest request, Model model,
                                  @RequestParam String sessionIdentifier,
                                  @RequestParam Integer niceClassNbr,
                                  @RequestParam String termsText,
                                  @RequestParam String action,
                                  @PathVariable NiceListType niceListType) {

        List<CNiceClass> niceClassList = getSessionNiceClasses(request, sessionIdentifier, niceListType);
        CNiceClass niceClass = findNiceClassInList(niceClassNbr, niceClassList);
        if (niceClass != null && !termsText.isEmpty()) {
            if (action.equalsIgnoreCase("add")) {
                CNiceClass temp = new CNiceClass();
                temp.setNiceClassNbr(niceClassNbr);
                temp.setNiceClassDescription(termsText);
                temp.generateInitialTermsFromDescription(CommonTerm.UNKNOWN);

                List<CTerm> termsToAdd = new ArrayList<>();
                temp.getTerms().forEach(tempTerm -> {
                    if (!niceClass.getTerms().contains(tempTerm)) {
                        termsToAdd.add(tempTerm);
                    }
                });
                niceClass.addTerms(null, termsToAdd);
            } else if (action.equalsIgnoreCase("replace")) {
                niceClass.setNiceClassDescription(termsText);
                niceClass.generateInitialTermsFromDescription(CommonTerm.UNKNOWN);
            }
        }

        return selectNiceClassesTable(model, request, sessionIdentifier, niceClassList, niceListType);
    }

    @PostMapping("/select-add-original-class")
    public String restoreFromOriginalClassesList(HttpServletRequest request, Model model,
                                                 @RequestParam String sessionIdentifier,
                                                 @PathVariable NiceListType niceListType) {

        List<CNiceClass> currentSessionList = getSessionNiceClasses(request, sessionIdentifier, niceListType);
        CFileId fileId = getOriginalFileId(request, sessionIdentifier, niceListType);
        List<CNiceClass> originalNiceList = NiceClassController.selectOriginalOrSessionNiceClasses(request, sessionIdentifier, true, markService, fileId, niceListType);

        Map<Integer, Integer> classesMap = new LinkedHashMap<>();
        originalNiceList.forEach(original -> {
            if (findNiceClassInList(original.getNiceClassNbr(), currentSessionList) == null) {
                classesMap.put(original.getNiceClassNbr(), original.getNiceClassNbr());
            }
        });

        return selectNiceClassClassChooseModal(model, classesMap, niceListType);
    }


    @PostMapping("/save-class-choice")
    public String saveClassChoice(HttpServletRequest request, Model model,
                                  @RequestParam String sessionIdentifier,
                                  @RequestParam Integer niceClassNbr,
                                  @PathVariable NiceListType niceListType) {
        List<CNiceClass> currentSessionList = getSessionNiceClasses(request, sessionIdentifier, niceListType);
        CFileId fileId = getOriginalFileId(request, sessionIdentifier, niceListType);
        List<CNiceClass> originalNiceList = NiceClassController.selectOriginalOrSessionNiceClasses(request, sessionIdentifier, true, markService, fileId, niceListType);
        CNiceClass toAdd = findNiceClassInList(niceClassNbr, originalNiceList);
        if (toAdd != null) {
            CNiceClass cloned = (CNiceClass) SerializationUtils.clone(toAdd);
            currentSessionList.add(cloned);
            Collections.sort(currentSessionList, (n1, n2) -> n1.getNiceClassNbr() > n2.getNiceClassNbr() ? 1 : -1);
        }

        return selectNiceClassesTable(model, request, sessionIdentifier, currentSessionList, niceListType);
    }

    @PostMapping("/delete-all-nice-classes")
    public String deleteAllClasses(HttpServletRequest request, Model model,
                                   @RequestParam String sessionIdentifier,
                                   @PathVariable NiceListType niceListType) {
        List<CNiceClass> currentSessionList = getSessionNiceClasses(request, sessionIdentifier, niceListType);
        currentSessionList.clear();
        return selectNiceClassesTable(model, request, sessionIdentifier, currentSessionList, niceListType);
    }

    private String selectNiceClassClassChooseModal(Model model, Map<Integer, Integer> classesMap, NiceListType niceListType) {
        model.addAttribute("niceClassesMap", classesMap.size() > 0 ? classesMap : null);
        model.addAttribute("niceListType", niceListType);
        return "ipobjects/common/nice_class/nice_classes_modal :: nice-class-class-choose";
    }


    private String selectNiceClassTermsChooseModal(Model model, CNiceClass cNiceClass, Integer niceClassNbr, NiceListType niceListType, List<CNiceClass> currentSessionList) {
        if (cNiceClass == null) {
            cNiceClass = new CNiceClass();
            cNiceClass.setNiceClassNbr(niceClassNbr);
        }
        CNiceClass existingSessionClass = findNiceClassInList(niceClassNbr, currentSessionList);
        model.addAttribute("niceClass", cNiceClass);
        model.addAttribute("currentSessionTermsList", existingSessionClass != null && existingSessionClass.getTerms() != null ?
                existingSessionClass.getTerms() : new ArrayList<>());
        model.addAttribute("niceListType", niceListType);
        return "ipobjects/common/nice_class/nice_classes_modal :: nice-class-terms-choose";
    }

    private String selectNiceClassesTable(Model model, HttpServletRequest request, String sessionIdentifier, List<CNiceClass> niceClassList, NiceListType niceListType, Boolean isAllNiceClassesIncluded) {
        NiceConfig niceConfig = getNiceConfig(request, sessionIdentifier, niceListType);
        model.addAttribute("allowEdit", niceConfig.allowEdit(isAllNiceClassesIncluded));
        model.addAttribute("niceConfig", niceConfig);
        model.addAttribute("niceClassList", niceClassList);
        return "ipobjects/common/nice_class/nice_classes_table :: nice-classes-table";
    }

    private NiceConfig getNiceConfig(HttpServletRequest request, String sessionIdentifier, NiceListType niceListType) {
        String fileType = null;
        String userdocType = null;
        CUserdoc userdoc;
        switch (niceListType) {
            case OBJECT_LIST:
            case OBJECT_INTL_REPLACEMENT_LIST:
                CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
                fileType = mark.getFile().getFileId().getFileType();
                break;
            case USERDOC_APPROVED:
            case USERDOC_REQUESTED:
                userdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
                userdocType = userdoc.getUserdocType().getUserdocType();
                break;
        }

        return NiceConfigFactory.get(niceListType, fileType, userdocType);
    }

    private String selectNiceClassesTable(Model model, HttpServletRequest request, String sessionIdentifier, List<CNiceClass> niceClassList, NiceListType niceListType) {
        return selectNiceClassesTable(model, request, sessionIdentifier, niceClassList, niceListType, null);
    }

    private String selectNiceClassesModal(Model model, NiceListType niceListType) {
        model.addAttribute("niceListType", niceListType);
        return "ipobjects/common/nice_class/nice_classes_modal :: nice-classes";
    }

    private List<CNiceClass> resolveViewNiceClasses(HttpServletRequest request, boolean isEdit, String sessionIdentifier, NiceListType niceListType) {
        List<CNiceClass> niceClassList;
        if (isEdit) {
            niceClassList = getSessionNiceClasses(request, sessionIdentifier, niceListType);
        } else {
            niceClassList = getOriginalNiceClasses(request, sessionIdentifier, niceListType);
        }
        return niceClassList;
    }

    private LocalizationProperties resolveLocalizationProperties(HttpServletRequest request, String sessionIdentifier,
                                                                 NiceListType niceListType) {
        String fileType = null;
        switch (niceListType) {
            case OBJECT_LIST:
            case OBJECT_INTL_REPLACEMENT_LIST:
                CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
                fileType = mark.getFile().getFileId().getFileType();
                break;
            case USERDOC_APPROVED:
            case USERDOC_REQUESTED:
                CUserdoc userdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
                fileType = userdoc.getUserdocParentData().getFileId().getFileType();
                break;
        }

        return LocalizationPropertiesFactory.get(fileType);

    }

    private List<CNiceClass> getOriginalNiceClasses(HttpServletRequest request, String sessionIdentifier, NiceListType niceListType) {
        switch (niceListType) {
            case OBJECT_LIST:
                return MarkSessionUtils.getSessionMark(request, sessionIdentifier).getProtectionData().getNiceClassList();
            case OBJECT_INTL_REPLACEMENT_LIST:
                return MarkSessionUtils.getSessionMark(request, sessionIdentifier).getMarkInternationalReplacement().getReplacementNiceClasses();
            case USERDOC_REQUESTED:
                return UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier).getProtectionData().getNiceClassList();
            case USERDOC_APPROVED:
                return UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier).getApprovedNiceClassList();
            default:
                throw new RuntimeException("Cannot find original nice classes for session identifier " + sessionIdentifier);
        }
    }

    private List<CNiceClass> getSessionNiceClasses(HttpServletRequest request, String sessionIdentifier, NiceListType niceListType) {
        switch (niceListType) {
            case OBJECT_LIST:
                return HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_NICE_CLASSES, sessionIdentifier, request);
            case OBJECT_INTL_REPLACEMENT_LIST:
                return HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_INTL_REPLACEMENT_NICE_CLASSES, sessionIdentifier, request);
            case USERDOC_REQUESTED:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_NICE_CLASSES, sessionIdentifier, request);
            case USERDOC_APPROVED:
                return HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_APPROVED_NICE_CLASSES, sessionIdentifier, request);
            default:
                throw new RuntimeException("Cannot find original nice classes for session identifier " + sessionIdentifier);
        }
    }

    public static List<CNiceClass> selectOriginalOrSessionNiceClasses(HttpServletRequest request, String sessionIdentifier, Boolean isAllNiceClassesIncluded, MarkService markService, CFileId markId, NiceListType niceListType) {
        List<CNiceClass> niceClassList;
        if (Objects.nonNull(isAllNiceClassesIncluded) && isAllNiceClassesIncluded) {
            CMark mark = markService.findMark(markId, false);
            if (Objects.isNull(mark)) {
                throw new RuntimeException("Cannot find mark for nice classes " + markId.toString());
            }
            niceClassList = mark.getProtectionData().getNiceClassList();
            niceClassList.forEach(niceClass -> niceClass.setAllTermsDeclaration(null));
        } else {
            switch (niceListType) {
                case USERDOC_REQUESTED:
                    niceClassList = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_NICE_CLASSES, sessionIdentifier, request);
                    break;
                case USERDOC_APPROVED:
                    niceClassList = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_APPROVED_NICE_CLASSES, sessionIdentifier, request);
                    break;
                case OBJECT_INTL_REPLACEMENT_LIST:
                    niceClassList = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_INTL_REPLACEMENT_NICE_CLASSES, sessionIdentifier, request);
                    break;
                default:
                    throw
                            new RuntimeException("selectOriginalOrSessionNiceClasses can not return session nice list");
            }

        }
        return niceClassList;
    }


    @GetMapping("/edit-terms-text")
    public String editTermsAsText(HttpServletRequest request, Model model,
                                  @RequestParam Integer niceClassNbr,
                                  @RequestParam boolean isEdit,
                                  @RequestParam(required = false) Boolean isAllNiceClassesIncluded,
                                  @RequestParam String sessionIdentifier,
                                  @PathVariable NiceListType niceListType) {
        List<CNiceClass> niceClassList = resolveViewNiceClasses(request, isEdit, sessionIdentifier, niceListType);
        CNiceClass existingClass = findNiceClassInList(niceClassNbr, niceClassList);

        if (existingClass != null) {
            model.addAttribute("niceClassDescription", existingClass.getNiceClassDescription());
            model.addAttribute("niceClassNbr", existingClass.getNiceClassNbr());
        } else {
            throw new RuntimeException("Cannot find nice class " + niceClassNbr + " for session identifier " + sessionIdentifier);
        }

        NiceConfig niceConfig = getNiceConfig(request, sessionIdentifier, niceListType);
        model.addAttribute("isEdit", isEdit && niceConfig.allowEdit(isAllNiceClassesIncluded));
        return selectNiceClassTermsTextModal(model, niceListType);
    }

    @PostMapping("/edit-terms-tolower")
    public String editTermsToLowerCase(HttpServletRequest request, Model model,
                                       @RequestParam(required = false) Integer niceClassNbr,
                                       @RequestParam String sessionIdentifier,
                                       @PathVariable NiceListType niceListType) {
        List<CNiceClass> niceClassList = resolveViewNiceClasses(request, true, sessionIdentifier, niceListType);
        if (niceClassNbr != null) {
            CNiceClass existingClass = findNiceClassInList(niceClassNbr, niceClassList);
            if (existingClass != null) {
                existingClass.allTermsToLowercase();
            } else {
                throw new RuntimeException("Cannot find nice class " + niceClassNbr + " for session identifier " + sessionIdentifier);
            }
        } else {
            niceClassList.stream().forEach(existingClass -> existingClass.allTermsToLowercase());
        }
        return selectNiceClassesTable(model, request, sessionIdentifier, niceClassList, niceListType);
    }


    @PostMapping("/save-terms-text")
    public String saveTermsAsText(HttpServletRequest request, Model model,
                                  @RequestParam Integer niceClassNbr,
                                  @RequestParam String termsText,
                                  @RequestParam String sessionIdentifier,
                                  @PathVariable NiceListType niceListType) {

        if (validateNiceClassTermTextForm(model, termsText, niceClassNbr)) {
            return selectNiceClassTermsTextModal(model, niceListType);
        }

        List<CNiceClass> niceClassList = resolveViewNiceClasses(request, true, sessionIdentifier, niceListType);
        CNiceClass existingClass = findNiceClassInList(niceClassNbr, niceClassList);

        if (existingClass != null) {
            existingClass.setNiceClassDescription(termsText);
            existingClass.generateInitialTermsFromDescription(CommonTerm.UNKNOWN);
        } else {
            throw new RuntimeException("Cannot find nice class " + niceClassNbr + " for session identifier " + sessionIdentifier);
        }

        return selectNiceClassesTable(model, request, sessionIdentifier, niceClassList, niceListType);
    }

    private boolean validateNiceClassTermTextForm(Model model, String niceDescription, Integer classNbr) {
        List<ValidationError> errors = new ArrayList<>();
        if (StringUtils.isEmpty(niceDescription)) {
            errors.add(ValidationError.builder().pointer("class.terms.description.textarea").messageCode("required.field").build());
        }

        if (!CollectionUtils.isEmpty(errors)) {
            model.addAttribute("validationErrors", errors);
            model.addAttribute("niceClassDescription", niceDescription);
            model.addAttribute("niceClassNbr", classNbr);
            model.addAttribute("isEdit", true);
            return true;
        }
        return false;
    }

    private String selectNiceClassTermsTextModal(Model model, NiceListType niceListType) {
        model.addAttribute("niceListType", niceListType);
        return "ipobjects/common/nice_class/nice_classes_modal :: nice-class-terms-text";
    }

    @PostMapping("/replace-term-text")
    public String replaceTermText(HttpServletRequest request, Model model,
                                  @RequestParam Integer niceClassNbr,
                                  @RequestParam int termIndex,
                                  @RequestParam String newTermText,
                                  @RequestParam boolean isEdit,
                                  @RequestParam String sessionIdentifier,
                                  @PathVariable NiceListType niceListType) {

        List<CNiceClass> niceClassList = resolveViewNiceClasses(request, isEdit, sessionIdentifier, niceListType);
        if (!StringUtils.isEmpty(newTermText) && isEdit) {
            CNiceClass existingClass = findNiceClassInList(niceClassNbr, niceClassList);
            if (existingClass != null) {
                CNiceClass tempClass = new CNiceClass();
                tempClass.setNiceClassDescription(newTermText);
                tempClass.generateInitialTermsFromDescription(CommonTerm.UNKNOWN);

                if (tempClass.getTerms() != null && tempClass.getTerms().size() > 0) {
                    existingClass.getTerms().remove(termIndex);
                    existingClass.addTerms(termIndex, tempClass.getTerms());
                    existingClass.duplicatedTermsIdentify();
                }
            }
        }

        return selectNiceClassesTable(model, request, sessionIdentifier, niceClassList, niceListType);
    }

    private void setSessionNiceClassList(HttpServletRequest request, String sessionIdentifier, List<CNiceClass> niceClassList, NiceListType niceListType) {
        switch (niceListType) {
            case USERDOC_REQUESTED:
                HttpSessionUtils.setSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_NICE_CLASSES, sessionIdentifier, niceClassList, request);
                break;
            case USERDOC_APPROVED:
                HttpSessionUtils.setSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_APPROVED_NICE_CLASSES, sessionIdentifier, niceClassList, request);
                break;
            case OBJECT_INTL_REPLACEMENT_LIST:
                HttpSessionUtils.setSessionAttribute(MarkSessionObjects.SESSION_MARK_INTL_REPLACEMENT_NICE_CLASSES, sessionIdentifier, niceClassList, request);
        }
    }

    private CNiceClass getClassAlphaList(LocalizationProperties localizationProperties, Integer niceClassNbr) {
        if (localizationProperties.getLanguage().equalsIgnoreCase(LocalizationProperties.ENGLISH_LANG)) {
            return tmClassService.getClassAlphaList(localizationProperties.getLanguage(), niceClassNbr.toString(), null);
        } else {
            CNiceClassList niceClassDBList = niceListService.getNiceList(niceClassNbr);
            if (niceClassDBList != null) {
                CNiceClass cNiceClass = createEmptyClass(niceClassNbr);
                cNiceClass.setNiceClassDescription(niceClassDBList.getAlphaList());
                cNiceClass.generateInitialTermsFromDescription(CommonTerm.UNKNOWN);
                return cNiceClass;
            }
        }
        return null;
    }

    private CNiceClass getClassHeading(LocalizationProperties localizationProperties, Integer niceClassNbr) {
        if (localizationProperties.getLanguage().equalsIgnoreCase(LocalizationProperties.ENGLISH_LANG)) {
            return tmClassService.getClassHeading(localizationProperties.getLanguage(), niceClassNbr.toString());
        } else {
            CNiceClassList niceClassDBList = niceListService.getNiceList(niceClassNbr);
            if (niceClassDBList != null) {
                CNiceClass cNiceClass = createEmptyClass(niceClassNbr);
                cNiceClass.setNiceClassDescription(niceClassDBList.getHeading());
                cNiceClass.generateInitialTermsFromDescription(CommonTerm.UNKNOWN);
                return cNiceClass;
            }
        }
        return null;
    }

    @PostMapping("/translate-nice")
    public String translateNiceClassesPanel(HttpServletRequest request, Model model,
                                            @RequestParam String sessionIdentifier,
                                            @PathVariable NiceListType niceListType,
                                            @RequestParam(required = false) Integer niceClassNbr) {
        List<CNiceClass> niceClassList = getSessionNiceClasses(request, sessionIdentifier, niceListType);
        LocalizationProperties localizationProperties = resolveLocalizationProperties(request, sessionIdentifier, niceListType);
        try {
            List<CNiceClass> translatedClasses;
            if (niceClassNbr == null) {
                translatedClasses =
                        niceClassList.stream().map(e -> translateExistingClass(e, localizationProperties))
                                .filter(e -> e != null)
                                .collect(Collectors.toList());
            } else {
                CNiceClass niceClass = findNiceClassInList(niceClassNbr, niceClassList);
                translatedClasses = new ArrayList<>();
                if (niceClass != null) {
                    translatedClasses.add(translateExistingClass(niceClass, localizationProperties));
                }
            }
            model.addAttribute("translatedClasses", translatedClasses);
        } catch (Exception e) {
            model.addAttribute("tmClassError", true);
            log.error("Exception in nice-class/translate-nice", e);
        }
        return "ipobjects/common/nice_class/translated_nice_classes_modal :: translated_nice_classes_list";
    }

    private CNiceClass translateExistingClass(CNiceClass existingClass, LocalizationProperties localizationProperties) {
        if (existingClass != null) {
            CNiceClass translated = tmClassService.translateClassTerms(localizationProperties, "en",
                    existingClass.getNiceClassDescription(), existingClass.getNiceClassNbr() + "");

            if (translated != null && translated.getTerms() != null && translated.getTerms().size() > 0) {
                return translated;
            }
        }
        return null;
    }
}
