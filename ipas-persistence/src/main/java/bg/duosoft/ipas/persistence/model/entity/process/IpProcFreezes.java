package bg.duosoft.ipas.persistence.model.entity.process;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_PROC_FREEZES", schema = "IPASPROD")
@Cacheable(value = false)
public class IpProcFreezes implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private IpProcFreezesPK pk;

    @Column(name = "IND_FREEZE_NO_OFFIDOC")
    private String indFreezeNoOffidoc;

    @Column(name = "IND_FREEZE_CONTINUE_WHEN_END")
    private String indFreezeContinueWhenEnd;

}
