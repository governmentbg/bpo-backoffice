package com.duosoft.ipas.controller.ipobjects.marklike.acp;

import bg.duosoft.ipas.core.model.acp.CAcpTakenItem;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.service.nomenclature.AcpTakenItemStorageService;
import bg.duosoft.ipas.core.service.nomenclature.AcpTakenItemTypeService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.mark.AcpTakenItemValidator;
import bg.duosoft.ipas.util.DefaultValue;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/acp/taken-items")
public class AcpTakenItemsController {
    @Autowired
    private AcpTakenItemTypeService acpTakenItemTypeService;
    @Autowired
    private AcpTakenItemStorageService acpTakenItemStorageService;
    @Autowired
    private IpasValidatorCreator validatorCreator;

    private final String panelPage = "ipobjects/marklike/acp/acp-taken-items/taken_items_main_panel :: main_panel";
    private final String modalPage = "ipobjects/marklike/acp/acp-taken-items/taken_item_modal :: modal";

    @PostMapping("/edit")
    public String edit(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam String sessionIdentifier) {
        CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
        List<CAcpTakenItem> items = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ACP_TAKEN_ITEMS, sessionIdentifier, request);
        if (!isCancel) {
            mark.setAcpTakenItems(items);
        }
        model.addAttribute("acpTakenItems", mark.getAcpTakenItems());
        return panelPage;
    }

    @PostMapping("/delete")
    public String delete(HttpServletRequest request, Model model,
                         @RequestParam String sessionIdentifier,
                         @RequestParam Integer id) {
        List<CAcpTakenItem> items = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ACP_TAKEN_ITEMS, sessionIdentifier, request);
        items.removeIf(r -> r.getId().equals(id));
        model.addAttribute("acpTakenItems", items);
        return panelPage;
    }

    @PostMapping("/open-modal")
    public String openModal(HttpServletRequest request, Model model,
                            @RequestParam String sessionIdentifier,
                            @RequestParam(required = false) Integer id) {
        List<CAcpTakenItem> items = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ACP_TAKEN_ITEMS, sessionIdentifier, request);
        CAcpTakenItem item = new CAcpTakenItem();
        if (Objects.nonNull(id)) {
            item = items.stream().filter(r -> Objects.equals(r.getId(), id)).findFirst().orElse(null);
        }
        model.addAttribute("item", item);
        model.addAttribute("types", acpTakenItemTypeService.findAll());
        model.addAttribute("storages", acpTakenItemStorageService.findAll());
        return modalPage;
    }


    private List<ValidationError> validateOnEditTakenItem(CAcpTakenItem item) {
        IpasValidator<CAcpTakenItem> validator = validatorCreator.create(false, AcpTakenItemValidator.class);
        return validator.validate(item);
    }

    @PostMapping("/edit-list")
    public String editAcpTakenItemsList(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier, @RequestParam(required = false) Integer id,
                                        @RequestParam(required = false) Integer type, @RequestParam(required = false) Integer storage,
                                        @RequestParam(required = false) String typeDescription, @RequestParam(required = false) String storageDescription,
                                        @RequestParam(required = false) Integer count, @RequestParam(required = false) Boolean forDestruction,
                                        @RequestParam(required = false) Boolean returned, @RequestParam(required = false) Boolean inStock) {
        List<CAcpTakenItem> items = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ACP_TAKEN_ITEMS, sessionIdentifier, request);
        CAcpTakenItem item = new CAcpTakenItem(id, Objects.nonNull(type) ? acpTakenItemTypeService.findById(type) : null, StringUtils.hasText(typeDescription) ? typeDescription : null, count, Objects.nonNull(storage) ? acpTakenItemStorageService.findById(storage) : null, StringUtils.hasText(storageDescription) ? storageDescription : null, forDestruction, returned, inStock);
        List<ValidationError> validationErrors = validateOnEditTakenItem(item);
        if (!CollectionUtils.isEmpty(validationErrors)) {
            model.addAttribute("validationErrors", validationErrors);
            model.addAttribute("item", item);
            model.addAttribute("types", acpTakenItemTypeService.findAll());
            model.addAttribute("storages", acpTakenItemStorageService.findAll());
            return modalPage;
        } else {
            if (Objects.nonNull(id)) {
                CAcpTakenItem existingItem = items.stream().filter(r -> Objects.equals(r.getId(), id)).findFirst().orElse(null);
                int indexOfOldItem = items.indexOf(existingItem);
                items.set(indexOfOldItem, item);
            } else {
                CAcpTakenItem itemWithMaxId = items.stream().max(Comparator.comparing(r -> r.getId())).orElse(null);
                if (Objects.isNull(itemWithMaxId)) {
                    item.setId(DefaultValue.INCREMENT_VALUE);
                } else {
                    item.setId(itemWithMaxId.getId() + DefaultValue.INCREMENT_VALUE);
                }
                items.add(item);
            }
            model.addAttribute("acpTakenItems", items);
            return panelPage;
        }
    }

}
