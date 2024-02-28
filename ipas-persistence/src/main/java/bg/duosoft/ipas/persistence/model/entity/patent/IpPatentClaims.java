package bg.duosoft.ipas.persistence.model.entity.patent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_PATENT_CLAIMS", schema = "IPASPROD")
@Cacheable(value = false)
public class IpPatentClaims implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private IpPatentClaimsPK pk;

    @Column(name = "CLAIM_DESCRIPTION")
    private String claimDescription;

}
