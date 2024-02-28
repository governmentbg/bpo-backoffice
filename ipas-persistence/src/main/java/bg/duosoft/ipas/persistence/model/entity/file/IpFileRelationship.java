package bg.duosoft.ipas.persistence.model.entity.file;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfRelationshipType;
import bg.duosoft.ipas.util.search.IpFileRelationshipPkBridge;
import bg.duosoft.ipas.util.search.IpPatentLocarnoClassesPKBridge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Indexed
@Table(name = "IP_FILE_RELATIONSHIP", schema = "IPASPROD")
@Cacheable(value = false)
public class IpFileRelationship implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    @FieldBridge(impl = IpFileRelationshipPkBridge.class)
    private IpFileRelationshipPK pk;

    @ManyToOne
    @JoinColumn(name = "RELATIONSHIP_TYP", referencedColumnName = "RELATIONSHIP_TYP", insertable = false, updatable = false)
    private CfRelationshipType relationshipType;

}
