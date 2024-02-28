package bg.duosoft.ipas.core.model.acp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CAcpAdministrativePenalty implements Serializable {
    private CAcpAdministrativePenaltyType penaltyType;
    private CAcpAdministrativePenaltyPaymentStatus paymentStatus;
    private BigDecimal amount;
    private BigDecimal partiallyPaidAmount;
    private Date notificationDate;
    private String otherTypeDescription;
}
