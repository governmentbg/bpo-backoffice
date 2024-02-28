package bg.duosoft.ipas.persistence.model.entity.vw.ind;

import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.util.search.IpProcBridge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;

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
@Entity(name = "bg.duosoft.ipas.persistence.model.entity.process.IpProc")
@Table(name = "VW_PROC_INDEX", schema = "EXT_CORE")
@Cacheable(value = false)
public class VwProcessIndex implements VwIndex {
    @EmbeddedId
    private IpProcPK pk;

    @Column(name = "STATUS_CODE")
    private String statusCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STATUS_DATE")
    private Date statusDate;

    @Column(name = "RESPONSIBLE_USER_ID")
    private Integer responsibleUserId;

}
