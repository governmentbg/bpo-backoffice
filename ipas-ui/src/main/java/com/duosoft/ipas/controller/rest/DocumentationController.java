package com.duosoft.ipas.controller.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocumentationController  {

    @GetMapping("/api/docs")
    public String redirectToSwaggerPage() {
        return "redirect:/swagger-ui.html";
    }

}