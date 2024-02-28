package bg.duosoft.ipas.persistence.model.entity.efiling;

import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
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
@Table(name = "IP_USERDOC_EFILING_DATA", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpUserodocEFilingData implements Serializable {

    @EmbeddedId
    private IpDocPK pk;

    @Column(name = "LOG_USER_NAME")
    private String logUserName;

    @Column(name = "ES_USER")
    private String esUser;

    @Column(name = "ES_USER_NAME")
    private String esUserName;

    @Column(name = "ES_USER_EMAIL")
    private String esUserEmail;

    @Column(name = "ES_VALID_FROM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date esValidFrom;

    @Column(name = "ES_VALID_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date esValidTo;

    @Column(name = "ES_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date esDate;

}
