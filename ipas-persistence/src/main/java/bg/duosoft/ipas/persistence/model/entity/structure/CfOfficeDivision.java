package bg.duosoft.ipas.persistence.model.entity.structure;

import bg.duosoft.ipas.persistence.model.entity.InsertableEntity;
import bg.duosoft.ipas.persistence.model.entity.UpdatableEntity;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@DynamicUpdate
@Table(name = "CF_OFFICE_DIVISION", schema = "IPASPROD")
@Cacheable(value = false)
@Inheritance(strategy = InheritanceType.JOINED)
public class CfOfficeDivision implements Serializable, CfOfficeStructureEntity, UpdatableEntity, InsertableEntity {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Id
    @Column(name = "OFFICE_DIVISION_CODE")
    private String officeDivisionCode;

    @Column(name = "OFFICE_DIVISION_NAME")
    private String name;

    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;

    @OneToOne(orphanRemoval = true)
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
