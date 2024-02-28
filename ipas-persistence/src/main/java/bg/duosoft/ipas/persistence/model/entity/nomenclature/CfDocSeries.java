package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_DOC_SERIES", schema = "IPASPROD")
@Cacheable(value = false)
public class CfDocSeries implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Id
    @Column(name = "DOC_SER")
    private Integer docSer;

    @Column(name = "IND_ACTIVE")
    private String indActive;

    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;

}
