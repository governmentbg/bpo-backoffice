package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.*;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CfAttachmentTypeBookmarksPK implements Serializable {
    @Column(name = "ATTACHMENT_TYPE_ID")
    private Integer attachmentTypeId;

    @Column(name = "BOOKMARK_NAME")
    private String bookmarkName;
}