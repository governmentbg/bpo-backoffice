package com.duosoft.ipas.controller.admin.structure;

import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.structure.Group;
import bg.duosoft.ipas.core.service.structure.GroupService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

/**
 * User: ggeorgiev
 * Date: 26.7.2019 г.
 * Time: 15:16
 */
@Controller
@RequestMapping("/structure/group")
@PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureGroupView.code())")
public class GroupController extends StructureControllerBase {
    @Autowired
    private GroupService groupService;

    @RequestMapping("/list")
    public String listGroups(Model model) {
        Map<Integer, String> groupsMap = groupService.getGroupsMap();
        model.addAttribute("groupsMap", groupsMap);
        return "structure/groups_list";
    }

    @RequestMapping("/new")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureGroupEdit.code())")
    public String newGroup(Model model) {
        generateEditModel(null, model, true);
        return "structure/group_edit";
    }

    @RequestMapping("/edit/{id}")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureGroupEdit.code())")
    public String editGroup(@PathVariable("id") int id, Model model) {
        Group group = groupService.getGroup(id);
        generateEditModel(group, model, false);
        return "structure/group_edit";
    }

    @RequestMapping("/view/{id}")
    public String viewGroup(@PathVariable("id") int id, Model model) {
        Group group = groupService.getGroup(id);
        generateEditModel(group, model, false);
        return "structure/group_view";
    }

    @RequestMapping("/save")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureGroupEdit.code())")
    public String editGroup(@ModelAttribute Group group, @RequestParam("isNew") boolean isNew, RedirectAttributes redirectAttributes, Model model) {
        try {
            int groupId = groupService.saveGroup(group);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("group.create.success", null, LocaleContextHolder.getLocale()));
            return "redirect:/structure/group/edit/" + groupId;
        } catch (IpasValidationException e) {
            model.addAttribute("validationErrors", e.getErrors());
            generateEditModel(group, model, isNew);
            return "structure/group_edit";
        }
    }

    @PostMapping("/useradd")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureGroupEdit.code())")
    public String addUserToGroup(@RequestParam("groupId") Integer groupId, @RequestParam("userId") Integer userId, Model model) {
        try {
            groupService.addUserToGroup(groupId, userId);
            return generateActionSuccessResponse(model);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }

    }

    @PostMapping("/userdelete")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureGroupEdit.code())")
    public String removeUserFromGroup(@RequestParam("groupId") Integer groupId, @RequestParam("userId") Integer userId, Model model) {
        try {
            groupService.removeUserFromGroup(groupId, userId);
            return generateActionSuccessResponse(model);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    private void generateEditModel(Group group, Model model, boolean isNew) {
        model.addAttribute("group", group);
        model.addAttribute("rolesMap", groupService.getRolesMap());
        model.addAttribute("isNew", isNew);
        if (!isNew) {
            List<CUser> groupUsers = groupService.getUsersByGroup(group.getGroupId());
            model.addAttribute("groupUsers", groupUsers);
        }

    }
}

