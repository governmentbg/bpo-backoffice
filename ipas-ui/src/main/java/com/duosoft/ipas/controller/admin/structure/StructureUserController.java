package com.duosoft.ipas.controller.admin.structure;

import bg.duosoft.ipas.core.model.structure.OfficeStructureId;
import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.structure.GroupService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.webmodel.structure.TransferUserRequestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping(value = "/structure/user")
@PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureView.code())")
public class StructureUserController extends StructureControllerBase {
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;

    @InitBinder
    public void emptyStringToNullBinder(WebDataBinder binder) {
        // tell spring to set empty values as null instead of empty string.
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

    }

    @RequestMapping("/transfer")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String transferUsers(@RequestParam("data") String data, Model model) {
        TransferUserRequestData transferUserData = JsonUtil.readJson(data, TransferUserRequestData.class);
        try {
            userService.transferUsers(transferUserData.getUserIds(), transferUserData.getNewStructureId());
            return generateActionSuccessResponse(model);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String newUser(Model model) {
        generateEditModel(null, model, true);
        return "structure/edit/edit_user";
    }

    @GetMapping("/edit/{userId}")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String editUser(@PathVariable("userId") int userId, Model model) {
        User user = userService.getUser(userId);
        generateEditModel(user, model, false);
        return "structure/edit/edit_user";
    }
    @GetMapping("/view/{userId}")
    public String viewUser(@PathVariable("userId") int userId, Model model) {
        User user = userService.getUser(userId);
        generateEditModel(user, model, false);
        return "structure/edit/view_user";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String saveUser(@ModelAttribute User user, @RequestParam("isNew") boolean isNew, Model model, RedirectAttributes redirectAttributes) {
        try {
            int userId = userService.saveUser(user);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("structure.create.success", null, LocaleContextHolder.getLocale()));
            return "redirect:/structure/user/edit/" + userId;
        } catch (IpasValidationException e) {
            model.addAttribute("validationErrors", e.getErrors());
            generateEditModel(user, model, isNew);
            return "structure/edit/edit_user";
        }
    }

    @PostMapping(value = "/delete/{userId}")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String deleteUser(@PathVariable("userId") Integer userId, Model model) {
        try {
            userService.archiveUser(userService.getUser(userId));
            return generateActionSuccessResponse(model);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    @PostMapping(value = "/activate/{userId}")
    @PreAuthorize("hasRole(T(bg.duosoft.ipas.enums.security.SecurityRole).StructureEdit.code())")
    public String activateUser(@PathVariable("userId") Integer userId, Model model) {
        try {
            User user = userService.getUser(userId);
            user.setIndInactive(false);
            userService.saveUser(user);
            return generateActionSuccessResponse(model);
        } catch (IpasValidationException e) {
            return generateValidationErrorModalResponse(model, e);
        }
    }

    private void generateEditModel(User user, Model model, boolean isNew) {
        model.addAttribute("user", user);


        OfficeStructureId structureId = user == null ? null : new OfficeStructureId(user.getOfficeDivisionCode(), user.getOfficeDepartmentCode(), user.getOfficeSectionCode());
        generateStructureDivisionsModel(structureId, model);
        generateStructureDepartmentsModel(structureId, model);
        generateStructureSectionsModel(structureId, model);
        Map<Integer, String> groupsMap = groupService.getGroupsMap();
        model.addAttribute("groupsMap", groupsMap);
        model.addAttribute("isNew", isNew);
    }


}
