package bg.duosoft.ipas.persistence.model.entity.dailylog;

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
@Table(name = "IP_DAILY_LOG", schema = "IPASPROD")
@Cacheable(value = false)
public class IpDailyLog implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private IpDailyLogPK pk;

    @Column(name = "FIRST_DOC_NBR")
    private Integer firstDocNbr;

    @Column(name = "LAST_DOC_NBR")
    private Integer lastDocNbr;

    @Column(name = "IND_OPEN")
    private String indOpen;

    @Column(name = "IND_CLOSED")
    private String indClosed;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AFFECTED_FILES_READY_DATE")
    private Date affectedFilesReadyDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOGO_CAPTURE_READY_DATE")
    private Date logoCaptureReadyDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DIGITALIZATION_READY_DATE")
    private Date digitalizationReadyDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FILE_CAPTURE_READY_DATE")
    private Date fileCaptureReadyDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VIENNA_CODES_READY_DATE")
    private Date viennaCodesReadyDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ARCHIVING_DATE")
    private Date archivingDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "USERDOC_CAPTURE_READY_DATE")
    private Date userdocCaptureReadyDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CERTIFICATION_READY_DATE")
    private Date certificationReadyDate;

}
