package bg.duosoft.ipas.persistence.model.entity.ext;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "EXT_CONFIG_PARAM", schema = "IPASPROD")
@Cacheable(value = false)
public class ExtConfigParam implements Serializable {
    @Id
    @Column(name = "CONFIG_CODE")
    private String configCode;

    @Column(name = "VALUE")
    private String value;

}
