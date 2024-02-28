package bg.duosoft.ipas.persistence.model.entity.ext.acp;


import bg.duosoft.ipas.persistence.model.entity.IntellectualPropertyRepresentative;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkReprsPK;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "ACP_REPRS", schema = "EXT_CORE")
@Cacheable(value = false)
public class AcpReprs implements Serializable, IntellectualPropertyRepresentative {

    @EmbeddedId
    private IpMarkReprsPK pk;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "PERSON_NBR", referencedColumnName = "PERSON_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "ADDR_NBR", referencedColumnName = "ADDR_NBR", insertable = false, updatable = false)
    })
    private IpPersonAddresses ipPersonAddresses;

}
