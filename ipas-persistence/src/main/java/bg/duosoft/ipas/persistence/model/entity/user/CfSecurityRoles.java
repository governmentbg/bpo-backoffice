package bg.duosoft.ipas.persistence.model.entity.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 20.6.2019 г.
 * Time: 17:35
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CF_SECURITY_ROLES", schema = "EXT_USER")
@Cacheable(value = false)
public class CfSecurityRoles implements Serializable {

    @Id
    @Basic
    @Column(name = "ROLE_NAME")
    private String roleName;
    @Basic
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic
    @Column(name = "DATE_CREATED")
    private Date dateCreated;
}
