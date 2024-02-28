package bg.duosoft.ipas.persistence.model.entity.dailylog;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class IpDailyLogPK implements Serializable {

    @Column(name = "DOC_ORI")
    private String docOri;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DAILY_LOG_DATE")
    private Date dailyLogDate;

    @Column(name = "DOC_LOG")
    private String docLog;
}
