package bg.duosoft.ipas.core.model;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
public class CExtLiabilityDetail implements Serializable {

    private Integer id;
    private CDocumentId documentId;
    private CFileId fileId;
    private String liabilityCode;
    private String liabilityCodeName;
    private BigDecimal amount;
    private BigDecimal amountOutstanding;
    private Date dateCreated;
    private Integer annuityNumber;
    private Date expirationDate;
    private String lastPaymentType;
    private Date lastDatePayment;
    private String lastPayerName;
    private boolean processed;

    public String createFilingNumber() {
        return fileId != null ? fileId.createFilingNumber() : (documentId != null ? documentId.createFilingNumber() : null);
    }
}
