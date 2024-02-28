package com.duosoft.ipas.controller.admin.structure;


import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.structure.*;
import bg.duosoft.ipas.core.service.person.SimpleUserService;
import bg.duosoft.ipas.core.service.structure.OfficeDepartmentService;
import bg.duosoft.ipas.core.service.structure.OfficeDivisionService;
import bg.duosoft.ipas.core.service.structure.OfficeSectionService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;

import java.util.*;

@PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureView.code())")
public abstract class StructureControllerBase {
    @Autowired
    protected OfficeDivisionService officeDivisionService;
    @Autowired
    protected OfficeDepartmentService officeDepartmentService;
    @Autowired
    protected OfficeSectionService officeSectionService;
    @Autowired
    protected UserService userService;
    @Autowired
    protected SimpleUserService simpleUserService;

    @Autowired
    protected MessageSource messageSource;


    protected void generateStructureEmployeesModel(StructureNode structureNode, Model model) {
        List<CUser> users = simpleUserService.getUsers(structureNode.getOfficeStructureId().getOfficeDivisionCode(), structureNode.getOfficeStructureId().getOfficeDepartmentCode(), structureNode.getOfficeStructureId().getOfficeSectionCode(), true, false);
        model.addAttribute("structureEmployees", users);

    }

    protected void generateTransferUsersStructureModel(Model model) {
        List<OfficeDivision> activeDivisions = officeDivisionService.getDivisionsByPartOfName(null, true);
        List<OfficeDepartment> activeDepartments = officeDepartmentService.getDepartmentsByPartOfName(null, true);
        List<OfficeSection> activeSections = officeSectionService.getSectionsByPartOfName(null, true);

        List<StructureNode> ddsWm = new ArrayList<>();
        activeDivisions.stream().forEach(ddsWm::add);
        activeDepartments.stream().forEach(ddsWm::add);
        activeSections.stream().forEach(ddsWm::add);
        Collections.sort(ddsWm, Comparator.comparing(StructureNode::getName));
        model.addAttribute("transferUserStructure", ddsWm);
    }

    protected void generateSignatureUsersModel(StructureNode structureNode, Model model) {
        List<CUser> structureUsers = simpleUserService.getUsers(structureNode.getOfficeStructureId().getOfficeDivisionCode(), structureNode.getOfficeStructureId().getOfficeDepartmentCode(), structureNode.getOfficeStructureId().getOfficeSectionCode(), true, true);

        if (structureNode.getSignatureUser() != null && structureNode.getSignatureUser().getUserId() != null) {
            if (structureUsers.stream().map(s -> s.getUserId()).filter(u -> Objects.equals(structureNode.getSignatureUser().getUserId(), u)).findFirst().isEmpty()) {
                structureUsers.add(simpleUserService.findSimpleUserById(structureNode.getSignatureUser().getUserId()));
                structureUsers.sort(Comparator.comparing(CUser::getUserName));
            }
        }
        model.addAttribute("directors", structureUsers);

    }

    protected String generateActionSuccessResponse(Model model) {
        model.addAttribute("message", messageSource.getMessage("structure.modified", null, LocaleContextHolder.getLocale()));
        return "structure/action_success_modal::action-success";
    }
    protected String generateValidationErrorModalResponse(Model model, IpasValidationException e) {
        model.addAttribute("validationErrors", e.getErrors());
        return "base/modal/validation_errors::errors";
    }

    /**
     * pri edit na strcuture -> generira model-a za dropdown-a s division-ite
     *
     * @param currentOfficeStructureId
     * @param model
     */
    protected void generateStructureDivisionsModel(OfficeStructureId currentOfficeStructureId, Model model) {
        List<OfficeDivision> activeDivisions = officeDivisionService.getDivisionsByPartOfName(null, true);
        if (currentOfficeStructureId != null && currentOfficeStructureId.getFullDivisionCode() != null) {
            //checks if the current structure's department is missing (it is not active) - it should be added separately in the list
            Optional<OfficeStructureId> currentDivision = activeDivisions.stream().map(r -> r.getOfficeStructureId()).filter(r -> r.getFullDivisionCode().equals(currentOfficeStructureId.getFullDivisionCode())).findFirst();
            if (currentDivision.isEmpty()) {
                activeDivisions.add(officeDivisionService.getDivision(currentOfficeStructureId.getFullDivisionCode()));
                Collections.sort(activeDivisions, Comparator.comparing(OfficeDivision::getName));
            }
        }
        model.addAttribute("availableDivisions", activeDivisions);
    }

    /**
     * pri edit na structure -> generira drop-down-a s departments
     *
     * @param currentNodeId
     * @param model
     */
    protected void generateStructureDepartmentsModel(OfficeStructureId currentNodeId, Model model) {
        if (currentNodeId != null && currentNodeId.getFullDivisionCode() != null) {
            //if there is a division code, takes all the departments from the division
            List<OfficeDepartment> activeDepartments = officeDepartmentService.getDepartmentsOfDivision(currentNodeId.getOfficeDivisionCode(), true);
            //if there is a department code, checks if the department is already selected(exists in the list of active departments). If not - adds it!
            if (currentNodeId != null && currentNodeId.getFullDepartmentCode() != null) {
                //checks if the current structure's department is missing in the activeDepartments (it is not active) and should be added in the list!
                Optional<OfficeDepartment> found = activeDepartments.stream().filter(r -> r.getOfficeStructureId().getFullDepartmentCode().equals(currentNodeId.getFullDepartmentCode())).findAny();
                if (found.isEmpty()) {
                    activeDepartments.add(officeDepartmentService.getDepartment(currentNodeId));
                    Collections.sort(activeDepartments, Comparator.comparing(OfficeDepartment::getName));
                }
            }
            if (!CollectionUtils.isEmpty(activeDepartments)){
                activeDepartments.sort(Comparator.comparing(OfficeDepartment::getName));
            }
            model.addAttribute("availableDepartments", activeDepartments);
        }
    }

    /**
     * pri edit na structure -> generira drop-down-a sys sections
     *
     * @param currentNodeId
     * @param model
     */
    protected void generateStructureSectionsModel(OfficeStructureId currentNodeId, Model model) {
        if (currentNodeId != null && currentNodeId.getFullDepartmentCode() != null) {
            List<OfficeSection> activeSections = officeSectionService.getSectionsOfDepartment(currentNodeId.getOfficeDivisionCode(), currentNodeId.getOfficeDepartmentCode(), true);
            if (currentNodeId.getFullSectionCode() != null) {
                //checks if the current section exists in the active sections. If it does not exist, it's getting added separately
                Optional<OfficeSection> found = activeSections.stream().filter(r -> r.getOfficeStructureId().getFullSectionCode().equals(currentNodeId.getFullSectionCode())).findAny();
                if (found.isEmpty()) {
                    activeSections.add(officeSectionService.getSection(currentNodeId));
                    Collections.sort(activeSections, Comparator.comparing(OfficeSection::getName));
                }
            }
            model.addAttribute("availableSections", activeSections);
        }
    }

}
