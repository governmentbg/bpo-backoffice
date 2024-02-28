package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.patent.CAttachmentTypeBookmarks;

import java.util.List;

public interface AttachmentTypeBookmarksService {
     List<CAttachmentTypeBookmarks> selectAllByAttachmentTypeId (Integer attachmentTypeId);
}
