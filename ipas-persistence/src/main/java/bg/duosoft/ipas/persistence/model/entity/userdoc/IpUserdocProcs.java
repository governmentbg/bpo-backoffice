package bg.duosoft.ipas.persistence.model.entity.userdoc;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_USERDOC_PROCS", schema = "IPASPROD")
public class IpUserdocProcs implements Serializable {
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private IpUserdocProcsPK pk;

    @Column(name = "PROC_TYP")
    private String procTyp;

    @Column(name = "PROC_NBR")
    private Integer procNbr;
}
