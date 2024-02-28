package bg.duosoft.ipas.persistence.model.entity.user;

import bg.duosoft.ipas.persistence.model.entity.InsertableEntity;
import bg.duosoft.ipas.persistence.model.entity.UpdatableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 20.6.2019 г.
 * Time: 17:51
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CF_THIS_GROUP", schema = "IPASPROD")
@Cacheable(value = false)
public class CfThisGroup implements Serializable, UpdatableEntity, InsertableEntity {
    @Basic
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;
    @Id
    @Column(name = "GROUP_ID")
    private Integer groupId;
    @Basic
    @Column(name = "GROUPNAME")
    private String groupname;
    @Basic
    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;
    @Basic
    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "CREATION_USER_ID")
    private Integer creationUserId;

    @Basic
    @Column(name = "LAST_UPDATE_DATE")
    private Date lastUpdateDate;

    @Column(name = "LAST_UPDATE_USER_ID")
    private Integer lastUpdateUserId;

    @Basic
    @Column(name = "DESCRIPTION")
    private String description;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
//    private List<CfThisUserGroup> users;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "GROUP_ID", referencedColumnName = "GROUP_ID", insertable = false, updatable = false)
    private List<CfGroupSecurityRole> groupSecurityRoles;

}
