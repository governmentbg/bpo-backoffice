package bg.duosoft.ipas.persistence.model.entity.nomenclature;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CF_CLASS_CPC", schema = "IPASPROD")
@Cacheable(value = false)
public class CfClassCpc implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private CfClassCpcPK pk;

    @Column(name = "CPC_NAME")
    private String cpcName;

    @Column(name = "CPC_LATEST_VERSION")
    private String cpcLatestVersion;

    @Column(name = "SYMBOL")
    private String symbol;
}
