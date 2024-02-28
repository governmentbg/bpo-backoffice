package bg.duosoft.ipas.persistence.model.entity.user;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 20.6.2019 г.
 * Time: 17:38
 */
@Entity
@Table(name = "CF_GROUP_SECURITY_ROLE", schema = "EXT_USER")
@Data
@Cacheable(value = false)
public class CfGroupSecurityRole implements Serializable {
    @EmbeddedId
    private CfGroupSecurityRolePK cfGroupSecurityRolePK;

    @Basic
    @Column(name = "DATE_CREATED", updatable = false)//This field is only getting inserted, but not updated!!! The idea is the creation date/user/ always to be the first created ones!
    private Date creationDate;

    @Basic
    @Column(name = "USER_CREATED", updatable = false)
    private Integer creationUserId;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "ROLE_NAME", referencedColumnName = "ROLE_NAME", updatable = false, insertable = false)
    })
    private CfSecurityRoles securityRole;


}
