package com.duosoft.ipas.controller.ipobjects.patentlike.common;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CPatentAttachment;
import bg.duosoft.ipas.core.model.util.CPdfAttachmentBookmark;
import bg.duosoft.ipas.core.service.PdfBookmarkService;
import bg.duosoft.ipas.core.service.patent.PatentAttachmentService;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.patent.attachments.AttachmentBookmarksValidator;
import bg.duosoft.ipas.util.attachment.AttachmentUtils;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/patent-like/attachment")
public class AttachmentsController {
    private final String attachmentsView = "ipobjects/patentlike/common/details/patent_attachments :: patent-attachments";
    private final String bookmarksModalView = " ipobjects/patentlike/common/details/patent_attachment_bookmarks_modal :: bookmarks-modal";

    @Autowired
    private PatentAttachmentService patentAttachmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IpasValidatorCreator validatorCreator;
    @Autowired
    private PdfBookmarkService pdfBookmarkService;

    @PostMapping("/add")
    public String addAttachment(HttpServletRequest request, Model model,
                                @RequestParam String sessionIdentifier) {

        CPatentAttachment newCPatentAttachment = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_NEW_ATTACHMENT, sessionIdentifier, request);
        List<CPatentAttachment> patentAttachments = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_ATTACHMENTS, sessionIdentifier, request);
        patentAttachments.add(newCPatentAttachment);
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, PatentSessionObject.SESSION_PATENT_NEW_ATTACHMENT);
        return initsOnAttachmentsViewReturn(model, request, sessionIdentifier, patentAttachments);
    }

    @PostMapping("/delete")
    public String deleteAttachment(HttpServletRequest request, Model model,
                                   @RequestParam String sessionIdentifier,
                                   @RequestParam Integer id,
                                   @RequestParam Integer attachmentTypeId) {
        List<CPatentAttachment> patentAttachments = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_ATTACHMENTS, sessionIdentifier, request);
        patentAttachments.removeIf(r -> r.getId().equals(id) && r.getAttachmentType().getId().equals(attachmentTypeId));
        return initsOnAttachmentsViewReturn(model, request, sessionIdentifier, patentAttachments);
    }

    @RequestMapping(value = "/content")
    public void loadPatentAttachment(HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam String sessionIdentifier,
                                     @RequestParam Integer id,
                                     @RequestParam Integer attachmentTypeId) {

        List<CPatentAttachment> patentAttachments = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_ATTACHMENTS, sessionIdentifier, request);
        CPatent sessionPatent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        CFileId fileId = sessionPatent.getFile().getFileId();
        if (CollectionUtils.isEmpty(patentAttachments)) {
            patentAttachments = sessionPatent.getPatentDetails().getPatentAttachments();
        }

        CPatentAttachment patentAttachment = patentAttachments.stream()
                .filter(r -> r.getId().equals(id) && r.getAttachmentType().getId().equals(attachmentTypeId))
                .findFirst()
                .orElse(null);
        if (!patentAttachment.isContentLoaded()) {
            patentAttachment = patentAttachmentService.findPatentAttachment(id, attachmentTypeId, fileId, true);
        }
        String mediaType = null;
        if (patentAttachment.getAttachmentType().getAttachmentExtension().equals("xml")) {
            mediaType = MediaType.APPLICATION_XML_VALUE;
        }
        if (patentAttachment.getAttachmentType().getAttachmentExtension().equals("pdf")) {
            mediaType = MediaType.APPLICATION_PDF_VALUE;
        }
        AttachmentUtils.writeData(response, patentAttachment.getAttachmentContent(), mediaType, patentAttachment.getAttachmentName());
    }


    @PostMapping("/bookmarks/open-edit-modal")
    public String openEditBookmarkModal(HttpServletRequest request, Model model,
                                        @RequestParam String sessionIdentifier,
                                        @RequestParam Integer id,
                                        @RequestParam Integer attachmentTypeId) {
        CPatent sessionPatent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        List<CPatentAttachment> patentAttachments = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_ATTACHMENTS, sessionIdentifier, request);
        CPatentAttachment patentAttachment = patentAttachments.stream()
                .filter(r -> r.getId().equals(id) && r.getAttachmentType().getId().equals(attachmentTypeId))
                .findFirst()
                .orElse(null);

        List<CPdfAttachmentBookmark> bookmarks = patentAttachmentService.initAttachmentBookmarks(patentAttachment, sessionPatent.getFile().getFileId());
        return initsOnOpenBookmarkModal(model, bookmarks, id, attachmentTypeId);
    }


    @PostMapping("/bookmarks/edit")
    public String editBookmarks(HttpServletRequest request, Model model,
                                @RequestParam String sessionIdentifier,
                                @RequestParam Integer id,
                                @RequestParam Integer attachmentTypeId, @RequestParam String editedBookmarks) throws IOException {
        CPatent sessionPatent = PatentSessionUtils.getSessionPatent(request, sessionIdentifier);
        CFileId fileId = sessionPatent.getFile().getFileId();
        List<CPdfAttachmentBookmark> bookmarks = objectMapper.readValue(editedBookmarks, new TypeReference<List<CPdfAttachmentBookmark>>() {
        });

        if (CollectionUtils.isEmpty(bookmarks)) {
            throw new RuntimeException("No configuration related to bookmarks");
        }

        List<CPatentAttachment> patentAttachments = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_ATTACHMENTS, sessionIdentifier, request);
        CPatentAttachment patentAttachment = patentAttachments.stream()
                .filter(r -> r.getId().equals(id) && r.getAttachmentType().getId().equals(attachmentTypeId))
                .findFirst()
                .orElse(null);

        List<ValidationError> errors = validateBookmarksOnEdit(patentAttachment, fileId, bookmarks);

        if (CollectionUtils.isEmpty(errors)) {
            patentAttachmentService.updateBookmarks(fileId, patentAttachment, bookmarks);
            return initsOnAttachmentsViewReturn(model, request, sessionIdentifier, patentAttachments);
        } else {
            model.addAttribute("validationErrors",errors);
            return initsOnOpenBookmarkModal(model,bookmarks,id,attachmentTypeId);
        }

    }

    private List<ValidationError> validateBookmarksOnEdit(CPatentAttachment patentAttachment, CFileId fileId, List<CPdfAttachmentBookmark> bookmarks) {
        IpasTwoArgsValidator<Integer, List<CPdfAttachmentBookmark>> validator = validatorCreator.createTwoArgsValidator(false, AttachmentBookmarksValidator.class);
        byte[] content = patentAttachmentService.getAttachmentContent(patentAttachment, fileId);
        Integer pagesCount = pdfBookmarkService.getPagesCount(content);
        return validator.validate(pagesCount, bookmarks);
    }

    private String initsOnAttachmentsViewReturn(Model model, HttpServletRequest request, String sessionIdentifier, List<CPatentAttachment> patentAttachments) {
        model.addAttribute("panelPrefix", CoreUtils.getUrlPrefixByFileType(PatentSessionUtils.getSessionPatent(request, sessionIdentifier).getFile().getFileId().getFileType()));
        model.addAttribute("patentAttachments", patentAttachments);
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        return attachmentsView;
    }

    private String initsOnOpenBookmarkModal(Model model, List<CPdfAttachmentBookmark> bookmarks, Integer id, Integer attachmentTypeId) {
        model.addAttribute("bookmarks", bookmarks);
        model.addAttribute("id", id);
        model.addAttribute("attachmentTypeId", attachmentTypeId);
        return bookmarksModalView;
    }

}
