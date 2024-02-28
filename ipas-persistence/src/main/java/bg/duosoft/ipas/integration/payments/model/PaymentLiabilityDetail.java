package bg.duosoft.ipas.integration.payments.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentLiabilityDetail extends PaymentLiability {
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

    @JsonDeserialize(using = DetailDateDeserializer.class)
    public void setReferenceDate(Date referenceDate) {
        super.setReferenceDate(referenceDate);
    }

    @JsonDeserialize(using = DetailDateDeserializer.class)
    public void setDateCreated(Date dateCreated) {
        super.setDateCreated(dateCreated);
    }

    @JsonDeserialize(using = DetailDateDeserializer.class)
    public void setLastDatePayment(Date lastDatePayment) {
        this.lastDatePayment = lastDatePayment;
    }

    @JsonDeserialize(using = DetailDateDeserializer.class)
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public boolean isPaid() {
        return paid;
    }


}
