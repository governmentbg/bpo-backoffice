package bg.duosoft.ipas.persistence.model.entity.ext;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@MappedSuperclass
public class ExtLiabilityDetailsBase implements Serializable {

    @EmbeddedId
    private ExtLiabilityDetailsPK pk;

    @Column(name = "liability_code")
    private String liabilityCode;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "amount_outstanding")
    private BigDecimal amountOutstanding;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "annuity_number")
    private Integer annuityNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "last_payment_type")
    private String lastPaymentType;

    @Temporal(TemporalType.DATE)
    @Column(name = "last_date_payment")
    private Date lastDatePayment;

    @Column(name = "LAST_PAYER_NAME")
    private String lastPayerName;

    @Column(name = "PROCESSED")
    private boolean processed;

}
