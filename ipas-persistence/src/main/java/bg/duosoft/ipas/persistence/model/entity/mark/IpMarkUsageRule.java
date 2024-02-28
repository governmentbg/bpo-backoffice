package bg.duosoft.ipas.persistence.model.entity.mark;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfUsageRule;
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
@Table(name = "IP_MARK_USAGE_RULE", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpMarkUsageRule implements Serializable {

    @EmbeddedId
    private IpMarkUsageRulePK pk;

    @Column(name="name")
    private String name;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "content")
    private byte[] content;

    @Column(name = "DATE_CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @ManyToOne
    @JoinColumn(name = "type", referencedColumnName = "id", insertable = false, updatable = false)
    private CfUsageRule usageRule;
}
