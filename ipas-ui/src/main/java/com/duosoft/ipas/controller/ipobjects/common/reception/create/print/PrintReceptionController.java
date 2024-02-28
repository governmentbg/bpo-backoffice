package com.duosoft.ipas.controller.ipobjects.common.reception.create.print;

import bg.duosoft.abdocs.model.DocumentBarcodeData;
import bg.duosoft.abdocs.model.ElectronicServiceData;
import bg.duosoft.abdocs.service.AbdocsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/reception/print")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).ReceptionCreator.code())")
public class PrintReceptionController {

    @Autowired
    private AbdocsService abdocsServiceAdmin;

    @RequestMapping("/getData")
    public String getData(Model model, @RequestParam Integer id) {
        ElectronicServiceData serviceData = abdocsServiceAdmin.selectEServiceData(id);
        if (Objects.isNull(serviceData)) {
            throw new RuntimeException("Empty serviceData ! ID: " + id);
        }

        DocumentBarcodeData document = abdocsServiceAdmin.selectDocumentBarcodeDataById(id);
        if (Objects.isNull(document)) {
            throw new RuntimeException("Empty document ! ID: " + id);
        }

        model.addAttribute("serviceData", serviceData);
        model.addAttribute("document", document);
        return "ipobjects/common/reception/print/print-template :: template";
    }

}
