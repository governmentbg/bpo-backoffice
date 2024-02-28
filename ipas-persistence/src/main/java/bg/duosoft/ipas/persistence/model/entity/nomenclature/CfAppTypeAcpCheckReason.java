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
@Table(name = "CF_APP_TYPE_ACP_CHECK_REASON", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfAppTypeAcpCheckReason implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "APPL_TYP")
    private String applicationType;

    @ManyToOne
    @JoinColumn(name = "ACP_CHECK_REASON_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private CfAcpCheckReason acpCheckReason;

}
