package com.duosoft.ipas.controller.admin.structure;

import bg.duosoft.ipas.core.model.structure.OfficeDepartment;
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
@RequestMapping(value = "/structure/department")
public class DepartmentController extends StructureControllerBase {


    @GetMapping(value = "/edit/{divisionCode}/{departmentCode}")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String editDepartment(@PathVariable("divisionCode") String divisionCode, @PathVariable("departmentCode") String departmentCode, Model model) {
        OfficeDepartment department = officeDepartmentService.getDepartment(divisionCode, departmentCode);

        generateEditModel(department, model, false);

        return "structure/edit/edit_department";
    }

    @GetMapping(value = "/view/{divisionCode}/{departmentCode}")
    public String viewDepartment(@PathVariable("divisionCode") String divisionCode, @PathVariable("departmentCode") String departmentCode, Model model) {
        OfficeDepartment department = officeDepartmentService.getDepartment(divisionCode, departmentCode);
        generateEditModel(department, model, false);
        return "structure/edit/view_structure";
    }


    @GetMapping("/new")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String newDepartment(Model model) {
        generateEditModel(null, model, true);
        return "structure/edit/edit_department";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String saveDepartment(@ModelAttribute("officeDepartment") OfficeDepartment department, @RequestParam("isNew")boolean isNew, Model model, RedirectAttributes redirectAttributes) {
        try {
            OfficeStructureId departmentId = department.getOfficeStructureId();
            if (isNew) {
                departmentId = officeDepartmentService.insertDepartment(department);
                redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("structure.create.success", null, LocaleContextHolder.getLocale()));
            } else {
                officeDepartmentService.updateDepartment(department);
                redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("structure.edit.success", null, LocaleContextHolder.getLocale()));
            }

            return String.format("redirect:/structure/department/edit/%s/%s", departmentId.getOfficeDivisionCode(), departmentId.getOfficeDepartmentCode());
        } catch (IpasValidationException e) {
            model.addAttribute("validationErrors", e.getErrors());
            generateEditModel(department, model, isNew);
            return "structure/edit/edit_department";
        }
    }

    @PostMapping(value = "/delete/{divisionCode}/{departmentCode}")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String deleteDepartment(@PathVariable("divisionCode") String divisionCode, @PathVariable("departmentCode") String departmentCode, Model model) {
        OfficeDepartment dep = officeDepartmentService.getDepartment(divisionCode, departmentCode);
        try {
            officeDepartmentService.archiveDepartment(dep);
            return generateActionSuccessResponse(model);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @PostMapping(value = "/activate/{divisionCode}/{departmentCode}")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String activateDepartment(@PathVariable("divisionCode") String divisionCode, @PathVariable("departmentCode") String departmentCode, Model model) {
        OfficeDepartment dep = officeDepartmentService.getDepartment(divisionCode, departmentCode);
        try {
            dep.setActive(true);
            officeDepartmentService.updateDepartment(dep);
            return generateActionSuccessResponse(model);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @RequestMapping("/transfer")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String transferDepartments(@RequestParam("data") String data, Model model) {
        TransferStructureRequestData transferStructureData = JsonUtil.readJson(data, TransferStructureRequestData.class);
        List<OfficeDepartment> departments = transferStructureData.getStructureIds().stream().map(s -> officeDepartmentService.getDepartment(s.getOfficeDivisionCode(), s.getOfficeDepartmentCode())).collect(Collectors.toList());
        try {
            officeDepartmentService.transferDepartments(departments, transferStructureData.getNewStructureId().getOfficeDivisionCode());
            return generateActionSuccessResponse(model);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }



    private void generateEditModel(OfficeDepartment department, Model model, boolean isNew) {
        model.addAttribute("structure", department);
        model.addAttribute("isNew", isNew);
        generateStructureDivisionsModel(department == null ? null : department.getOfficeStructureId(), model);

        if (!isNew) {
            List<OfficeDepartment> activeDepartments = officeDepartmentService.getDepartmentsByPartOfName(null, true);
            model.addAttribute("transferSectionDepartments", activeDepartments);

            List<OfficeSection> departmentSections = officeSectionService.getSectionsOfDepartment(department.getOfficeStructureId().getOfficeDivisionCode(), department.getOfficeStructureId().getOfficeDepartmentCode(), false);
            model.addAttribute("structureSections", departmentSections);

            generateStructureEmployeesModel(department, model);
            generateTransferUsersStructureModel(model);
            generateSignatureUsersModel(department, model);
        }
    }

    @RequestMapping("/sectionsbydepartment")
    public String getSectionsByDepartment(@RequestParam(value = "divisionCode") String divisionCode, @RequestParam(value = "departmentCode") String departmentCode, Model model) {
        List<OfficeSection> sections;
        sections = officeSectionService.getSectionsOfDepartment(divisionCode, departmentCode, true);
        model.addAttribute("availableSections", sections);
        model.addAttribute("disabled", false);
        return "structure/edit/select_section::select-section";
    }
}
