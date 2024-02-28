package bg.duosoft.ipas.persistence.model.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.IntellectualPropertyPriority;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfGeoCountry;
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
@Table(name = "IP_PATENT_PRIORITIES", schema = "IPASPROD")
@Cacheable(value = false)
public class IpPatentPriorities implements Serializable, IntellectualPropertyPriority {

    @EmbeddedId
    private IpPatentPrioritiesPK pk;

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PRIORITY_DATE")
    private Date priorityDate;

    @Column(name = "IND_ACCEPTED")
    private String indAccepted;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "PRIORITY_APPL_ID_ALT")
    private Long priorityApplIdAlt;

    @Column(name = "NOTES_UNUSED")
    private String notesUnused;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUNTRY_CODE", referencedColumnName = "COUNTRY_CODE", insertable = false, updatable = false)
    private CfGeoCountry country;
}
