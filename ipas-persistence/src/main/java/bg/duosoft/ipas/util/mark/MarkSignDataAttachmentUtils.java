package bg.duosoft.ipas.util.mark;

import bg.duosoft.ipas.core.model.mark.CMarkAttachment;
import bg.duosoft.ipas.core.model.mark.CSignData;
import bg.duosoft.ipas.enums.AttachmentType;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MarkSignDataAttachmentUtils {

    public static CMarkAttachment selectFirstImageAttachment(CSignData signData) {
        if (Objects.nonNull(signData)) {
            List<CMarkAttachment> attachments = signData.getAttachments();
            if (!CollectionUtils.isEmpty(attachments)) {
                CMarkAttachment markAttachment = attachments.stream()
                        .filter(attachment -> attachment.getAttachmentType() == AttachmentType.IMAGE)
                        .findFirst()
                        .orElse(null);
                return markAttachment;
            }
        }
        return null;
    }

    public static CMarkAttachment selectFirstSoundAttachment(CSignData signData) {
        if (Objects.nonNull(signData)) {
            List<CMarkAttachment> attachments = signData.getAttachments();
            if (!CollectionUtils.isEmpty(attachments)) {
                CMarkAttachment markAttachment = attachments.stream()
                        .filter(attachment -> attachment.getAttachmentType() == AttachmentType.AUDIO)
                        .findFirst()
                        .orElse(null);
                return markAttachment;
            }
        }
        return null;
    }

    public static List<CMarkAttachment> selectImagesFromAttachments(List<CMarkAttachment> markAttachments) {
        if (CollectionUtils.isEmpty(markAttachments))
            return markAttachments;

        return markAttachments.stream()
                .filter(markAttachment -> markAttachment.getAttachmentType() == AttachmentType.IMAGE)
                .collect(Collectors.toList());
    }

    public static List<CMarkAttachment> selectAudiosFromAttachments(List<CMarkAttachment> markAttachments) {
        if (CollectionUtils.isEmpty(markAttachments))
            return markAttachments;

        return markAttachments.stream()
                .filter(markAttachment -> markAttachment.getAttachmentType() == AttachmentType.AUDIO)
                .collect(Collectors.toList());
    }

    public static List<CMarkAttachment> selectVideosFromAttachments(List<CMarkAttachment> markAttachments) {
        if (CollectionUtils.isEmpty(markAttachments))
            return markAttachments;

        return markAttachments.stream()
                .filter(markAttachment -> markAttachment.getAttachmentType() == AttachmentType.VIDEO)
                .collect(Collectors.toList());
    }

}
