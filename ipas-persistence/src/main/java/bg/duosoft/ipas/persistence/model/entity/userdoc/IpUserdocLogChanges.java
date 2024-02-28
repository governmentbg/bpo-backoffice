package bg.duosoft.ipas.persistence.model.entity.userdoc;

import bg.duosoft.ipas.persistence.model.entity.IpLogChanges;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * User: ggeorgiev
 * Date: 22.11.2021
 * Time: 14:45
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_USERDOC_LOG_CHANGES", schema = "IPASPROD")
@Cacheable(value = false)
public class IpUserdocLogChanges implements Serializable, IpLogChanges {
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;
    @EmbeddedId
    private IpUserdocLogChangesPK pk;
    @Column(name = "CHANGE_USER_ID")
    private Integer changeUserId;
    @Column(name = "CHANGE_DATE")
    private Timestamp changeDate;
    @Column(name = "DATA_CODE")
    private String dataCode;
    @Column(name = "DATA_VERSION_WCODE")
    private String dataVersionWcode;
    @Column(name = "DATA_VALUE")
    private String dataValue;

}
