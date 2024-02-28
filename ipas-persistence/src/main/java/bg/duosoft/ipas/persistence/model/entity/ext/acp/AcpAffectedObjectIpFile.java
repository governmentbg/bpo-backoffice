package bg.duosoft.ipas.persistence.model.entity.ext.acp;

import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "IP_FILE", schema = "IPASPROD")
@Cacheable(value = false)
public class AcpAffectedObjectIpFile implements Serializable {

    @EmbeddedId
    private IpFilePK pk;

    @Column(name = "REGISTRATION_NBR")
    private Integer registrationNbr;

    @Column(name = "TITLE")
    private String title;

}
