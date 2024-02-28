package bg.duosoft.ipas.persistence.model.entity.action;

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
@Table(name = "IP_JOURNAL", schema = "IPASPROD")
@Cacheable(value = false)
public class IpJournal implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Id
    @Column(name = "JOURNAL_CODE")
    private String journalCode;

    @Column(name = "JORNAL_NAME")
    private String jornalName;

    @Column(name = "PUBLICATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date publicationDate;

    @Column(name = "NOTIFICATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notificationDate;

    @Column(name = "IND_CLOSED")
    private String indClosed;

    @Column(name = "IND_PUBLICATION_IN_PROCESS")
    private String indPublicationInProcess;

}
