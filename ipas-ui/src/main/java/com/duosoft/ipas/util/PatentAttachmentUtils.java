package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CPatentAttachment;
import bg.duosoft.ipas.core.service.patent.PatentAttachmentService;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Objects;

public class PatentAttachmentUtils {

    public static void loadCPatentEmptyAttachments(CPatent patent, PatentAttachmentService patentAttachmentService){
        if (Objects.nonNull(patent.getPatentDetails()) && !CollectionUtils.isEmpty(patent.getPatentDetails().getPatentAttachments())){
            List<CPatentAttachment> patentAttachments = patent.getPatentDetails().getPatentAttachments();
            for (CPatentAttachment patentAttachment:patentAttachments) {
                if (!patentAttachment.isContentLoaded()){
                    CPatentAttachment dbPatentAttachment =  patentAttachmentService.findPatentAttachment(patentAttachment.getId(),patentAttachment.getAttachmentType().getId(),patent.getFile().getFileId(),true);
                    patentAttachment.setAttachmentContent(dbPatentAttachment.getAttachmentContent());
                }
            }
        }
    }

}
