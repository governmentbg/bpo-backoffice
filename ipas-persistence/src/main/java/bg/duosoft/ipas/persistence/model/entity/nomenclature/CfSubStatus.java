package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_SUB_STATUS", schema = "IPASPROD")
@Cacheable(value = false)
public class CfSubStatus {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private CfSubStatusPK pk;

    @Column(name = "SUB_STATUS_NAME")
    private String subStatusName;

    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;

}
