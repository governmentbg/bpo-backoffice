package bg.duosoft.ipas.persistence.model.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAttachmentType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "IP_PATENT_ATTACHMENT", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpPatentAttachment implements Serializable {

    @EmbeddedId
    private IpPatentAttachmentPK pk;

    @Column(name="ATTACHMENT_NAME")
    private String attachmentName;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ATTACHMENT_CONTENT")
    private byte[] attachmentContent;

    @Column(name = "DATE_CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @ManyToOne
    @JoinColumn(name = "ATTACHMENT_TYPE_ID", referencedColumnName = "id", insertable = false, updatable = false)
    private CfAttachmentType attachmentType;

    @Column(name = "HAS_BOOKMARKS")
    private Boolean hasBookmarks;

}
