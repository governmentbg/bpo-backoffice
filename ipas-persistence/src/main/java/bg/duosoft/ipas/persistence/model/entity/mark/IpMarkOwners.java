package bg.duosoft.ipas.persistence.model.entity.mark;

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
@Table(name = "IP_MARK_OWNERS", schema = "IPASPROD")
@Cacheable(value = false)
public class IpMarkOwners implements Serializable, IntellectualPropertyOwner {

    @EmbeddedId
    private IpMarkOwnersPK pk;

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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumns({
//            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
//            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
//            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
//            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false)
//    })
//    private IpMark ipMark;

}
