package bg.duosoft.ipas.persistence.model.entity.ext.core;

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
@Table(name = "IP_PERSON_ABDOCS_SYNC", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpPersonAbdocsSync implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "PERSON_NBR")
    private Integer personNbr;

    @Column(name = "ADDR_NBR")
    private Integer addrNbr;

    @Column(name = "INSERTED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PROCESSED_AT")
    private Date processedAt;

    @Column(name = "IND_SYNC")
    private String indSync;
}
