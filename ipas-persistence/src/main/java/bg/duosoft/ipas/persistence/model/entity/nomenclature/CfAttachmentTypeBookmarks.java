package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CF_ATTACHMENT_TYPE_BOOKMARKS", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfAttachmentTypeBookmarks implements Serializable {

    @EmbeddedId
    private CfAttachmentTypeBookmarksPK pk;

    @Column(name = "BOOKMARK_REQUIRED")
    private Boolean bookmarkRequired;

    @Column(name = "bookmark_order")
    private Integer bookmarkOrder;

}
