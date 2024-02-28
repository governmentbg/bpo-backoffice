package bg.duosoft.ipas.persistence.model.entity.ext.agent;

import bg.duosoft.ipas.util.search.BooleanToIndInactiveBridge;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * User: ggeorgiev
 * Date: 22.3.2019 г.
 * Time: 12:13
 */
@Entity
@Table(name = "EXTENDED_PARTNERSHIP", schema = "EXT_AGENT")
@NoArgsConstructor
@Getter
@Setter
@Cacheable(value = false)
public class ExtendedPartnership implements Serializable {
    @Id
    @Column(name = "PERSON_NBR")
    private Integer personNbr;
    @Basic
    @Column(name = "PERSON_NAME_EN")
    private String personNameEn;
    @Basic
    @Column(name = "PARTNERSHIP_CODE")
    @Field(name = "partnershipCodeField", analyze = Analyze.YES, index= org.hibernate.search.annotations.Index.YES, store = Store.YES)
    @Field(name = "partnershipCodeExact", analyze = Analyze.NO, index= Index.YES, store = Store.YES, norms = Norms.NO)
    private String partnershipCode;
    @Basic
    @Column(name = "PARTNERSHIP_ALT_CODE")
    private String partnershipAltCode;
    @Basic
    @Column(name = "ORI_COUNTRY_CODE")
    private String oriCountryCode;
    @Basic
    @Column(name = "NATIONAL_NBR")
    private String nationalNbr;
    @Basic
    @Column(name = "REPRESENT_QUALITY")
    private BigInteger representQuality;
    @Basic
    @Column(name = "REPRESENT_NAME")
    private String representName;
    @Basic
    @Column(name = "ORI_REPRESENT_PERSONS")
    private String oriRepresentPersons;
    @Basic
    @Column(name = "IND_INACTIVE")
    @Field(analyze = Analyze.NO, index= Index.YES, store = Store.YES, bridge = @FieldBridge(impl = BooleanToIndInactiveBridge.class))
    private String indInactive;
    @ManyToOne
    @JoinColumn(name = "PARTNERSHIP_TYPE", referencedColumnName = "ID")
    private CfPartnershipType partnershipType;

//    @OneToOne(mappedBy = "personNbr", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
//    private IpPerson ipPerson;

}
