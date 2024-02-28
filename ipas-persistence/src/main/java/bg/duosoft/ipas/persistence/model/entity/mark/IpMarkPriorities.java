package bg.duosoft.ipas.persistence.model.entity.mark;

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
@Table(name = "IP_MARK_PRIORITIES", schema = "IPASPROD")
@Cacheable(value = false)
public class IpMarkPriorities implements Serializable, IntellectualPropertyPriority {

    @EmbeddedId
    private IpMarkPrioritiesPK pk;

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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumns({
//            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
//            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
//            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
//            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
//    })
//    private IpMark ipMark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUNTRY_CODE", referencedColumnName = "COUNTRY_CODE", insertable = false, updatable = false)
    private CfGeoCountry country;

}
