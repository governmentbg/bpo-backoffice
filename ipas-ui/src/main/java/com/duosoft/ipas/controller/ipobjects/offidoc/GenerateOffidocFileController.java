package com.duosoft.ipas.controller.ipobjects.offidoc;

import bg.duosoft.abdocs.model.DocFile;
import bg.duosoft.abdocs.model.DocFileVisibility;
import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.offidoc.COffidocTypeStaticTemplate;
import bg.duosoft.ipas.core.model.offidoc.COffidocTypeTemplate;
import bg.duosoft.ipas.core.service.offidoc.OffidocService;
import bg.duosoft.ipas.properties.PropertyAccess;
import bg.duosoft.ipas.util.attachment.AttachmentUtils;
import bg.duosoft.ipas.util.offidoc.OffidocUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.offidoc.OffidocSessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/offidoc/generate-file")
public class GenerateOffidocFileController extends BaseOffidocFileController {

    private final AbdocsService abdocsService;
    private final MessageSource messageSource;
    private final PropertyAccess propertyAccess;
    private final OffidocService offidocService;

    @PostMapping
    public String generateOffidocFile(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model, @RequestParam String templateName, @RequestParam String sessionIdentifier) {
        COffidoc sessionOffidoc = OffidocSessionUtils.getSessionOffidoc(request, sessionIdentifier);
        List<COffidocTypeTemplate> templates = sessionOffidoc.getOffidocType().getTemplates();
        COffidocTypeTemplate template = templates.stream().filter(s -> s.getNameWFile().equalsIgnoreCase(templateName)).findFirst().orElse(null);
        if (Objects.nonNull(template) && !StringUtils.isEmpty(template.getNameWFile())) {
            try {
                Map<String, byte[]> generatedFiles = offidocService.generateOffidocFilesFromTemplate(sessionOffidoc.getActionId(), Collections.singletonList(template.getNameWFile()));
                byte[] bytes = generatedFiles.get(template.getNameWFile());
                String mimeType = AttachmentUtils.tikaMimeTypeConvertMap.get(AttachmentUtils.getContentType(bytes));
                String fileName = OffidocUtils.selectOffidocFileName(sessionOffidoc, template) + AttachmentUtils.contentTypeToExtensionMap.get(mimeType);

                Integer abdocsDocumentId = sessionOffidoc.getAbdocsDocumentId();
                if (Objects.isNull(abdocsDocumentId)) {
                    throw new RuntimeException("Cannot save offidoc, because abdocs document id is empty ! Offidoc ID: " + sessionOffidoc.getOffidocId());
                }

                Document document = abdocsService.selectDocumentById(abdocsDocumentId);
                super.saveFile(bytes, fileName, abdocsDocumentId, document);
                redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("offidoc.generate.new.file", new String[]{template.getNameWFile()}, LocaleContextHolder.getLocale()));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("offidoc.generate.file.error", new String[]{template.getNameWFile()}, LocaleContextHolder.getLocale()));
            }
        }

        String fullSessionIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
        return redirectToOpenExistingObject(redirectAttributes, fullSessionIdentifier, true, null);
    }

    @PostMapping("/static")
    public String generateStaticOffidocFile(HttpServletRequest request, RedirectAttributes redirectAttributes, @RequestParam String templateName, @RequestParam String sessionIdentifier) {
        COffidoc sessionOffidoc = OffidocSessionUtils.getSessionOffidoc(request, sessionIdentifier);
        List<COffidocTypeStaticTemplate> templates = sessionOffidoc.getOffidocType().getStaticTemplates();
        COffidocTypeStaticTemplate template = templates.stream().filter(s -> s.getStaticFileName().equalsIgnoreCase(templateName)).findFirst().orElse(null);
        if (Objects.nonNull(template) && !StringUtils.isEmpty(template.getStaticFileName())) {
            String staticFileName = template.getStaticFileName();
            try {
                String templatesDir = propertyAccess.getOffidocStaticTemplatesDirectory();
                if (StringUtils.isEmpty(templatesDir)) {
                    throw new RuntimeException("Cannot generate static file because offidoc static templates directory is not configured ! Offidoc session id: " + sessionIdentifier);
                }

                String fileSystemPath = templatesDir + File.separator + staticFileName;
                byte[] bytes = AttachmentUtils.readFileSystemResource(fileSystemPath);

                Integer abdocsDocumentId = sessionOffidoc.getAbdocsDocumentId();
                if (Objects.isNull(abdocsDocumentId)) {
                    throw new RuntimeException("Cannot save offidoc, because abdocs document id is empty ! Offidoc ID: " + sessionOffidoc.getOffidocId());
                }

                Document document = abdocsService.selectDocumentById(abdocsDocumentId);
                super.saveFile(bytes, staticFileName, abdocsDocumentId, document);
                redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("offidoc.attach.file.success", new String[]{staticFileName}, LocaleContextHolder.getLocale()));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("offidoc.attach.file.error", new String[]{staticFileName}, LocaleContextHolder.getLocale()));
            }
        }

        String fullSessionIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
        return redirectToOpenExistingObject(redirectAttributes, fullSessionIdentifier, true, null);
    }

    public static String redirectToOpenExistingObject(RedirectAttributes redirectAttributes, String sessionObjectIdentifier, boolean openExistingObject, String scrollToPanel) {
        if (!StringUtils.isEmpty(scrollToPanel))
            redirectAttributes.addFlashAttribute("scrollToPanel", scrollToPanel);

        redirectAttributes.addAttribute("sessionObject", sessionObjectIdentifier);
        redirectAttributes.addAttribute("openExistingObject", openExistingObject);
        return "redirect:/session/open-existing-object";
    }

    @Override
    protected AbdocsService getAbdocsService() {
        return abdocsService;
    }

    @Override
    protected DocFileVisibility getFileVisibility() {
        return DocFileVisibility.PublicAttachedFile;
    }
}
