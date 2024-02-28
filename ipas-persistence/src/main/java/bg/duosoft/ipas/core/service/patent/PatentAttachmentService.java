package bg.duosoft.ipas.core.service.patent;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.patent.CPatentAttachment;
import bg.duosoft.ipas.core.model.util.CPdfAttachmentBookmark;
import java.util.List;

public interface PatentAttachmentService {
    CPatentAttachment findPatentAttachment(Integer id, Integer attachmentType, CFileId fileId,boolean loadContent);
    Integer getMaxIdByFileIdAndAttachmentType(CFileId fileId,Integer attachmentType);
    List<CPdfAttachmentBookmark> initAttachmentBookmarks(CPatentAttachment patentAttachment, CFileId fileId);
    void updateBookmarks(CFileId fileId,CPatentAttachment patentAttachment,List<CPdfAttachmentBookmark> bookmarks);
    byte[] getAttachmentContent(CPatentAttachment patentAttachment, CFileId fileId);
}
