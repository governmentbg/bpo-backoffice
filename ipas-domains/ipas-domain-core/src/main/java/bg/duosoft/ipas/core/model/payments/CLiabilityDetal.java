package bg.duosoft.ipas.core.model.payments;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
public class CLiabilityDetal implements Serializable {
    private int id;
    private String referenceNumber;
    private String liabilityCode;
    private BigDecimal amount;
    private BigDecimal amountOutstanding;
    private String status;
    private Date referenceDate;
    private Date dateCreated;
    private BigDecimal paidAmount;
    private String statusName;
    private String liabilityCodeName;
    private String lastPaymentType;
    private String lastPaymentTypeName;
    private String lastPayerName;
    private Date lastDatePayment;
    private boolean paid;
    private String annuityNumber;
    private Date expirationDate;
    private String userdocNumber;
    private String userdocTypeName;
    private String userdocNotes;
    public boolean isUserdoc() {
        return userdocNumber != null && !userdocNumber.isEmpty();
    }
}
