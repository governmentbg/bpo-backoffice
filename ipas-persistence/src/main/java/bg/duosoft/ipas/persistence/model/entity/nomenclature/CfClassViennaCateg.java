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
@Table(name = "CF_CLASS_VIENNA_CATEG", schema = "IPASPROD")
@Cacheable(value = false)
public class CfClassViennaCateg implements Serializable {

    @EmbeddedId
    private CfClassViennaCategPK pk;

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Column(name = "VIENNA_CATEGORY_NAME")
    private String viennaCategoryName;

    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;

}
