package bg.duosoft.ipas.integration.payments.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentLiability {
    private static final String PAID = "PAID";

    private int id;

    private String referenceNumber;

    private String liabilityCode;

    private BigDecimal amount;

    private BigDecimal amountOutstanding;

    private String status;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date referenceDate;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date dateCreated;

    public boolean isPaid() {
        return PAID.equals(status);
    }

    @Override
    public String toString() {
        return "PaymentLiability{" +
                "id=" + id +
                ", referenceNumber='" + referenceNumber + '\'' +
                ", liabilityCode='" + liabilityCode + '\'' +
                ", amount=" + amount +
                ", amountOutstanding=" + amountOutstanding +
                ", status='" + status + '\'' +
                ", referenceDate=" + referenceDate +
                '}';
    }
}
