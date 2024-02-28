package bg.duosoft.ipas.core.model.payments;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 18.08.2021
 * Time: 16:06
 */
@Data
public class CPaymentXmlDetail implements Serializable {
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
    private Date datePayment;
    private String paymentType;
    private String paymentTypeName;
    private String payerName;
    private String module;
}
