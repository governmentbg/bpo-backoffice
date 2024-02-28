package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CF_ATTACHMENT_TYPE", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfAttachmentType implements Serializable {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "ATTACHMENT_NAME_SUFFIX")
    private String attachmentNameSuffix;

    @Column(name = "ATTACHMENT_ORDER")
    private Integer attachmentOrder;

    @Column(name = "ATTACHMENT_FILE_TYPES")
    private String attachmentFileTypes;

    @Column(name = "ATTACHMENT_EXTENSION")
    private String attachmentExtension;

    @OneToMany
    @JoinColumn(name = "ATTACHMENT_TYPE_ID", referencedColumnName = "id", insertable = false, updatable = false)
    private List<CfAttachmentTypeBookmarks> bookmarks;
}
