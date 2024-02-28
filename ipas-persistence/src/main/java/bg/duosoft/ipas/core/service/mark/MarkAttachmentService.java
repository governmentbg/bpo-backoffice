package bg.duosoft.ipas.core.service.mark;

import bg.duosoft.ipas.core.model.mark.CMarkAttachment;

public interface MarkAttachmentService {

    CMarkAttachment selectAttachmentById(Integer id, boolean addAttachmentData);

}