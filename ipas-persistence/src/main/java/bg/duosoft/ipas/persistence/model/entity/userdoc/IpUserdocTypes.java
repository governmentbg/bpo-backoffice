package bg.duosoft.ipas.persistence.model.entity.userdoc;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfUserdocType;
import lombok.*;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "IP_USERDOC_TYPES", schema = "IPASPROD")
@Cacheable(value = false)
public class IpUserdocTypes implements Serializable {

    @EmbeddedId
    @IndexedEmbedded
    private IpUserdocTypesPK pk;

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Column(name = "PROC_TYP")
    private String procTyp;

    @Column(name = "PROC_NBR")
    private Integer procNbr;

    @Column(name = "IND_PROCESS_FIRST")
    private String indProcessFirst;

    @ManyToOne
    @JoinColumn(name = "USERDOC_TYP", referencedColumnName = "USERDOC_TYP", insertable = false, updatable = false)
    private CfUserdocType userdocType;

}
