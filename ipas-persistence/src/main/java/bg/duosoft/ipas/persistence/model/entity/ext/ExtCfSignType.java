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
@Table(name = "EXT_CF_SIGN_TYPE", schema = "IPASPROD")
@Cacheable(value = false)
public class ExtCfSignType implements Serializable {

    @Id
    @Column(name = "SIGN_TYPE")
    private String signType;

    @Column(name = "SIGN_TYPE_NAME")
    private String signTypeName;
}
