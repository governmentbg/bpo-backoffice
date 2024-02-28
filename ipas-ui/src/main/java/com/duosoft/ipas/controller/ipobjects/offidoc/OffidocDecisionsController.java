package com.duosoft.ipas.controller.ipobjects.offidoc;

import bg.duosoft.abdocs.model.DocFileVisibility;
import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.decisiondesktop.CDecisionContext;
import bg.duosoft.ipas.core.model.decisiondesktop.CDecisionTemplate;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.integration.decisiondesktop.service.AdmintoolService;
import bg.duosoft.ipas.integration.decisiondesktop.service.DraftingEditorService;
import bg.duosoft.ipas.properties.PropertyAccess;
import bg.duosoft.ipas.util.security.SecurityUtils;
import com.duosoft.ipas.util.session.offidoc.OffidocSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 23.04.2021
 * Time: 11:16
 */
@Slf4j
@Controller
@RequestMapping("/offidoc/decisions")
public class OffidocDecisionsController extends BaseOffidocFileController {

    @Autowired
    private AdmintoolService admintoolService;

    @Autowired
    private DraftingEditorService draftingEditorService;

    @Autowired
    private PropertyAccess propertyAccess;

    @Autowired
    private AbdocsService abdocsService;


    @PostMapping("/load-data")
    public String loadDecisionsPanel(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier){
        String loggedUser = SecurityUtils.getLoggedUsername();
        COffidoc sessionOffidoc = OffidocSessionUtils.getSessionOffidoc(request, sessionIdentifier);
        List<CDecisionContext> contexts = draftingEditorService.getFilteredContexts(sessionOffidoc.getOffidocId(), loggedUser);
        List<String> usedTemplates = contexts.stream().map(c -> c.getTemplateId()).collect(Collectors.toList());
        List<CDecisionTemplate> templates = admintoolService.getTemplates(loggedUser, sessionOffidoc).stream().filter(
                template -> !usedTemplates.contains(template.getId())
        ).collect(Collectors.toList());
        model.addAttribute("templates", templates);
        model.addAttribute("contexts", contexts);
        model.addAttribute("draftingEditorAppUrl", propertyAccess.getDraftingEditorAppUrl());
        model.addAttribute("sessionIdentifier", sessionIdentifier);

        return "ipobjects/offidoc/decisions/decisions_panel :: decisions_panel";
    }

    @PostMapping("/create-context")
    public String createContext(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier, @RequestParam String templateId, @RequestParam String templateName){
        COffidoc sessionOffidoc = OffidocSessionUtils.getSessionOffidoc(request, sessionIdentifier);
        String result = draftingEditorService.createContext(templateId, templateName, sessionOffidoc, SecurityUtils.getLoggedUsername());
        return loadDecisionsPanel(request, model, sessionIdentifier);
    }

    @PostMapping("/delete-context")
    public String deleteContext(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier, @RequestParam String contextId){
        String loggedUser = SecurityUtils.getLoggedUsername();
        try {
            draftingEditorService.cleanDraftingEditor(contextId, loggedUser);
        } catch (Exception e){
            log.error("Error cleaning context, because letter does not yet exist");
            draftingEditorService.deleteContext(contextId, loggedUser);
        }
        return loadDecisionsPanel(request, model, sessionIdentifier);
    }

    @PostMapping(value = "/export-file/{fileType}", produces = MimeTypeUtils.TEXT_PLAIN_VALUE)
    public @ResponseBody String exportFileToAbdocs(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier,
                              @RequestParam String contextId, @RequestParam String templateName, @PathVariable String fileType){
        if(!fileType.equals("docx") && !fileType.equals("pdf")){
            throw new RuntimeException("Bad fileType path param");
        }
        COffidoc sessionOffidoc = OffidocSessionUtils.getSessionOffidoc(request, sessionIdentifier);
        String loggedUser = SecurityUtils.getLoggedUsername();
        Integer abdocsDocumentId = sessionOffidoc.getAbdocsDocumentId();
        if (Objects.isNull(abdocsDocumentId)) {
            throw new RuntimeException("Cannot save offidoc, because abdocs document id is empty ! Offidoc ID: " + sessionOffidoc.getOffidocId());
        }
        Document document = abdocsService.selectDocumentById(abdocsDocumentId);
        byte[] fileBytes = draftingEditorService.downloadLetterFile(fileType, contextId, loggedUser);
        super.saveFile(fileBytes, templateName+"."+fileType, abdocsDocumentId, document);

        return "ok";
    }

    @Override
    protected AbdocsService getAbdocsService() {
        return abdocsService;
    }

    @Override
    protected DocFileVisibility getFileVisibility() {
        return DocFileVisibility.PrivateAttachedFile;
    }
}
