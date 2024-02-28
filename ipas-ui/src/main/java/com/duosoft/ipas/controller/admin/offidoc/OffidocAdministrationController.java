package com.duosoft.ipas.controller.admin.offidoc;

import bg.duosoft.ipas.core.model.offidoc.COffidocType;
import bg.duosoft.ipas.core.model.offidoc.COffidocTypeTemplate;
import bg.duosoft.ipas.core.service.nomenclature.OffidocTypeService;
import bg.duosoft.ipas.core.service.nomenclature.OffidocTypeTemplateService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.offidoc.UpdateOffidocTypeValidator;
import bg.duosoft.ipas.util.filter.OffidocTypeFilter;
import bg.duosoft.ipas.util.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin/offidoc")
public class OffidocAdministrationController {

    @Autowired
    private OffidocTypeService offidocTypeService;

    @Autowired
    private OffidocTypeTemplateService offidocTypeTemplateService;

    @Autowired
    private IpasValidatorCreator validatorCreator;

    @Autowired
    private MessageSource messageSource;


    @GetMapping(value = "/list")
    public String getOffidocTypeList(Model model, OffidocTypeFilter filter) {
        fillOffidocTypeAttributes(model, filter, null);
        return "admin/offidoc/offidoc";
    }

    @PostMapping(value = "/search")
    public String search(Model model, OffidocTypeFilter filter) {
        fillOffidocTypeAttributes(model, filter, null);
        return "admin/offidoc/offidoc_table :: table";
    }

    @PostMapping(value = "/update-table")
    public String updateTable(Model model, @RequestParam Integer tableCount, OffidocTypeFilter filter) {
        fillOffidocTypeAttributes(model, filter, tableCount);
        return "admin/offidoc/offidoc_table :: table";
    }

    @GetMapping(value = "/edit/{id}")
    public String editUserdocType(@PathVariable("id") String id, OffidocTypeFilter filter, Model model) {
        COffidocType cOffidocType = offidocTypeService.selectById(id);
        model.addAttribute("offidocType", cOffidocType);
        model.addAttribute("offidocTypeFilter", filter);
        return "admin/offidoc/offidoc_edit";
    }

    @PostMapping(value = "/save-main-offidoc-data")
    public String saveMainOffidocData(@RequestParam String data, Model model) {
        COffidocType formData = JsonUtil.readJson(data, COffidocType.class);
        COffidocType cOffidocTypeUpdated = createUpdatedOffidocType(formData);

        IpasValidator<COffidocType> validator = validatorCreator.create(false, UpdateOffidocTypeValidator.class);
        List<ValidationError> errors = validator.validate(cOffidocTypeUpdated);

        if (CollectionUtils.isEmpty(errors)) {
            COffidocType cOffidocType = offidocTypeService.saveOffidocType(cOffidocTypeUpdated);
            model.addAttribute("successMessage", messageSource.getMessage("edit.success", null, LocaleContextHolder.getLocale()));
            model.addAttribute("offidocType", cOffidocType);
            return "admin/offidoc/offidoc_edit_panels :: main-offidoc-type-data";
        } else {
            model.addAttribute("validationErrors", errors);
            return "base/modal/validation_errors::errors";
        }
    }

    @PostMapping(value = "/add-offidoc-template")
    public String addOffidocTemplate(@RequestParam("offidocType") String offidocType, @RequestParam("template") String template, Model model) {
        try {
            COffidocType cOffidocType = offidocTypeService.addOffidocTemplate(offidocType, template);
            return generateSuccessResponse(model, cOffidocType);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @PostMapping(value = "/delete-offidoc-template")
    public String deleteOffidocTemplate(@RequestParam("offidocType") String offidocType, @RequestParam("template") String template, Model model) {
        try {
            COffidocType cOffidocType = offidocTypeService.deleteOffidocTemplate(offidocType, template);
            return generateSuccessResponse(model, cOffidocType);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @PostMapping(value = "/change-offidoc-default-template")
    public String changeOffidocDefaultTemplate(@RequestParam("offidocType") String offidocType, @RequestParam("template") String template, Model model) {
        try {
            COffidocType cOffidocType = offidocTypeService.changeOffidocDefaultTemplate(offidocType, template);
            return generateSuccessResponse(model, cOffidocType);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @PostMapping(value = "/open-offidoc-template-modal")
    public String openOffidocTempateModal(@RequestParam("templateName") String templateName, @RequestParam("offidocType") String offidocType, Model model) {
        COffidocTypeTemplate template = offidocTypeTemplateService.selectById(offidocType, templateName);
        model.addAttribute("cOffidocTypeTemplate", template);
        return "admin/offidoc/offidoc_template_edit_modal :: modal";
    }

    @PostMapping(value = "/edit-offidoc-template")
    public String editOffidocTemplate(@RequestParam("template") String template, @RequestParam("offidocType") String offidocType, @RequestParam("nameConfig") String nameConfig, Model model) {
        try {
            offidocTypeTemplateService.updateOffidocTemplateConfig(offidocType, template, nameConfig);
            return generateSuccessResponse(model, offidocTypeService.selectById(offidocType));
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    private void fillOffidocTypeAttributes(Model model, OffidocTypeFilter offidocTypeFilter, Integer tableCount) {
        if (Objects.isNull(offidocTypeFilter.getPage())) {
            offidocTypeFilter.setPage(OffidocTypeFilter.DEFAULT_PAGE);
        }
        if (Objects.isNull(offidocTypeFilter.getPageSize())) {
            offidocTypeFilter.setPageSize(OffidocTypeFilter.DEFAULT_PAGE_SIZE);
        }

        List<COffidocType> offidocTypeList = offidocTypeService.selectOffidocTypes(offidocTypeFilter);
        Integer offidocTypeCount;

        if (Objects.isNull(tableCount)) {
            offidocTypeCount = offidocTypeService.selectOffidocTypeCount(offidocTypeFilter);
        } else {
            offidocTypeCount = tableCount;
        }

        model.addAttribute("offidocTypeList", offidocTypeList);
        model.addAttribute("offidocTypeCount", offidocTypeCount);
        model.addAttribute("offidocTypeFilter", offidocTypeFilter);
    }

    private String generateSuccessResponse(Model model, COffidocType cOffidocType) {
        model.addAttribute("offidocType", cOffidocType);
        return "admin/offidoc/offidoc_edit_panels :: offidoc-templates-data";
    }

    private String generateValidationErrorModalResponse(Model model, IpasValidationException e) {
        model.addAttribute("validationErrors", e.getErrors());
        return "base/modal/validation_errors::errors";
    }

    private COffidocType createUpdatedOffidocType(COffidocType formData) {
        COffidocType cOffidocType = offidocTypeService.selectById(formData.getOffidocType());
        if (Objects.nonNull(cOffidocType)) {
            cOffidocType.setOffidocName(formData.getOffidocName());
            cOffidocType.setDirection(formData.getDirection());
            cOffidocType.setHasPublication(formData.getHasPublication());
            return cOffidocType;
        }

        return null;
    }

}
