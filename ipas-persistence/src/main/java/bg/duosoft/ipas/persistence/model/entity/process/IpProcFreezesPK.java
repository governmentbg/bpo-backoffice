package bg.duosoft.ipas.persistence.model.entity.process;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class IpProcFreezesPK implements Serializable {

    @Column(name = "FROZEN_PROC_TYP")
    private String frozenProcTyp;

    @Column(name = "FROZEN_PROC_NBR")
    private Integer frozenProcNbr;

    @Column(name = "FREEZING_PROC_TYP")
    private String freezingProcTyp;

    @Column(name = "FREEZING_PROC_NBR")
    private Integer freezingProcNbr;

}
