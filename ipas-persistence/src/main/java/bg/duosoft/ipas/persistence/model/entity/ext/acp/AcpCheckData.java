package bg.duosoft.ipas.persistence.model.entity.ext.acp;

import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAcpCheckResult;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "ACP_CHECK_DATA", schema = "EXT_CORE")
@Cacheable(value = false)
public class AcpCheckData implements Serializable  {

    @EmbeddedId
    private IpFilePK pk;

    @Column(name = "CHECK_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkDate;

    @Column(name = "RESULT_ID")
    private Integer resultId;

    @ManyToOne
    @JoinColumn(name = "RESULT_ID", referencedColumnName = "ID",insertable = false,updatable = false)
    private CfAcpCheckResult acpCheckResult;
}
