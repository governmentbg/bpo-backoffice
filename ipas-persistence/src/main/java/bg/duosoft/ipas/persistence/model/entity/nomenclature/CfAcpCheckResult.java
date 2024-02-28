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
@Table(name = "CF_ACP_CHECK_RESULT", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfAcpCheckResult implements Serializable {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "DESCRIPTION")
    private String description;

}
