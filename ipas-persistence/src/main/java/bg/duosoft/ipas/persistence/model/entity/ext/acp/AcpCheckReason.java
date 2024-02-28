package bg.duosoft.ipas.persistence.model.entity.ext.acp;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAcpCheckReason;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "ACP_CHECK_REASON", schema = "EXT_CORE")
@Cacheable(value = false)
public class AcpCheckReason implements Serializable {

    @EmbeddedId
    private AcpCheckReasonPK pk;

    @ManyToOne
    @JoinColumn(name = "REASON_ID", referencedColumnName = "ID",insertable = false,updatable = false)
    private CfAcpCheckReason acpCheckReason;

}
