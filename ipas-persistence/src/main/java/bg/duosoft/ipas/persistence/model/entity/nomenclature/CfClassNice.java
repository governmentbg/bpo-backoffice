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
@Table(name = "CF_CLASS_NICE", schema = "IPASPROD")
@Cacheable(value = false)
public class CfClassNice implements Serializable {

    @EmbeddedId
    private CfClassNicePK pk;

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Column(name = "NICE_CLASS_NAME")
    private String niceClassName;

    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;

    @Column(name = "CLASS_DETAIL")
    private String classDetail;



}
