package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "CF_CLASS_VIENNA_SECT")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Cacheable(value = false)
public class CfClassViennaSect
        implements Serializable {
    @EmbeddedId
    private CfClassViennaSectPK pk;
    @Column(name = "ROW_VERSION")
    private Long rowVersion;
    @Column(name = "VIENNA_SECTION_NAME")
    private String viennaSectionName;
    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;
    private static final long serialVersionUID = 1L;


}


