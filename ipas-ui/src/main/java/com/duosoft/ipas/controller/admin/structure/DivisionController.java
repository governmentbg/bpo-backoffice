package com.duosoft.ipas.controller.admin.structure;


import bg.duosoft.ipas.core.model.structure.OfficeDepartment;
import bg.duosoft.ipas.core.model.structure.OfficeDivision;
import bg.duosoft.ipas.core.model.structure.OfficeStructureId;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/structure/division")
public class DivisionController extends StructureControllerBase {

    @RequestMapping("/departmentsbydivision")
    public String getDepartmentsByDivision(@RequestParam(value = "divisionCode", required = false) String divisionCode, Model model) {
        List<OfficeDepartment> departments;
        if (divisionCode != null) {
            departments = officeDepartmentService.getDepartmentsOfDivision(divisionCode, true);
        } else {
            departments = new ArrayList<>();
        }
        model.addAttribute("availableDepartments", departments);
        model.addAttribute("disabled", false);
        return "structure/edit/select_department::select-department";
    }

    @RequestMapping("/edit/{divisionCode}")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String editDivision(@PathVariable("divisionCode") String divisionCode, Model model) {
        OfficeDivision division = officeDivisionService.getDivision(divisionCode);
        generateEditModel(division, model, false);
        return "structure/edit/edit_division";
    }

    @RequestMapping("/view/{divisionCode}")
    public String viewDivision(@PathVariable("divisionCode") String divisionCode, Model model) {
        OfficeDivision division = officeDivisionService.getDivision(divisionCode);
        generateEditModel(division, model, false);
        return "structure/edit/view_structure";
    }

    @PostMapping(value = "/delete/{divisionCode}")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String deleteDivision(@PathVariable("divisionCode") String divisionCode, Model model) {
        OfficeDivision division = officeDivisionService.getDivision(divisionCode);
        try {
            officeDivisionService.archiveDivision(division);
            return generateActionSuccessResponse(model);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @PostMapping(value = "/activate/{divisionCode}")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String activateDivision(@PathVariable("divisionCode") String divisionCode, Model model) {
        OfficeDivision division = officeDivisionService.getDivision(divisionCode);
        try {
            division.setActive(true);
            officeDivisionService.updateDivision(division);
            return generateActionSuccessResponse(model);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @GetMapping(value = "/new")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String newSection(Model model) {
        generateEditModel(null, model, true);
        return "structure/edit/edit_division";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String saveSection(@ModelAttribute OfficeDivision division, @RequestParam("isNew") boolean isNew, Model model, RedirectAttributes redirectAttributes) {
        try {
            OfficeStructureId divisionId = division.getOfficeStructureId();
            if (isNew) {
                divisionId = officeDivisionService.insertDivision(division);
                redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("structure.create.success", null, LocaleContextHolder.getLocale()));
            } else {
                officeDivisionService.updateDivision(division);
                redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("structure.edit.success", null, LocaleContextHolder.getLocale()));
            }

            return String.format("redirect:/structure/division/edit/%s", divisionId.getOfficeDivisionCode());

        } catch (IpasValidationException e) {
            model.addAttribute("validationErrors", e.getErrors());
            generateEditModel(division, model, isNew);
            return "structure/edit/edit_section";
        }

    }

    protected void generateEditModel(OfficeDivision division, Model model, boolean isNew) {
        model.addAttribute("structure", division);
        model.addAttribute("isNew", isNew);
        if (!isNew) {
            List<OfficeDepartment> divisionDepartments = officeDepartmentService.getDepartmentsOfDivision(division.getOfficeStructureId().getOfficeDivisionCode(), false);
            model.addAttribute("structureDepartments", divisionDepartments);

            model.addAttribute("transferDepartmentDivisions", officeDivisionService.getDivisionsByPartOfName(null, true));
            generateTransferUsersStructureModel(model);
            generateSignatureUsersModel(division, model);
            generateStructureEmployeesModel(division, model);
        }
    }
}
