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
@Table(name = "CF_CONFIG_PARAM", schema = "IPASPROD")
@Cacheable(value = false)
public class CfConfigParam implements Serializable {
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Id
    @Column(name = "CONFIG_CODE")
    private String configCode;

    @Column(name = "VALUE")
    private String value;

}
