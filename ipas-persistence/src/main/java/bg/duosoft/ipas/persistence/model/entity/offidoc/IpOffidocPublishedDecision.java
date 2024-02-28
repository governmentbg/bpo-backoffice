package bg.duosoft.ipas.persistence.model.entity.offidoc;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_OFFIDOC_PUBLISHED_DECISION", schema = "EXT_CORE")
@Cacheable(value = false)
@NoArgsConstructor
@AllArgsConstructor
public class IpOffidocPublishedDecision implements Serializable {


    @EmbeddedId
    private IpOffidocPK pk;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ATTACHMENT_CONTENT")
    private byte[] attachmentContent;

    @Column(name = "ATTACHMENT_NAME")
    private String attachmentName;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DECISION_DATE")
    private Date decisionDate;

}
