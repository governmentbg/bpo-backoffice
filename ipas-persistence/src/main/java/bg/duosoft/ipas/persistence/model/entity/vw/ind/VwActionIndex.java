package bg.duosoft.ipas.persistence.model.entity.vw.ind;

import bg.duosoft.ipas.persistence.model.entity.action.IpActionPK;
import bg.duosoft.ipas.util.search.IpActionBridge;
import bg.duosoft.ipas.util.search.IpProcBridge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 02.03.2021
 * Time: 13:31
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "VW_ACTION_INDEX", schema = "EXT_CORE")
@Cacheable(value = false)
public class VwActionIndex implements VwIndex {
    @EmbeddedId
    private IpActionPK pk;

    @Column(name = "ACTION_TYP")
    private String actionTyp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTION_DATE")
    private Date actionDate;

    @Column(name = "NEW_STATUS_CODE")
    private String statusCode;

    @Column(name = "PRIOR_STATUS_CODE")
    private String priorStatusCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PRIOR_STATUS_DATE")
    private Date priorStatusDate;

    @Column(name = "RESPONSIBLE_USER_ID")
    private Integer responsibleUserId;

    @Column(name = "CAPTURE_USER_ID")
    private Integer captureUserId;

    @Column(name = "JOURNAL_YEAR")
    private String year;

    @Column(name = "JOURNAL_BULETIN")
    private String buletin;

    @Column(name = "JOURNAL_SECT")
    private String sect;

}
