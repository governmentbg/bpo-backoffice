package bg.duosoft.ipas.persistence.model.entity.vw.ind;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 12.03.2021
 * Time: 16:55
 */
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class VwSpcIndex {
    @Column(name = "BG_PERMIT_NUMBER")
    private String bgPermitNumber;

    @Column(name = "BG_PERMIT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bgPermitDate;

    @Column(name = "EU_PERMIT_NUMBER")
    private String euPermitNumber;

    @Column(name = "EU_PERMIT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date euPermitDate;

}
