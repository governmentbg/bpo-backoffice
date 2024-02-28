package bg.duosoft.ipas.persistence.model.entity.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: ggeorgiev
 * Date: 20.6.2019 г.
 * Time: 18:46
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CF_THIS_USER_GROUP", schema = "IPASPROD")
@Cacheable(value = false)
public class CfThisUserGroup implements Serializable {
    @EmbeddedId
    private CfThisUserGroupPK cfThisUserGroupPK;

    @Basic
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Basic
    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;

//    @ManyToOne
//    @JoinColumn(name = "GROUP_ID", insertable = false, updatable = false)
//    private CfThisGroup group;

//    @ManyToOne
//    @JoinColumn(name = "USER_ID")
//    @MapsId("USER_ID")
//    private IpUser user;
}
