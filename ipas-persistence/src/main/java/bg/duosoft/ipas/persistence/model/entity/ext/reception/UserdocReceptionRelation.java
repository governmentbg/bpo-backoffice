package bg.duosoft.ipas.persistence.model.entity.ext.reception;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfUserdocType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "USERDOC_RECEPTION_RELATION", schema = "EXT_RECEPTION")
@Cacheable(value = false)
public class UserdocReceptionRelation implements Serializable {

    @EmbeddedId
    private UserdocReceptionRelationPK pk;

    @Column(name = "IS_VISIBLE")
    private String isVisible;

    @ManyToOne
    @JoinColumn(name = "LINKED_USERDOC_TYPE", referencedColumnName = "USERDOC_TYP", insertable = false, updatable = false)
    private CfUserdocType userdocType;

}
