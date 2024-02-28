package bg.duosoft.ipas.persistence.model.entity.design;

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
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "IP_DOC", schema = "IPASPROD")
@Cacheable(value = false)
public class SingleDesignIpDoc implements Serializable {

    @EmbeddedId
    private IpDocPK pk;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RECEPTION_DATE")
    private Date receptionDate;
}
