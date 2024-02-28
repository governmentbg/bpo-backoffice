package bg.duosoft.ipas.persistence.model.entity.file;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfRelationshipType;
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
@Table(name = "IP_FILE_RELATIONSHIP_EXTENDED", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpFileRelationshipExtended implements Serializable {

    @EmbeddedId
    private IpFilePK pk;

    @Column(name="APPLICATION_TYPE")
    private String applicationType;

    @Column(name="FILING_NUMBER")
    private String filingNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FILING_DATE")
    private Date filingDate;

    @Column(name="REGISTRATION_NUMBER")
    private String registrationNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGISTRATION_DATE")
    private Date registrationDate;

    @Column(name = "REGISTRATION_COUNTRY")
    private String registrationCountry;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CANCELLATION_DATE")
    private Date cancellationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PRIORITY_DATE")
    private Date priorityDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SERVE_MESSAGE_DATE")
    private Date serveMessageDate;

    @ManyToOne
    @JoinColumn(name = "RELATIONSHIP_TYP", referencedColumnName = "RELATIONSHIP_TYP")
    private CfRelationshipType relationshipType;

}
