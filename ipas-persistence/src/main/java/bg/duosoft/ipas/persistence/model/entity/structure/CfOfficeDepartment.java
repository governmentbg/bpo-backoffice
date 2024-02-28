package bg.duosoft.ipas.persistence.model.entity.structure;

import bg.duosoft.ipas.persistence.model.entity.InsertableEntity;
import bg.duosoft.ipas.persistence.model.entity.UpdatableEntity;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CF_OFFICE_DEPARTMENT", schema = "IPASPROD")
@Cacheable(value = false)
@Inheritance(strategy = InheritanceType.JOINED)
public class CfOfficeDepartment implements Serializable, CfOfficeStructureEntity, UpdatableEntity, InsertableEntity {

    @Column(name = "ROW_VERSION")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer rowVersion;

    @EmbeddedId
    private CfOfficeDepartmentPK cfOfficeDepartmentPK;

    @Column(name = "OFFICE_DEPARTMENT_NAME")
    private String name;

    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "OFFICE_DIVISION_CODE",referencedColumnName = "OFFICE_DIVISION_CODE",insertable = false,updatable = false)
    })
    private CfOfficeDivision division;

    @OneToOne
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
