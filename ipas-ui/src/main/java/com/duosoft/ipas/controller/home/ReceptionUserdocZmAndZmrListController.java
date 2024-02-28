package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.enums.UserdocGroup;
import bg.duosoft.ipas.util.filter.ReceptionUserdocListFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
@RequestMapping("/reception-userdoc-zm-zmr")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).UserdocInternationalMarkReceptionsListZmAndZmr.code())")
public class ReceptionUserdocZmAndZmrListController extends ReceptionUserdocBaseControllerController{

    @Override
    protected void fillGenericAttributesInitActions(Model model, ReceptionUserdocListFilter filter) {
        filter.setUserdocTypeGroups(UserdocGroup.getAllWithoutMainAndCorrespGroup());
        model.addAttribute("myObjectsParentUrl","reception-userdoc-zm-zmr");
    }
}
