package bg.duosoft.ipas.persistence.model.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.IntellectualPropertyRepresentative;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_PATENT_REPRS", schema = "IPASPROD")
@Cacheable(value = false)
public class IpPatentReprs implements Serializable, IntellectualPropertyRepresentative {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private IpPatentReprsPK pk;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "PERSON_NBR", referencedColumnName = "PERSON_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "ADDR_NBR", referencedColumnName = "ADDR_NBR", insertable = false, updatable = false)
    })
    @IndexedEmbedded
    private IpPersonAddresses ipPersonAddresses;

    @Temporal(TemporalType.DATE)
    @Column(name = "ATTORNEY_POWER_TERM")
    private Date attorneyPowerTerm;

    @Column(name = "REAUTHORIZATION_RIGHT")
    private Boolean reauthorizationRight;

    @Column(name = "PRIOR_REPRS_REVOCATION")
    private Boolean priorReprsRevocation;

    @Column(name = "AUTHORIZATION_CONDITION")
    private String authorizationCondition;
}


