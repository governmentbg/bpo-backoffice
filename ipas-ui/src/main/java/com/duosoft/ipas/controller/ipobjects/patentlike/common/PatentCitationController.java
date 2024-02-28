package com.duosoft.ipas.controller.ipobjects.patentlike.common;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CPatentCitation;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.patent.citations.PatentCitationValidator;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/patent-like/citations")
public class PatentCitationController {

    private final String patentCitationsModalView = "ipobjects/patentlike/common/citations/citation_form_modal :: patent-citation-form-modal";
    private final String patentCitationsView = "ipobjects/patentlike/common/citations/citations_panel :: patent-citations";

    private final PatentCitationValidator patentCitationValidator;

    @PostMapping("/edit-panel")
    public String panelEdit(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam String sessionIdentifier) {

        CPatent patent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        if (!isCancel) {
            List<CPatentCitation> sessionCitations = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_CITATIONS, sessionIdentifier, request);
            if (!CollectionUtils.isEmpty(sessionCitations)) {
                patent.getTechnicalData().setCitationList(sessionCitations);
            } else {
                patent.getTechnicalData().setCitationList(new ArrayList<>());
            }
        }
        model.addAttribute("citationsList", patent.getTechnicalData().getCitationList());
        PatentSessionUtils.removePatentCitationsSessionObjects(request, sessionIdentifier);
        return patentCitationsView;
    }

    @PostMapping("/edit-list")
    public String editCitationsList(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier,
                                    @RequestParam Integer refNumber, @RequestParam String refDescription, @RequestParam String refClaims, @RequestParam(required = false) Integer oldRefNumber) {
        List<CPatentCitation> citations = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_CITATIONS, sessionIdentifier, request);

        CPatentCitation editCitation = new CPatentCitation(refNumber, refDescription, StringUtils.hasText(refClaims) ? refClaims : null);
        List<ValidationError> errors = patentCitationValidator.validate(new CPatentCitation(refNumber, refDescription, refClaims), citations, oldRefNumber);
        if (!CollectionUtils.isEmpty(errors)) {
            model.addAttribute("validationErrors", errors);
            model.addAttribute("citation", editCitation);
            model.addAttribute("oldRefNumber",oldRefNumber);
            return patentCitationsModalView;
        }

        if (Objects.nonNull(oldRefNumber)) {
            citations.removeIf(citation -> citation.getRefNumber().equals(oldRefNumber));
        }
        citations.add(editCitation);
        citations.sort(Comparator.comparing(CPatentCitation::getRefNumber));
        
        model.addAttribute("citationsList", citations);
        return patentCitationsView;

    }

    @PostMapping("/open-modal")
    public String openModal(HttpServletRequest request, Model model,
                            @RequestParam String sessionIdentifier,
                            @RequestParam(required = false) Integer refNumber) {
        List<CPatentCitation> sessionCitations = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_CITATIONS, sessionIdentifier, request);

        model.addAttribute("oldRefNumber", refNumber);

        if (Objects.nonNull(refNumber)) {
            CPatentCitation editedCitation = sessionCitations.stream().filter(citation -> Objects.equals(citation.getRefNumber(), refNumber)).findFirst().orElse(null);
            model.addAttribute("citation", editedCitation);
            model.addAttribute("isNew", false);
        } else {
            model.addAttribute("citation", new CPatentCitation());
            model.addAttribute("isNew", true);
        }

        return patentCitationsModalView;
    }

    @PostMapping("/delete-citation")
    public String deletePatentCitation(HttpServletRequest request, Model model,
                                       @RequestParam String sessionIdentifier,
                                       @RequestParam Integer refNumber) {

        List<CPatentCitation> citations = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_CITATIONS, sessionIdentifier, request);
        CPatentCitation cDeletedCitation = citations.stream().filter(citation -> Objects.equals(citation.getRefNumber(), refNumber)).findFirst().orElse(null);
        if (!Objects.isNull(cDeletedCitation) && !CollectionUtils.isEmpty(citations)){
            citations.remove(cDeletedCitation);
        }
        model.addAttribute("citationsList", citations);
        return patentCitationsView;
    }
}
