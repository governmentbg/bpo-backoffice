package com.duosoft.ipas.controller.ipobjects.offidoc;

import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.offidoc.COffidocPublishedDecision;
import bg.duosoft.ipas.core.service.offidoc.OffidocPublishedDecisionService;
import bg.duosoft.ipas.core.validation.common.PdfFormatValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.util.attachment.AttachmentUtils;
import com.duosoft.ipas.util.session.offidoc.OffidocSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/offidoc/published-decision")
public class OffidocPublishedDecisionController {

    @Autowired
    private PdfFormatValidator pdfFormatValidator;

    @Autowired
    private OffidocPublishedDecisionService offidocPublishedDecisionService;


    @RequestMapping(value = "/upload")
    public String upload(Model model, HttpServletRequest request,
                         @RequestParam(value = "uploadDecisionForPublishing") MultipartFile uploadFile,
                         @RequestParam String sessionIdentifier) throws IOException {
        List<ValidationError> errors = new ArrayList<>();
        COffidoc sessionOffidoc = OffidocSessionUtils.getSessionOffidoc(request, sessionIdentifier);
        boolean validPdf = pdfFormatValidator.isValidPdf(uploadFile.getBytes());
        if (!validPdf) {
            errors.add(ValidationError.builder().pointer("offidoc.published.decision").messageCode("invalid.pdf.attachment.data").build());
        } else {

            if (Objects.isNull(sessionOffidoc.getPublishedDecision())){
                sessionOffidoc.setPublishedDecision(new COffidocPublishedDecision());
            }
            sessionOffidoc.getPublishedDecision().setAttachmentContent(uploadFile.getBytes());
            sessionOffidoc.getPublishedDecision().setAttachmentName(uploadFile.getOriginalFilename());
            offidocPublishedDecisionService.update(sessionOffidoc.getOffidocId(),sessionOffidoc.getPublishedDecision());
        }

        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        model.addAttribute("offidoc", sessionOffidoc);
        model.addAttribute("validationErrors", errors);
        return "ipobjects/offidoc/published-decision/published_decision_panel :: panel";
    }


    @RequestMapping(value = "/download")
    public void download(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam String sessionIdentifier) {

        COffidoc sessionOffidoc = OffidocSessionUtils.getSessionOffidoc(request, sessionIdentifier);
        byte[] content = null;
        if (Objects.isNull(sessionOffidoc.getPublishedDecision().getAttachmentContent())) {
            content = offidocPublishedDecisionService.findById(sessionOffidoc.getOffidocId(), true).getAttachmentContent();
        } else {
            content = sessionOffidoc.getPublishedDecision().getAttachmentContent();
        }

        AttachmentUtils.writeData(response, content, MediaType.APPLICATION_PDF.toString(), sessionOffidoc.getPublishedDecision().getAttachmentName());
    }


    @RequestMapping(value = "/delete")
    public String delete(Model model, HttpServletRequest request, HttpServletResponse response,
                         @RequestParam String sessionIdentifier) {
        COffidoc sessionOffidoc = OffidocSessionUtils.getSessionOffidoc(request, sessionIdentifier);
        sessionOffidoc.setPublishedDecision(null);
        offidocPublishedDecisionService.delete(sessionOffidoc.getOffidocId());
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        model.addAttribute("offidoc", sessionOffidoc);
        model.addAttribute("validationErrors", null);
        return "ipobjects/offidoc/published-decision/published_decision_panel :: panel";
    }


}
