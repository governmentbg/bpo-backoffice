package com.duosoft.ipas.controller.ipobjects.marklike.mark.attachment;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.mark.*;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.mark.attachment.MarkImageValidator;
import bg.duosoft.ipas.core.validation.mark.attachment.MarkAudioValidator;
import bg.duosoft.ipas.core.validation.mark.attachment.MarkVideoValidator;
import bg.duosoft.ipas.enums.AttachmentType;
import bg.duosoft.ipas.enums.MarkSignType;
import bg.duosoft.ipas.util.attachment.AttachmentUtils;
import bg.duosoft.ipas.util.mark.MarkSignDataAttachmentUtils;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/mark")
public class MarkAttachmentController {

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private MarkAudioValidator markSoundValidator;

    @Autowired
    private MarkImageValidator markImageValidator;

    @Autowired
    private MarkVideoValidator markVideoValidator;

    @RequestMapping(value = "/add-attachment")
    public String addAttachment(Model model, HttpServletRequest request,
                                @RequestParam String sessionIdentifier,
                                @RequestParam String signType,
                                @RequestParam String type) {
        List<CMarkAttachment> sessionMarkAttachments = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ATTACHMENTS, sessionIdentifier, request);

        CMarkAttachment attachment = new CMarkAttachment();
        attachment.setAttachmentType(AttachmentType.valueOf(type));
        if (attachment.getAttachmentType() == AttachmentType.IMAGE || attachment.getAttachmentType() == AttachmentType.VIDEO) {
            attachment.setViennaClassList(new ArrayList<>());
        }
        sessionMarkAttachments.add(attachment);

        CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
        model.addAttribute("mark", createTemporaryMark(signType, mark, sessionMarkAttachments));
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        return "ipobjects/marklike/mark/details/attachment_data :: attachments";
    }

    @RequestMapping(value = "/delete-mark-attachment")
    public String deleteAttachment(Model model, HttpServletRequest request,
                                       @RequestParam String sessionIdentifier,
                                       @RequestParam String signType,
                                       @RequestParam String type,
                                       @RequestParam Integer attachmentIndex) {
        CMark mark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
        List<CMarkAttachment> sessionMarkAttachments = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ATTACHMENTS, sessionIdentifier, request);
        AttachmentType attachmentType = AttachmentType.valueOf(type);
        switch (attachmentType) {
            case IMAGE:
                List<CMarkAttachment> images = MarkSignDataAttachmentUtils.selectImagesFromAttachments(sessionMarkAttachments);
                sessionMarkAttachments.remove(images.get(attachmentIndex));
                break;
            case AUDIO:
                List<CMarkAttachment> sounds = MarkSignDataAttachmentUtils.selectAudiosFromAttachments(sessionMarkAttachments);
                sessionMarkAttachments.remove(sounds.get(attachmentIndex));
                break;
            case VIDEO:
                List<CMarkAttachment> videos = MarkSignDataAttachmentUtils.selectVideosFromAttachments(sessionMarkAttachments);
                sessionMarkAttachments.remove(videos.get(attachmentIndex));
                break;
        }
        model.addAttribute("mark", createTemporaryMark(signType, mark, sessionMarkAttachments));
        model.addAttribute("sessionObjectIdentifier", sessionIdentifier);
        return "ipobjects/marklike/mark/details/attachment_data :: attachments";
    }

    @RequestMapping(value = "/validate-uploaded-logo")
    public String validateUploadedLogo(Model model, HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(required = false) String sessionIdentifier,
                                       @RequestParam Integer attachmentIndex,
                                       @RequestParam(value = "uploadMarkLogo") MultipartFile uploadFile) {

        return validateUploadedAttachment(model, request, response, attachmentIndex, sessionIdentifier, uploadFile, AttachmentType.IMAGE);
    }

    @RequestMapping(value = "/validate-uploaded-audio")
    public String validateUploadedAudio(Model model, HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam Integer attachmentIndex,
                                        @RequestParam(required = false) String sessionIdentifier,
                                        @RequestParam(value = "uploadMarkAudio") MultipartFile uploadFile) {
        return validateUploadedAttachment(model, request, response, attachmentIndex, sessionIdentifier, uploadFile, AttachmentType.AUDIO);
    }

    @RequestMapping(value = "/validate-uploaded-video")
    public String validateUploadedVideo(Model model, HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam Integer attachmentIndex,
                                        @RequestParam(required = false) String sessionIdentifier,
                                        @RequestParam(value = "uploadMarkVideo") MultipartFile uploadFile) {
        return validateUploadedAttachment(model, request, response, attachmentIndex, sessionIdentifier, uploadFile, AttachmentType.VIDEO);
    }

    private CMark createTemporaryMark(@RequestParam String signType, CMark mark, List<CMarkAttachment> sessionMarkAttachments) {
        CMark tempMark = new CMark();
        tempMark.setFile(new CFile());
        tempMark.getFile().setFileId(mark.getFile().getFileId());
        tempMark.setSignData(new CSignData());
        tempMark.getSignData().setSignType(MarkSignType.selectByCode(signType));
        tempMark.getSignData().setAttachments(sessionMarkAttachments);
        return tempMark;
    }

    private String validateUploadedAttachment(Model model, HttpServletRequest request, HttpServletResponse response,
                                              Integer attachmentIndex,
                                              String sessionIdentifier,
                                              MultipartFile uploadFile,
                                              AttachmentType attachmentType) {
        List<CMarkAttachment> sessionMarkAttachments = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ATTACHMENTS, sessionIdentifier, request);
        IpasValidator<CMarkAttachment> validator;
        List<CMarkAttachment> attachments;
        String url;
        String errorId;
        String successPage;
        switch (attachmentType) {
            case IMAGE:
                validator = markImageValidator;
                attachments = MarkSignDataAttachmentUtils.selectImagesFromAttachments(sessionMarkAttachments);
                url = CoreUtils.generateContextUrl(request) + "/mark/file/image";
                errorId = "mark-imageFile-" + attachmentIndex;
                successPage = "ipobjects/marklike/mark/details/image/img :: tag";
                break;
            case AUDIO:
                validator = markSoundValidator;
                attachments = MarkSignDataAttachmentUtils.selectAudiosFromAttachments(sessionMarkAttachments);
                url = CoreUtils.generateContextUrl(request) + "/mark/file/audio";
                errorId = "mark-audioFile-" + attachmentIndex;
                successPage = "ipobjects/marklike/mark/details/audio/audio :: tag";
                break;
            case VIDEO:
                validator = markVideoValidator;
                attachments = MarkSignDataAttachmentUtils.selectVideosFromAttachments(sessionMarkAttachments);
                url = CoreUtils.generateContextUrl(request) + "/mark/file/video";
                errorId = "mark-videoFile-" + attachmentIndex;
                successPage = "ipobjects/marklike/mark/details/video/video :: tag";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + attachmentType);
        }


        List<ValidationError> errors;
        CMarkAttachment newAttachment = new CMarkAttachment();
        String originalFilename = uploadFile.getOriginalFilename();
        try {
            if (Objects.nonNull(originalFilename)) {
                byte[] bytes = uploadFile.getBytes();
                newAttachment.setData(bytes);
                newAttachment.setMimeType(AttachmentUtils.getContentType(bytes));
                errors = validator.validate(newAttachment, attachmentIndex, Integer.valueOf(yamlConfig.getVideoMaxSize()));
            } else
                throw new RuntimeException("File name is empty !");
        } catch (IOException e) {
            throw new RuntimeException("Upload of file failed with IOException. Filename: " + originalFilename, e);
        }

        if (CollectionUtils.isEmpty(errors)) {
            CMarkAttachment attachment = attachments.get(attachmentIndex);
            if (Objects.isNull(attachment)) {
                throw new RuntimeException("Cannot find mark attachment with index " + attachmentIndex);
            }
            attachment.setData(newAttachment.getData());
            attachment.setMimeType(newAttachment.getMimeType());
            attachment.setAttachmentType(attachmentType);

            model.addAttribute("sessionIdentifier", sessionIdentifier);
            model.addAttribute("attachmentIndex", attachmentIndex);
            model.addAttribute("url", url);
            response.addHeader("Attachment-Index", String.valueOf(attachmentIndex));
            return successPage;
        }
        model.addAttribute("errors", errors);
        model.addAttribute("id", errorId);
        response.addHeader("Attachment-Index", String.valueOf(attachmentIndex));
        return "base/validation :: validation-message";
    }
}
