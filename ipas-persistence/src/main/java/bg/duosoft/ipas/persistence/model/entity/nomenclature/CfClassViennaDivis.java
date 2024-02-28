package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "CF_CLASS_VIENNA_DIVIS")
@Setter
@Getter
@Cacheable(value = false)
public class CfClassViennaDivis
        implements Serializable {
    @EmbeddedId
    private CfClassViennaDivisPK pk;
    @Column(name = "ROW_VERSION")
    private Long rowVersion;
    @Column(name = "VIENNA_DIVISION_NAME")
    private String viennaDivisionName;
    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;
    private static final long serialVersionUID = 1L;


}


