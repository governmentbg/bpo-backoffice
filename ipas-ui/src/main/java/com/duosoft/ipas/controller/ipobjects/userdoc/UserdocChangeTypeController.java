package com.duosoft.ipas.controller.ipobjects.userdoc;

import bg.duosoft.ipas.core.model.document.CExtraData;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPersonRole;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import com.duosoft.ipas.util.RedirectUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/userdoc")
public class UserdocChangeTypeController {

    @Autowired
    private UserdocTypesService userdocTypesService;

    @PostMapping("/change-userdoc-type")
    public String changeUserdocType(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model,
                                    @RequestParam(required = false) List<String> editedPanels,
                                    @RequestParam Boolean isCancel,
                                    @RequestParam(required = false) String userdocType,
                                    @RequestParam String sessionIdentifier) {
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        if (isCancel) {
            model.addAttribute("userdoc", sessionUserdoc);
            model.addAttribute("userdocTypeMap", userdocTypesService.selectAllowedUserdocTypesForChange(sessionUserdoc));
            return "ipobjects/userdoc/type/userdoc_type_panel :: panel";
        } else {
            CUserdocType sessionUserdocType = sessionUserdoc.getUserdocType();
            if (!(sessionUserdocType.getUserdocType().equalsIgnoreCase(userdocType))) {
                updateSessionObject(userdocType, sessionUserdoc);
            }
            redirectAttributes.addFlashAttribute("userdoc", sessionUserdoc);
            redirectAttributes.addFlashAttribute("sessionObjectIdentifier", sessionIdentifier);
            redirectAttributes.addFlashAttribute("editedPanels", editedPanels);
            return RedirectUtils.redirectToObjectViewPage(UserdocUtils.convertDocumentIdToString(sessionUserdoc.getDocumentId()));
        }
    }

    private void updateSessionObject(String userdocType, CUserdoc sessionUserdoc) {
        CUserdocType cUserdocType = userdocTypesService.selectUserdocTypeById(userdocType);
        if (Objects.isNull(cUserdocType)) {
            throw new RuntimeException("Cannot find userdoc type: " + userdocType);
        } else {
            sessionUserdoc.setUserdocType(cUserdocType);
            sessionUserdoc.getDocument().setExtraData(new CExtraData());
            sessionUserdoc.setUserdocExtraData(new ArrayList<>());
            sessionUserdoc.setIndCompulsoryLicense(null);
            sessionUserdoc.setIndExclusiveLicense(null);
            sessionUserdoc.getProtectionData().setNiceClassList(new ArrayList<>());
            sessionUserdoc.setUserdocRootGrounds(new ArrayList<>());
            UserdocUtils.removePersonsWithRolesWhichIsNotInConfiguration(sessionUserdoc, cUserdocType);
        }
    }

}
