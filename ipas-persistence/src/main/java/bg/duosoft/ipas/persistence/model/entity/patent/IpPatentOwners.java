package bg.duosoft.ipas.persistence.model.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.IntellectualPropertyOwner;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_PATENT_OWNERS", schema = "IPASPROD")
@Cacheable(value = false)
public class IpPatentOwners implements Serializable, IntellectualPropertyOwner {

    @EmbeddedId
    private IpPatentOwnersPK pk;

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "ORDER_NBR")
    private Integer orderNbr;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "PERSON_NBR", referencedColumnName = "PERSON_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "ADDR_NBR", referencedColumnName = "ADDR_NBR", insertable = false, updatable = false)
    })
    @IndexedEmbedded
    private IpPersonAddresses ipPersonAddresses;

}
