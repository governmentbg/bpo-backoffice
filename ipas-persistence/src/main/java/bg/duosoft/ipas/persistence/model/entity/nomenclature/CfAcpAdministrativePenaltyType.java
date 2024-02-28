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
@Table(name = "CF_ACP_ADMINISTRATIVE_PENALTY_TYPE", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfAcpAdministrativePenaltyType implements Serializable {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "DESCRIPTION")
    private String description;

}
