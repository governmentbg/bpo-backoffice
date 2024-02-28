package bg.duosoft.ipas.persistence.model.entity.user;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * User: ggeorgiev
 * Date: 20.6.2019 г.
 * Time: 17:38
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Embeddable
public class CfThisUserGroupPK implements Serializable {
    @Column(name = "USER_ID")
    private Integer userId;
    @Column(name = "GROUP_ID")
    private Integer groupId;
}
