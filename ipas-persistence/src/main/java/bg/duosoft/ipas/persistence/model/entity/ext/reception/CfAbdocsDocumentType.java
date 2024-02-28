package bg.duosoft.ipas.persistence.model.entity.ext.reception;

import bg.duosoft.ipas.enums.IpasObjectType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_ABDOCS_DOCUMENT_TYPE", schema = "EXT_RECEPTION")
@Cacheable(value = false)
public class CfAbdocsDocumentType implements Serializable {

    @Column(name = "ABDOCS_DOC_TYPE_ID")
    private Integer abdocsDocTypeId;

    @Id
    @Column(name = "TYPE")
    private String type;

    @Column(name = "NAME")
    private String name;

    @Column(name = "IPAS_OBJECT")
    @Enumerated(EnumType.STRING)
    private IpasObjectType ipasObject;

    @Column(name = "DOC_REGISTRATION_TYPE")
    private Integer docRegistrationType;

}
