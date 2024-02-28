package com.duosoft.ipas.controller.ipobjects.marklike.mark.attachment;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CLogo;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.mark.CMarkAttachment;
import bg.duosoft.ipas.core.model.mark.CSignData;
import bg.duosoft.ipas.core.service.mark.MarkAttachmentService;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.util.attachment.AttachmentUtils;
import bg.duosoft.ipas.util.mark.MarkSignDataAttachmentUtils;
import com.duosoft.ipas.controller.ipobjects.common.attachment.FileController;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/mark/file")
public class MarkFileAttachmentController extends FileController {
    @Autowired
    private MarkService markService;

    @Autowired
    private MarkAttachmentService markAttachmentService;

    @RequestMapping(value = "/image")
    public void getImage(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam String sessionIdentifier,
                            @RequestParam Integer attachmentIndex) {
        CMark sessionMark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
        CSignData signData = sessionMark.getSignData();
        CFileId cFileId = CoreUtils.createCFileId(sessionMark.getFile().getFileId().createFilingNumber());

        List<CMarkAttachment> attachments = getAttachments(request, sessionIdentifier, signData);

        List<CMarkAttachment> markImages = MarkSignDataAttachmentUtils.selectImagesFromAttachments(attachments);
        if (!CollectionUtils.isEmpty(markImages)) {
            if (attachmentIndex == 0) {
                CMarkAttachment markLogo = markImages.get(attachmentIndex);
                if (Objects.nonNull(markLogo)) {
                    if (markLogo.isLoaded())
                        AttachmentUtils.writeData(response, markLogo.getData(), markLogo.getMimeType(), null);
                    else {
                        CLogo cLogo = markService.selectMarkLogo(cFileId);
                        if (Objects.nonNull(cLogo) && cLogo.isLoaded())
                            AttachmentUtils.writeData(response, cLogo.getLogoData(), markLogo.getMimeType(), null);
                    }
                }
            } else {
                CMarkAttachment markImage = markImages.get(attachmentIndex);
                if (Objects.nonNull(markImage)) {
                    if (markImage.isLoaded())
                        AttachmentUtils.writeData(response, markImage.getData(), markImage.getMimeType(), null);
                }
            }
        }
    }

    @RequestMapping(value = "/audio")
    public void getAudio(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer attachmentIndex, @RequestParam String sessionIdentifier) {
        String fullSessionIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
        if (SessionObjectUtils.isSessionObjectMark(fullSessionIdentifier)) {
            CMark sessionMark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
            CSignData signData = sessionMark.getSignData();
            List<CMarkAttachment> attachments = getAttachments(request, sessionIdentifier, signData);
            List<CMarkAttachment> markSounds = MarkSignDataAttachmentUtils.selectAudiosFromAttachments(attachments);
            writeAttachment(response, attachmentIndex, markSounds);
        }
    }

    @RequestMapping(value = "/video")
    public void getVideo(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer attachmentIndex, @RequestParam String sessionIdentifier) {
        String fullSessionIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
        if (SessionObjectUtils.isSessionObjectMark(fullSessionIdentifier)) {
            CMark sessionMark = MarkSessionUtils.getSessionMark(request, sessionIdentifier);
            CSignData signData = sessionMark.getSignData();
            List<CMarkAttachment> attachments = getAttachments(request, sessionIdentifier, signData);
            List<CMarkAttachment> markVideos = MarkSignDataAttachmentUtils.selectVideosFromAttachments(attachments);
            writeAttachment(response, attachmentIndex, markVideos);
        }
    }

    private List<CMarkAttachment> getAttachments(HttpServletRequest request, String sessionIdentifier, CSignData signData) {
        List<CMarkAttachment> attachments = null;
        List<CMarkAttachment> sessionMarkAttachments = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK_ATTACHMENTS, sessionIdentifier, request);
        if (Objects.nonNull(sessionMarkAttachments)) {
            attachments = sessionMarkAttachments;
        } else {
            if (Objects.nonNull(signData)) {
                attachments = signData.getAttachments();
            }
        }
        return attachments;
    }

    private void writeAttachment(HttpServletResponse response, Integer attachmentIndex, List<CMarkAttachment> attachments) {
        if (!CollectionUtils.isEmpty(attachments)) {
            CMarkAttachment sound = attachments.get(attachmentIndex);
            if (Objects.nonNull(sound)) {
                if (sound.isLoaded()) {
                    AttachmentUtils.writeData(response, sound.getData(), sound.getMimeType(), null);
                } else {
                    Integer id = sound.getId();
                    if (Objects.nonNull(id)) {
                        CMarkAttachment databaseSound = markAttachmentService.selectAttachmentById(id, true);
                        if (Objects.nonNull(databaseSound)) {
                            AttachmentUtils.writeData(response, databaseSound.getData(), databaseSound.getMimeType(), null);
                        }
                    }
                }
            }
        }
    }

}
