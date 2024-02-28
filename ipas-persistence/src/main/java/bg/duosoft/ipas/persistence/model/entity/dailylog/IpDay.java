package bg.duosoft.ipas.persistence.model.entity.dailylog;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfDocSeries;
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
@Table(name = "IP_DAY", schema = "IPASPROD")
@Cacheable(value = false)
public class IpDay implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Id
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DAILY_DATE")
    private Date dailyDate;

    @ManyToOne
    @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false)
    private CfDocSeries docSer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "NEXT_WORKING_DATE")
    private Date nextWorkingDate;

}
