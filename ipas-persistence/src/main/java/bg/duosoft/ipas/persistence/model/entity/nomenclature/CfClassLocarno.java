package bg.duosoft.ipas.persistence.model.entity.nomenclature;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CF_CLASS_LOCARNO", schema = "IPASPROD")
@Cacheable(value = false)
public class CfClassLocarno  implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    @IndexedEmbedded
    private CfClassLocarnoPK pk;

    @Column(name = "LOCARNO_NAME")
    private String locarnoName;

    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;


}
