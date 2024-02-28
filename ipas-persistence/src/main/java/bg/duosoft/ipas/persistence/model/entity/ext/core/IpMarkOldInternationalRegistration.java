package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_MARK_OLD_INTL_REGISTRATION", schema = "EXT_CORE")
public class IpMarkOldInternationalRegistration implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "WIPO_REFERENCE")
    private Integer wipoReference;

    @Column(name = "BG_REFERENCE")
    private String bgReference;

    @Column(name = "RECEIVED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedDate;

    @Column(name = "FILE_NBR")
    private Integer fileNbr;

    @Column(name = "REGISTRATION_NBR")
    private Integer registrationNbr;

    @Column(name = "REGISTRATION_DUP")
    private String registrationDup;

    @Column(name = "INTERNATIONAL_REG_NBR")
    private String intlRegNbr;

    @Column(name = "INTERNATIONAL_REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date intlRegDate;

    @Column(name = "HOLDER_NAME")
    private String holderName;

    @Column(name = "HOLDER_ADDRESS")
    private String holderAddress;

    @Column(name = "PROCESSED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date processedDate;

    @Column(name = "EXTERNAL_SYSTEM_ID")
    private String externalSystemId;

    @Column(name = "HAS_ABDOCS_RECORD")
    private Boolean hasAbdocsRecord;
}
