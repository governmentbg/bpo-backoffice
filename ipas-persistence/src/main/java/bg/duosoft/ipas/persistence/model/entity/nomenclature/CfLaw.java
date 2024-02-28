package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_LAW", schema = "IPASPROD")
@Cacheable(value = false)
public class CfLaw implements Serializable {
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Id
    @Column(name = "LAW_CODE")
    private Integer lawCode;

    @Column(name = "LAW_NAME")
    private String lawName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAW_ENFORCEMENT_DATE")
    private Date lawEnforcementDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAW_ENDING_DATE")
    private Date lawEndingDate;

    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;

}
