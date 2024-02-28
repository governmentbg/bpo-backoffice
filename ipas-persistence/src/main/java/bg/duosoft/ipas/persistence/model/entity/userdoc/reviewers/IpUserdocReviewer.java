package bg.duosoft.ipas.persistence.model.entity.userdoc.reviewers;

import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "IP_USERDOC_REVIEWERS", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpUserdocReviewer implements Serializable {

    @EmbeddedId
    private IpUserdocReviewerPK pk;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    private IpUser user;

    @Column(name = "MAIN")
    private Boolean main;
}
