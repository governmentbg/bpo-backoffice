package bg.duosoft.ipas.persistence.model.entity.structure;

import bg.duosoft.ipas.persistence.model.entity.InsertableEntity;
import bg.duosoft.ipas.persistence.model.entity.UpdatableEntity;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
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
@Table(name = "CF_OFFICE_SECTION", schema = "IPASPROD")
@Cacheable(value = false)
@Inheritance(strategy = InheritanceType.JOINED)
public class CfOfficeSection implements Serializable, CfOfficeStructureEntity, UpdatableEntity, InsertableEntity {
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private CfOfficeSectionPK cfOfficeSectionPK;

    @Column(name = "OFFICE_SECTION_NAME")
    private String name;

    @Column(name = "IND_DELIVERY_ANY_MEMBER")
    private String indDeliveryAnyMember;

    @Column(name = "IND_DELIVERY_IGNORE")
    private String indDeliveryIgnore;

    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;

    @ManyToOne
    @JoinColumn(name = "SIGNATURE_USER_ID", referencedColumnName = "USER_ID")
    private IpUser signatureUser;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "CREATION_USER_ID")
    private Integer creationUserId;

    @Column(name = "LAST_UPDATE_DATE")
    private Date lastUpdateDate;

    @Column(name = "LAST_UPDATE_USER_ID")
    private Integer lastUpdateUserId;

}
