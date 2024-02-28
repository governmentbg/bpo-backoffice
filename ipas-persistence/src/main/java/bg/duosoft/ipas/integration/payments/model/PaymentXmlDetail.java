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
public class PaymentXmlDetail {
    private Integer id;
    private Integer paymentId;
    private String cdAccounting;
    private String idAppli;
    private String idFee;
    private String idDomain;
    private String noDocacc;
    private BigDecimal paymentAmount;
    private String comment;
    private Integer importedFileId;
    private String referenceNumber;
    private BigDecimal amountOutstanding;
    private String paymentReferenceNumber;
    @JsonDeserialize(using = DetailDateDeserializer.class)
    private Date datePayment;
    private String paymentType;
    private String paymentTypeName;
    private String payerName;
}
