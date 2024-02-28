package bg.duosoft.ipas.persistence.model.entity.ext.acp;

import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "ACP_INFRINGER", schema = "EXT_CORE")
@Cacheable(value = false)
public class AcpInfringerPerson implements Serializable {

    @EmbeddedId
    private IpFilePK pk;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "INFRINGER_ADDR_NBR", referencedColumnName = "ADDR_NBR"),
            @JoinColumn(name = "INFRINGER_PERSON_NBR", referencedColumnName = "PERSON_NBR")
    })
    private IpPersonAddresses infringerPerson;

}
