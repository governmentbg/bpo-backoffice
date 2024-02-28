package bg.duosoft.ipas.persistence.model.entity.ext.acp;

import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "ACP_DETAILS", schema = "EXT_CORE")
@Cacheable(value = false)
public class AcpDetails implements Serializable {

    @EmbeddedId
    private IpFilePK pk;

    @Column(name = "AFFECTED_OBJECT_OTHERS")
    private String affectedObjectOthers;
}
