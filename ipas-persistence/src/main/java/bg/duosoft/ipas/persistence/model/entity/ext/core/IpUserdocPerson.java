package bg.duosoft.ipas.persistence.model.entity.ext.core;

import bg.duosoft.ipas.enums.RepresentativeType;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.persistence.model.entity.UserDocumentRelatedPerson;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.util.search.IpUserDocPersonPKBridge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.EnumBridge;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_USERDOC_PERSON", schema = "EXT_CORE")
@Cacheable(value = false)
@Indexed
public class IpUserdocPerson implements Serializable, UserDocumentRelatedPerson {
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    @IndexedEmbedded
    @FieldBridge(impl = IpUserDocPersonPKBridge.class)
    private IpUserdocPersonPK pk;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "IND_MAIN")
    private String indMain;

    @Column(name = "REPRESENTATIVE_TYP")
    private String representativeType;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "PERSON_NBR", referencedColumnName = "PERSON_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "ADDR_NBR", referencedColumnName = "ADDR_NBR", insertable = false, updatable = false)
    })
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
