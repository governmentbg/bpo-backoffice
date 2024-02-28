package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@Cacheable(value = false)
@Table(name = "CF_PERSON_ID_TYPE", schema = "IPASPROD")
public class CfPersonIdType implements Serializable {
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Id
    @Column(name = "PERSON_ID_TYP")
    private String personIdTyp;

    @Column(name = "PERSON_ID_NAME")
    private String personIdName;

    @Column(name = "IND_GENERAL_ID")
    private String indGeneralId;

    @Column(name = "IND_INDIVIDUAL_ID")
    private String indIndividualId;

}
