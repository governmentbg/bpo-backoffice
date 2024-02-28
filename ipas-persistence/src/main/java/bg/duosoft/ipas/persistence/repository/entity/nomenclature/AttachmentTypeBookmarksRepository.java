package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAttachmentTypeBookmarks;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAttachmentTypeBookmarksPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttachmentTypeBookmarksRepository extends BaseRepository<CfAttachmentTypeBookmarks, CfAttachmentTypeBookmarksPK> {

    @Query(value="SELECT bm.* FROM EXT_CORE.CF_ATTACHMENT_TYPE_BOOKMARKS bm" +
            " WHERE ATTACHMENT_TYPE_ID = ?1", nativeQuery = true)
    List<CfAttachmentTypeBookmarks> selectAllByAttachmentTypeId (Integer attachmentTypeId);
}
