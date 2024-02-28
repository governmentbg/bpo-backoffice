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
@Table(name = "ACP_SERVICE_PERSON", schema = "EXT_CORE")
@Cacheable(value = false)
public class AcpServicePerson implements Serializable {

    @EmbeddedId
    private IpFilePK pk;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "SERVICE_ADDR_NBR", referencedColumnName = "ADDR_NBR"),
            @JoinColumn(name = "SERVICE_PERSON_NBR", referencedColumnName = "PERSON_NBR")
    })
    private IpPersonAddresses servicePerson;

}
