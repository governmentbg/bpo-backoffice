package bg.duosoft.ipas.persistence.model.entity.mark;

import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_MARK_REGULATION", schema = "IPASPROD")
@Cacheable(value = false)
public class IpMarkRegulation implements Serializable {
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private IpFilePK pk;

    @Column(name = "REGULATIONS_DESCRIPTION")
    private String regulationsDescription;

}
