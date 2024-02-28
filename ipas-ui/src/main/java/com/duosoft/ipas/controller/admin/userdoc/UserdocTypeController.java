package com.duosoft.ipas.controller.admin.userdoc;

import bg.duosoft.abdocs.model.DocumentTypeDto;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.miscellaneous.CAbdocsDocumentType;
import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.reception.CUserdocReceptionRelation;
import bg.duosoft.ipas.core.model.structure.OfficeDepartment;
import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.model.userdoc.config.CUserdocTypeConfig;
import bg.duosoft.ipas.core.model.userdoc.config.CUserdocTypeDepartment;
import bg.duosoft.ipas.core.service.nomenclature.ProcessTypeService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.reception.AbdocsDocumentTypeService;
import bg.duosoft.ipas.core.service.reception.UserdocReceptionRelationService;
import bg.duosoft.ipas.core.service.structure.OfficeDepartmentService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.core.service.userdoc.UserdocPanelService;
import bg.duosoft.ipas.core.service.userdoc.UserdocPersonRoleService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.userdoc.ChangeUserdocAbdocsDocumentValidator;
import bg.duosoft.ipas.core.validation.userdoc.UpdateUserdocTypeValidator;
import bg.duosoft.ipas.enums.IpasObjectType;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.filter.UserdocTypesFilter;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.util.json.UserdocTypeConfigurationData;
import com.duosoft.ipas.util.json.UserdocTypeMainData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/userdoc-types")
public class UserdocTypeController {

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Autowired
    private ProcessTypeService processTypeService;

    @Autowired
    private UserdocPanelService userdocPanelService;

    @Autowired
    private UserdocPersonRoleService userdocPersonRoleService;

    @Autowired
    private UserdocReceptionRelationService userdocReceptionRelationService;

    @Autowired
    private AbdocsService abdocsService;

    @Autowired
    private AbdocsDocumentTypeService abdocsDocumentTypeService;

    @Autowired
    private IpasValidatorCreator validatorCreator;

    @Autowired
    private UserService userService;

    @Autowired
    private OfficeDepartmentService officeDepartmentService;

    @Autowired
    protected MessageSource messageSource;

    @GetMapping(value = "/list")
    public String getUserdocTypesList(Model model, UserdocTypesFilter filter) {
        fillUserdocTypesAttributes(model, filter, null);
        return "admin/userdoc/userdoc_administration/userdoc_types";
    }

    @PostMapping(value = "/search")
    public String search(Model model, UserdocTypesFilter filter) {
        fillUserdocTypesAttributes(model, filter, null);
        return "admin/userdoc/userdoc_administration/userdoc_types_table :: table";
    }

    @PostMapping(value = "/update-table")
    public String updateUserdocTypesTable(Model model, @RequestParam Integer tableCount, UserdocTypesFilter filter) {
        fillUserdocTypesAttributes(model, filter, tableCount);
        return "admin/userdoc/userdoc_administration/userdoc_types_table :: table";
    }

    @GetMapping(value = "/edit/{id}")
    public String editUserdocType(@PathVariable("id") String id, UserdocTypesFilter filter, Model model) {
        CUserdocType userdocType = userdocTypesService.selectUserdocTypeById(id);
        fillModelAttributesForMainPanel(model, userdocType);
        fillModelAttributesForInvalidatedUserdocsPanel(model, userdocType);
        fillModelAttributesForDetailPagePanelsPanel(model, userdocType);
        fillModelAttributesForPersonRolesPanel(model, userdocType);
        fillModelAttributesForReceptionRelationPanel(model, userdocType.getUserdocType());
        fillModelAttributesForAbdocsDocumentsPanel(model, userdocType.getUserdocType());
        fillModelAttributesForUserdocTypeConfigurationsPanel(model, userdocType);
        model.addAttribute("userdocTypesFilter", filter);
        return "admin/userdoc/userdoc_administration/userdoc_type_edit";
    }


    @GetMapping(value = "/invalidate-userdoc-type-autocomplete")
    @ResponseBody
    public List<CUserdocType> invalidateUserdocTypeAutocomplete(@RequestParam String invalidateType, @RequestParam String userdocType) {
        return userdocTypesService.selectAutocompleteUserdocTypesForInvalidation(invalidateType, userdocType);
    }

    @PostMapping(value = "/add-invalidated-userdoc-type")
    public String addInvalidatedUserdocType(@RequestParam("userdocType") String userdocType, @RequestParam("invalidateType") String invalidateType, Model model) {
        try {
            CUserdocType cUserdocType = userdocTypesService.addInvalidatedUserdocType(userdocType, invalidateType);
            return generateSuccessResponseInvalidationPanel(model, cUserdocType);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @PostMapping(value = "/delete-invalidated-userdoc-type")
    public String deleteInvalidatedUserdocType(@RequestParam("userdocType") String userdocType, @RequestParam("invalidateType") String invalidateType, Model model) {
        try {
            CUserdocType cUserdocType = userdocTypesService.deleteInvalidatedUserdocType(userdocType, invalidateType);
            return generateSuccessResponseInvalidationPanel(model, cUserdocType);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @PostMapping("/save-main-panel-data")
    public String editMainPanel(@RequestParam(required = false) String data, Model model) {
        UserdocTypeMainData mainData = JsonUtil.readJson(data, UserdocTypeMainData.class);
        CUserdocType cUserdocType = setMainPanelChanges(mainData);
        IpasValidator<CUserdocType> validator = validatorCreator.create(false, UpdateUserdocTypeValidator.class);
        List<ValidationError> errors = validator.validate(cUserdocType);

        if (CollectionUtils.isEmpty(errors)) {
            CUserdocType userdocType = userdocTypesService.saveUserdocType(cUserdocType);
            model.addAttribute("successMessage", messageSource.getMessage("edit.success", null, LocaleContextHolder.getLocale()));
            fillModelAttributesForMainPanel(model, userdocType);
            return "admin/userdoc/userdoc_administration/userdoc_type_edit_panels:: main-userdoc-type-data";
        } else {
            model.addAttribute("validationErrors", errors);
            return "base/modal/validation_errors::errors";
        }
    }

    @PostMapping("/save-configuration-data")
    public String saveUserdocTypeConfigurationData(@RequestParam(required = false) String data, Model model) {
        UserdocTypeConfigurationData configurationData = JsonUtil.readJson(data, UserdocTypeConfigurationData.class);
        CUserdocType cUserdocType = setConfigurationPanelChanges(configurationData);
        IpasValidator<CUserdocType> validator = validatorCreator.create(false, UpdateUserdocTypeValidator.class);
        List<ValidationError> errors = validator.validate(cUserdocType);
        if (CollectionUtils.isEmpty(errors)) {
            CUserdocType userdocType = userdocTypesService.saveUserdocType(cUserdocType);
            model.addAttribute("successMessage", messageSource.getMessage("edit.success", null, LocaleContextHolder.getLocale()));
            fillModelAttributesForUserdocTypeConfigurationsPanel(model, userdocType);
            return "admin/userdoc/userdoc_administration/userdoc_type_edit_panels:: userdoc-type-configurations";
        } else {
            model.addAttribute("validationErrors", errors);
            return "base/modal/validation_errors::errors";
        }
    }

    @PostMapping("/save-userdoc-abdocs-document")
    public String editUserdocAbdocsDocument(@RequestParam("abdocsDoc") Integer abdocsDoc, @RequestParam("registrationType") Integer registrationType, @RequestParam("userdocType") String userdocType, Model model) {
        CAbdocsDocumentType cAbdocsDocumentType = setAbdocsDocumentChanges(abdocsDoc, registrationType, userdocType);
        IpasValidator<CAbdocsDocumentType> validator = validatorCreator.create(false, ChangeUserdocAbdocsDocumentValidator.class);
        List<ValidationError> errors = validator.validate(cAbdocsDocumentType);

        if (CollectionUtils.isEmpty(errors)) {
            abdocsDocumentTypeService.saveAbdocsDocumentType(cAbdocsDocumentType);
            model.addAttribute("successMessage", messageSource.getMessage("edit.success", null, LocaleContextHolder.getLocale()));
            fillModelAttributesForAbdocsDocumentsPanel(model, userdocType);
            return "admin/userdoc/userdoc_administration/userdoc_type_edit_panels:: userdoc-abdocs-document-data";
        } else {
            model.addAttribute("validationErrors", errors);
            return "base/modal/validation_errors::errors";
        }
    }

    @PostMapping("/add-panel-for-userdoc-type")
    public String addPanelForUserdocType(@RequestParam("panel") String panel, @RequestParam("userdocType") String userdocType, Model model) {
        try {
            CUserdocType cUserdocType = userdocTypesService.addPanelForUserdocType(userdocType, panel);

            return generateSuccessResponseDetailPanelsPanel(model, cUserdocType);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @PostMapping("/delete-panel-for-userdoc-type")
    public String deletePanelForUserdocType(@RequestParam("panel") String panel, @RequestParam("userdocType") String userdocType, Model model) {
        try {
            CUserdocType cUserdocType = userdocTypesService.deletePanelForUserdocType(userdocType, panel);

            return generateSuccessResponseDetailPanelsPanel(model, cUserdocType);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @PostMapping("/add-userdoc-person-role")
    public String addUserdocPersonRole(@RequestParam("role") UserdocPersonRole role, @RequestParam("userdocType") String userdocType, Model model) {
        try {
            CUserdocType cUserdocType = userdocTypesService.addRoleForUserdocType(userdocType, role);
            return generateSuccessResponsePersonRolesPanel(model, cUserdocType);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @PostMapping("/delete-userdoc-person-role")
    public String deleteUserdocPersonRole(@RequestParam("role") UserdocPersonRole role, @RequestParam("userdocType") String userdocType, Model model) {
        try {
            CUserdocType cUserdocType = userdocTypesService.deleteRoleForUserdocType(userdocType, role);
            return generateSuccessResponsePersonRolesPanel(model, cUserdocType);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @PostMapping("/add-reception-relation")
    public String addUserdocReceptionRelation(@RequestParam("mainType") String mainType, @RequestParam("userdocType") String userdocType, @RequestParam("isVisible") Boolean isVisible, Model model) {
        try {
            userdocReceptionRelationService.addReceptionRelationForUserdocType(userdocType, mainType, isVisible);
            return generateSuccessResponseReceptionRelationPanel(model, userdocType);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @PostMapping("/delete-reception-relation")
    public String deleteUserdocReceptionRelation(@RequestParam("mainType") String mainType, @RequestParam("userdocType") String userdocType, Model model) {
        try {
            userdocReceptionRelationService.deleteReceptionRelationForUserdocType(userdocType, mainType);
            return generateSuccessResponseReceptionRelationPanel(model, userdocType);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @PostMapping("/open-reception-relation-modal")
    public String openReceptionRelationModal(@RequestParam("mainType") String mainType, @RequestParam("userdocType") String userdocType, Model model) {
        CUserdocReceptionRelation cUserdocReceptionRelation = userdocReceptionRelationService.selectById(userdocType, mainType);
        model.addAttribute("cReceptionRelation", cUserdocReceptionRelation);
        model.addAttribute("mainTypesMap", userdocReceptionRelationService.selectMainTypesMap());
        return "admin/userdoc/userdoc_administration/userdoc_type_edit_modal :: modal";
    }

    @PostMapping("/edit-reception-relation")
    public String editReceptionRelation(@RequestParam("mainType") String mainType, @RequestParam("userdocType") String userdocType, @RequestParam("isVisible") Boolean isVisible, Model model) {
        try {
            userdocReceptionRelationService.updateReceptionRelation(userdocType, mainType, isVisible);
            return generateSuccessResponseReceptionRelationPanel(model, userdocType);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    private void fillUserdocTypesAttributes(Model model, UserdocTypesFilter userdocTypesFilter, Integer tableCount) {
        if (Objects.isNull(userdocTypesFilter.getPage())) {
            userdocTypesFilter.setPage(UserdocTypesFilter.DEFAULT_PAGE);
        }
        if (Objects.isNull(userdocTypesFilter.getPageSize())) {
            userdocTypesFilter.setPageSize(UserdocTypesFilter.DEFAULT_PAGE_SIZE);
        }

        List<CUserdocType> userdocTypesList = userdocTypesService.selectUserdocTypes(userdocTypesFilter);
        Integer userdocTypesCount;

        if (Objects.isNull(tableCount)) {
            userdocTypesCount = userdocTypesService.selectUserdocTypesCount(userdocTypesFilter);
        } else {
            userdocTypesCount = tableCount;
        }

        model.addAttribute("userdocTypesList", userdocTypesList);
        model.addAttribute("userdocTypesCount", userdocTypesCount);
        model.addAttribute("userdocTypesFilter", userdocTypesFilter);
    }

    private String generateSuccessResponseInvalidationPanel(Model model, CUserdocType userdocType) {
        fillModelAttributesForInvalidatedUserdocsPanel(model, userdocType);
        return "admin/userdoc/userdoc_administration/userdoc_type_edit_panels:: invalidated-userdoc-types-data";
    }

    private String generateSuccessResponsePersonRolesPanel(Model model, CUserdocType userdocType) {
        fillModelAttributesForPersonRolesPanel(model, userdocType);
        return "admin/userdoc/userdoc_administration/userdoc_type_edit_panels:: userdoc-person-roles-data";
    }

    private String generateSuccessResponseDetailPanelsPanel(Model model, CUserdocType userdocType) {
        fillModelAttributesForDetailPagePanelsPanel(model, userdocType);
        return "admin/userdoc/userdoc_administration/userdoc_type_edit_panels:: userdoc-panels-data";
    }

    private String generateSuccessResponseReceptionRelationPanel(Model model, String userdocType) {
        fillModelAttributesForReceptionRelationPanel(model, userdocType);
        return "admin/userdoc/userdoc_administration/userdoc_type_edit_panels:: userdoc-reception-relation-data";
    }

    private String generateValidationErrorModalResponse(Model model, IpasValidationException e) {
        model.addAttribute("validationErrors", e.getErrors());
        return "base/modal/validation_errors::errors";
    }

    private void fillModelAttributesForMainPanel(Model model, CUserdocType userdocType) {
        model.addAttribute("processTypeMap", processTypeService.getProcessTypeMap());
        model.addAttribute("userdocType", userdocType);
    }

    private void fillModelAttributesForInvalidatedUserdocsPanel(Model model, CUserdocType userdocType) {
        if (!CollectionUtils.isEmpty(userdocType.getInvalidatedUserdocTypes())) {
            Map<String, String> invalidatedUserdocNames = userdocTypesService.selectInvalidatedUserdocNames(userdocType.getInvalidatedUserdocTypes());
            model.addAttribute("invalidatedUserdocNames", invalidatedUserdocNames);
        }
        List<String> userdocsInvalidatingCurrentUserdoc = userdocTypesService.selectUserdocsInvalidatingCurrentUserdoc(userdocType.getUserdocType());
        model.addAttribute("userdocsInvalidatingCurrentUserdoc", userdocsInvalidatingCurrentUserdoc);
    }

    private void fillModelAttributesForDetailPagePanelsPanel(Model model, CUserdocType userdocType) {
        model.addAttribute("userdocType", userdocType);
        model.addAttribute("userdocPanelsMap", userdocPanelService.getUserdocPanelsSelectOptions(userdocType));
    }

    private void fillModelAttributesForPersonRolesPanel(Model model, CUserdocType userdocType) {
        model.addAttribute("userdocType", userdocType);
        model.addAttribute("personRolesMap", userdocPersonRoleService.getPersonRolesSelectOptions(userdocType));
    }

    private void fillModelAttributesForReceptionRelationPanel(Model model, String userdocType) {
        model.addAttribute("mainTypesMap", userdocReceptionRelationService.selectMainTypesMap());
        model.addAttribute("mainTypesByUserdocType", userdocReceptionRelationService.selectAllMainTypesForUserdocType(userdocType));
    }

    private void fillModelAttributesForAbdocsDocumentsPanel(Model model, String userdocType) {
        List<DocumentTypeDto> activeDocumentTypes = abdocsService.selectDocumentTypesByGroup(DefaultValue.ABDOCS_DOCUMENTS_IPAS_GROUP).stream().filter(d -> d.getIsActive()).collect(Collectors.toList());
        model.addAttribute("currentAbdocsDoc", abdocsDocumentTypeService.selectByType(userdocType));
        model.addAttribute("abdocsDocuments", activeDocumentTypes);
    }

    private void fillModelAttributesForUserdocTypeConfigurationsPanel(Model model, CUserdocType userdocType) {
        CUser userTargetingOnRegistration = null;
        if (Objects.nonNull(userdocType.getUserdocTypeConfig()) && Objects.nonNull(userdocType.getUserdocTypeConfig().getAbdocsUserTargetingOnRegistration())) {
            userTargetingOnRegistration = userService.getUserByLogin(userdocType.getUserdocTypeConfig().getAbdocsUserTargetingOnRegistration());
        }
        model.addAttribute("userdocType", userdocType);
        model.addAttribute("userTargetingOnRegistration", userTargetingOnRegistration);
        model.addAttribute("userdocTypeDepartments", officeDepartmentService.findAll());
    }

    private CUserdocType setMainPanelChanges(UserdocTypeMainData mainData) {
        CUserdocType cUserdocType = userdocTypesService.selectUserdocTypeById(mainData.getUserdocType());

        if (Objects.nonNull(cUserdocType)) {
            cUserdocType.setUserdocName(mainData.getUserdocName());
            cUserdocType.setUserdocGroup(mainData.getUserdocGroup());
            cUserdocType.setGenerateProcTyp(mainData.getGenerateProcType());
            cUserdocType.setIndInactive(mainData.getIndInactive());
            return cUserdocType;
        }
        return null;
    }

    private CUserdocType setConfigurationPanelChanges(UserdocTypeConfigurationData configurationData) {
        CUserdocType cUserdocType = userdocTypesService.selectUserdocTypeById(configurationData.getUserdocType());
        CUserdocTypeConfig cUserdocTypeConfig = new CUserdocTypeConfig();
        cUserdocTypeConfig.setUserdocTyp(configurationData.getUserdocType());
        cUserdocTypeConfig.setRegisterToProcess(configurationData.getRegisterToProcess());
        cUserdocTypeConfig.setMarkInheritResponsibleUser(configurationData.getMarkInheritResponsibleUser());
        cUserdocTypeConfig.setInheritResponsibleUser(configurationData.getInheritResponsibleUser());
        cUserdocTypeConfig.setAbdocsUserTargetingOnResponsibleUserChange(configurationData.getAbdocsUserTargetingOnResponsibleUserChange());
        cUserdocTypeConfig.setHasPublicLiabilities(configurationData.isHasPublicLiabilities());
        if (Objects.nonNull(configurationData.getAbdocsUserTargetingOnRegistrationId())) {
            User user = userService.getUser(configurationData.getAbdocsUserTargetingOnRegistrationId());
            cUserdocTypeConfig.setAbdocsUserTargetingOnRegistration(user.getLogin());
        } else {
            cUserdocTypeConfig.setAbdocsUserTargetingOnRegistration(null);
        }
        if (!CollectionUtils.isEmpty(configurationData.getDepartmentIds())) {
            List<CUserdocTypeDepartment> departments = new ArrayList<>();
            for (String departmentFullId : configurationData.getDepartmentIds()) {
                CUserdocTypeDepartment department = new CUserdocTypeDepartment();
                department.setUserdocTyp(configurationData.getUserdocType());
                String[] departmentKeyArray = departmentFullId.split("-");
                department.setDepartment(officeDepartmentService.getDepartment(departmentKeyArray[0], departmentKeyArray[1]));
                departments.add(department);
            }
            cUserdocTypeConfig.setDepartments(departments);
        } else {
            cUserdocTypeConfig.setDepartments(new ArrayList<>());
        }
        cUserdocType.setUserdocTypeConfig(cUserdocTypeConfig);
        return cUserdocType;
    }

    private CAbdocsDocumentType setAbdocsDocumentChanges(Integer abdocsDoc, Integer registrationType, String userdocType) {
        CAbdocsDocumentType cAbdocsDocumentType = abdocsDocumentTypeService.selectByType(userdocType);
        Map<Integer, String> documentTypesMap = abdocsService.selectDocumentTypesByGroup(DefaultValue.ABDOCS_DOCUMENTS_IPAS_GROUP).stream().collect(Collectors.toMap(DocumentTypeDto::getNomValueId, DocumentTypeDto::getName));

        if (Objects.nonNull(cAbdocsDocumentType)) {
            cAbdocsDocumentType.setAbdocsDocTypeId(abdocsDoc);
            cAbdocsDocumentType.setName(documentTypesMap.get(abdocsDoc));
            cAbdocsDocumentType.setDocRegistrationType(registrationType);
            return cAbdocsDocumentType;
        } else {
            CAbdocsDocumentType newAbdocsDocumentType = new CAbdocsDocumentType();

            newAbdocsDocumentType.setAbdocsDocTypeId(abdocsDoc);
            newAbdocsDocumentType.setName(documentTypesMap.get(abdocsDoc));
            newAbdocsDocumentType.setType(userdocType);
            newAbdocsDocumentType.setIpasObject(IpasObjectType.USERDOC);
            newAbdocsDocumentType.setDocRegistrationType(registrationType);
            return newAbdocsDocumentType;
        }
    }

}
