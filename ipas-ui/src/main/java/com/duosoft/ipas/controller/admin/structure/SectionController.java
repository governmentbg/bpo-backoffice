package com.duosoft.ipas.controller.admin.structure;

import bg.duosoft.ipas.core.model.structure.OfficeSection;
import bg.duosoft.ipas.core.model.structure.OfficeStructureId;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.webmodel.structure.TransferStructureRequestData;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/structure/section")
public class SectionController extends StructureControllerBase {

    @GetMapping(value = "/new")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String newSection(Model model) {
        generateEditModel(null, model, true);
        return "structure/edit/edit_section";
    }

    @GetMapping(value = "/edit/{divisionCode}/{departmentCode}/{sectionCode}")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String editSection(@PathVariable("divisionCode") String divisionCode, @PathVariable("departmentCode") String departmentCode, @PathVariable("sectionCode") String sectionCode, Model model) {

        OfficeSection section = officeSectionService.getSection(divisionCode, departmentCode, sectionCode);
        if (!section.getActive()) {
            throw new RuntimeException("Cannot edit structure...");
        }

        generateEditModel(section, model, false);
        return "structure/edit/edit_section";
    }

    @GetMapping(value = "/view/{divisionCode}/{departmentCode}/{sectionCode}")
    public String viewSection(@PathVariable("divisionCode") String divisionCode, @PathVariable("departmentCode") String departmentCode, @PathVariable("sectionCode") String sectionCode, Model model) {
        OfficeSection section = officeSectionService.getSection(divisionCode, departmentCode, sectionCode);
        generateEditModel(section, model, false);
        return "structure/view/view_structure";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String saveSection(@ModelAttribute OfficeSection section, @RequestParam("isNew") boolean isNew, Model model, RedirectAttributes redirectAttributes) {
        try {
            OfficeStructureId sectionId = section.getOfficeStructureId();
            if (isNew) {
                sectionId = officeSectionService.insertSection(section);
                redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("structure.create.success", null, LocaleContextHolder.getLocale()));
            } else {
                officeSectionService.updateSection(section);
                redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("structure.edit.success", null, LocaleContextHolder.getLocale()));
            }
            return String.format("redirect:/structure/section/edit/%s/%s/%s", sectionId.getOfficeDivisionCode(), sectionId.getOfficeDepartmentCode(), sectionId.getOfficeSectionCode());

        } catch (IpasValidationException e) {
            model.addAttribute("validationErrors", e.getErrors());
            generateEditModel(section, model, isNew);
            return "structure/edit/edit_section";
        }

    }

    private void generateEditModel(OfficeSection section, Model model, boolean isNew) {
        model.addAttribute("structure", section);
        model.addAttribute("isNew", isNew);
        generateStructureDivisionsModel(section == null ? null : section.getOfficeStructureId(), model);
        generateStructureDepartmentsModel(section == null ? null : section.getOfficeStructureId(), model);
        if (!isNew) {
            generateStructureEmployeesModel(section, model);
            generateTransferUsersStructureModel(model);
            generateSignatureUsersModel(section, model);
        }
    }


    @RequestMapping("/transfer")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String transferSections(@RequestParam("data") String data, Model model) {
        TransferStructureRequestData transferUserData = JsonUtil.readJson(data, TransferStructureRequestData.class);
        List<OfficeSection> sections = transferUserData.getStructureIds().stream().map(s -> officeSectionService.getSection(s.getOfficeDivisionCode(), s.getOfficeDepartmentCode(), s.getOfficeSectionCode())).collect(Collectors.toList());
        try {
            officeSectionService.transferSections(sections, transferUserData.getNewStructureId().getOfficeDivisionCode(), transferUserData.getNewStructureId().getOfficeDepartmentCode());
            return generateActionSuccessResponse(model);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @PostMapping(value = "/delete/{divisionCode}/{departmentCode}/{sectionCode}")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String deleteSection(@PathVariable("divisionCode") String divisionCode, @PathVariable("departmentCode") String departmentCode, @PathVariable("sectionCode") String sectionCode, Model model) {
        OfficeSection section = officeSectionService.getSection(divisionCode, departmentCode, sectionCode);
        try {
            officeSectionService.archiveSection(section);
            return generateActionSuccessResponse(model);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @PostMapping(value = "/activate/{divisionCode}/{departmentCode}/{sectionCode}")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String activateSection(@PathVariable("divisionCode") String divisionCode, @PathVariable("departmentCode") String departmentCode, @PathVariable("sectionCode") String sectionCode, Model model) {
        OfficeSection section = officeSectionService.getSection(divisionCode, departmentCode, sectionCode);
        try {
            section.setActive(true);
            officeSectionService.updateSection(section);
            return generateActionSuccessResponse(model);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }
}
