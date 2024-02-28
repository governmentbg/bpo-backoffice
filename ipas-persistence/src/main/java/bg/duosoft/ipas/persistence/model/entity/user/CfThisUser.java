package bg.duosoft.ipas.persistence.model.entity.user;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: ggeorgiev
 * Date: 25.7.2019 г.
 * Time: 14:48
 */
@Entity
@Table(name = "CF_THIS_USER", schema = "IPASPROD")
@Data
@Cacheable(value = false)
public class CfThisUser implements Serializable {
    @Basic
    @Column(name = "ROW_VERSION")
    private int rowVersion;
    @Id
    @Column(name = "USER_ID")
    private int userId;
    @Basic
    @Column(name = "USERNAME")
    private String username;
//    @Basic
//    @Column(name = "XML_DESIGNER")
//    private String xmlDesigner;
}
