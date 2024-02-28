package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.util.date.DateUtils;
import com.duosoft.ipas.util.CoreUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class AcpAdministrativeData {
    private Integer penaltyType;
    private String otherTypeDescription;
    private String amount;
    private BigDecimal amountAsDecimal;
    private String partiallyPaidAmount;
    private BigDecimal partiallyPaidAmountAsDecimal;
    private Integer paymentStatus;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date notificationDate;

    public void convertAmounts() {
        this.amountAsDecimal = CoreUtils.isBigDecimal(amount) ? new BigDecimal(amount) : null;
        this.partiallyPaidAmountAsDecimal = CoreUtils.isBigDecimal(partiallyPaidAmount) ? new BigDecimal(partiallyPaidAmount) : null;
    }
}
