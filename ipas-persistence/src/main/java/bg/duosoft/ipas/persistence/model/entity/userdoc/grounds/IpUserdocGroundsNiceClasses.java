package bg.duosoft.ipas.persistence.model.entity.userdoc.grounds;

import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocGroundsNiceClassesPK;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassNice;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_USERDOC_GROUND_NICE_CLASSES", schema = "EXT_CORE")
public class IpUserdocGroundsNiceClasses  implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private IpUserdocGroundsNiceClassesPK pk;

    @Column(name = "NICE_CLASS_DESCRIPTION")
    private String niceClassDescription;
}
