package com.duosoft.ipas.controller.home;


import bg.duosoft.ipas.util.filter.MyObjectsFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/my-objects")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).IpObjectsMyObjectsList.code())")
public class MyObjectsController extends MyObjectsBaseController {


    @Override
    protected void fillGenericAttributesInitActions(Model model, MyObjectsFilter filter) {
        model.addAttribute("myObjectsParentUrl","my-objects");
    }
}
