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
@Table(name = "CF_LIST", schema = "IPASPROD")
@Cacheable(value = false)
public class CfList implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Id
    @Column(name = "LIST_CODE")
    private String listCode;

    @Column(name = "LIST_NAME")
    private String listName;

    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;

    @Column(name = "IND_REQUIRED")
    private String indRequired;

}
