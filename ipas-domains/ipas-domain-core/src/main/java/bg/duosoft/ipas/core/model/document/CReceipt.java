package bg.duosoft.ipas.core.model.document;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class CReceipt
        implements Serializable {
    private static final long serialVersionUID = -9123825408709201031L;
    private String currencyType;
    private Double dReceiptAmount;
    private Date receiptDate;
    private Long receiptNbr;
    private String receiptType;

}


