package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_RELATIONSHIP_TYPE", schema = "IPASPROD")
@Cacheable(value = false)
public class CfRelationshipType implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Id
    @Column(name = "RELATIONSHIP_TYP")
    private String relationshipTyp;

    @Column(name = "RELATIONSHIP_NAME")
    private String relationshipName;

    @Column(name = "IND_RENEWAL")
    private String indRenewal;

    @Column(name = "DIRECT_RELATIONSHIP_NAME")
    private String directRelationshipName;

    @Column(name = "IND_DIRECT_FUNCTION")
    private String indDirectFunction;

    @Column(name = "IND_DIRECT_REQUIRED")
    private String indDirectRequired;

    @Column(name = "INVERSE_RELATIONSHIP_NAME")
    private String inverseRelationshipName;

    @Column(name = "IND_INVERSE_FUNCTION")
    private String indInverseFunction;

    @Column(name = "IND_INVERSE_REQUIRED")
    private String indInverseRequired;

}
