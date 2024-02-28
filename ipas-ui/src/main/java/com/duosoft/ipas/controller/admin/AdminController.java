package com.duosoft.ipas.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).AdminModule.code())")
public class AdminController {

    @GetMapping(value = {"", "/", "/home/"})
    public String redirectToHomePage(Model model) {
        return "redirect:/admin/home";
    }

    @GetMapping("/home")
    public String viewAdminPage() {
        return "admin/admin_page";
    }

}
