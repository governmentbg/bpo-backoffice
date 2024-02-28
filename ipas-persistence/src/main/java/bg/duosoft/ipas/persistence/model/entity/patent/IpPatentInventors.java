package bg.duosoft.ipas.persistence.model.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.IntellectualPropertyRelatedPerson;
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
@Table(name = "IP_PATENT_INVENTORS", schema = "IPASPROD")
@Cacheable(value = false)
public class IpPatentInventors implements Serializable, IntellectualPropertyRelatedPerson {

    @EmbeddedId
    private IpPatentInventorsPK pk;

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Column(name = "SEQ_NBR")
    private Integer seqNbr;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "PERSON_NBR", referencedColumnName = "PERSON_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "ADDR_NBR", referencedColumnName = "ADDR_NBR", insertable = false, updatable = false)
    })
    @IndexedEmbedded
    private IpPersonAddresses ipPersonAddresses;
}
