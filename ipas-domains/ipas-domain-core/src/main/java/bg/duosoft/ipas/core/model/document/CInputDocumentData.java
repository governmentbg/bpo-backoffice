package bg.duosoft.ipas.core.model.document;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class CInputDocumentData
        implements Serializable {
    private static final long serialVersionUID = 3551343250026856083L;
    private List<CReceipt> receiptList;
}


