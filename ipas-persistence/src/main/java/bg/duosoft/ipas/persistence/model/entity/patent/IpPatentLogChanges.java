package bg.duosoft.ipas.persistence.model.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.IpFileLogChanges;
import bg.duosoft.ipas.persistence.model.entity.file.IpLogChangesPK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * User: ggeorgiev
 * Date: 30.1.2019 г.
 * Time: 11:45
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_PATENT_LOG_CHANGES", schema = "IPASPROD")
@Cacheable(value = false)
public class IpPatentLogChanges implements Serializable, IpFileLogChanges {
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;
    @EmbeddedId
    private IpLogChangesPK pk;
    @Column(name = "CHANGE_USER_ID")
    private Integer changeUserId;
    @Column(name = "CHANGE_DATE")
    private Timestamp changeDate;
    @Column(name = "DATA_CODE")
    private String dataCode;
    @Column(name = "DATA_VERSION_WCODE")
    private String dataVersionWcode;
    @Column(name = "CHANGE_USERDOC_REF")
    private String changeUserdocRef;
    @Column(name = "DATA_VALUE")
    private String dataValue;

}
