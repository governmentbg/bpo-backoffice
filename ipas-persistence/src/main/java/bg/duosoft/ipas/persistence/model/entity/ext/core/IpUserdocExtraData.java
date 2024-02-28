package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "IP_USERDOC_EXTRA_DATA", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpUserdocExtraData implements Serializable {
    @EmbeddedId
    private IpUserdocExtraDataPK pk;

    @Column(name = "TEXT_VALUE")
    private String textValue;

    @Column(name = "NUMBER_VALUE")
    private Integer numberValue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_VALUE")
    private Date dateValue;

    @Column(name = "BOOLEAN_VALUE")
    private Boolean booleanValue;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "CODE", referencedColumnName = "CODE", insertable = false, updatable = false)
    private CfUserdocExtraDataType extraDataType;

}
