package com.duosoft.ipas.controller.ipobjects.marklike.acp;

import bg.duosoft.ipas.core.model.acp.CAcpViolationPlace;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.util.DefaultValue;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import liquibase.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/acp/violation-places")
public class AcpViolationPlacesController {

    @PostMapping("/edit")
    public String edit(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam String sessionIdentifier) {
        CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
        if (!isCancel) {
            List<CAcpViolationPlace> violationPlaces = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ACP_VIOLATION_PLACES, sessionIdentifier, request);
            mark.setAcpViolationPlaces(violationPlaces);
        }
        model.addAttribute("mark", mark);
        model.addAttribute("violationPlaces", mark.getAcpViolationPlaces());
        ;
        MarkSessionUtils.removeAcpViolationPlacesPanelSessionObjects(request, sessionIdentifier);
        return "ipobjects/marklike/acp/acp-violation-places/acp_violation_places_main_panel :: main_panel";
    }


    @PostMapping("/add-violation")
    public String add(HttpServletRequest request, Model model, @RequestParam String description, @RequestParam String sessionIdentifier) {
        List<CAcpViolationPlace> violationPlaces = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ACP_VIOLATION_PLACES, sessionIdentifier, request);

        List<ValidationError> errors = new ArrayList<>();
        if (StringUtils.isEmpty(description)) {
            errors.add(ValidationError.builder().pointer("violation.place.errors").messageCode("acp.violation.place.empty.description.error").build());
        }

        if (CollectionUtils.isEmpty(errors)) {
            CAcpViolationPlace newViolationPlace = new CAcpViolationPlace();
            newViolationPlace.setDescription(description);
            if (CollectionUtils.isEmpty(violationPlaces)) {
                newViolationPlace.setId(DefaultValue.INCREMENT_VALUE);
            } else {
                CAcpViolationPlace violationPlaceWithMaxId = violationPlaces.stream()
                        .max(Comparator.comparing(r -> r.getId())).orElse(null);
                newViolationPlace.setId(violationPlaceWithMaxId.getId() + DefaultValue.INCREMENT_VALUE);
            }
            violationPlaces.add(newViolationPlace);
        }

        model.addAttribute("validationErrors", errors);
        model.addAttribute("violationPlaces", violationPlaces);
        return "ipobjects/marklike/acp/acp-violation-places/acp_violation_places_table :: table";
    }


    @PostMapping("/delete-violation")
    public String delete(HttpServletRequest request, Model model, @RequestParam Integer id, @RequestParam String sessionIdentifier) {
        List<CAcpViolationPlace> violationPlaces = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ACP_VIOLATION_PLACES, sessionIdentifier, request);
        violationPlaces.removeIf(r -> r.getId().equals(id));
        model.addAttribute("violationPlaces", violationPlaces);
        return "ipobjects/marklike/acp/acp-violation-places/acp_violation_places_table :: table";
    }

}
