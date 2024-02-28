package bg.duosoft.ipas.core.model.payments;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.userdoc.CUserdocHierarchyNode;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 19.08.2021
 * Time: 10:48
 */
@Data
public class CNotLinkedPayment implements Serializable {
    private String referenceNumber;
    private CFileId fileId;
    private Integer registrationNbr;
    private List<CUserdocHierarchyNode> userdocs;
    private BigDecimal paymentAmount;
    private BigDecimal amountOutstanding;
    private String payerName;
    private Date datePayment;
    private String paymentTypeName;
    private String module;

    public String getUserdocNumbersAsString() {
        return getUserdocExternalSystemIdsOrDocumentIds().stream().collect(Collectors.joining(", "));
    }

    public List<String> getUserdocExternalSystemIdsOrDocumentIds() {
        return userdocs == null ? new ArrayList<>() : userdocs.stream().map(CNotLinkedPayment::getUserdocExternalSystemIdOrDocumentId).collect(Collectors.toList());
    }

    private static String getUserdocExternalSystemIdOrDocumentId(CUserdocHierarchyNode ud) {
        return ud.getExternalSystemId() == null ? ud.getDocumentId().createFilingNumber() : ud.getDocumentId().createFilingNumber();
    }
    public boolean hasUserdocs() {
        return userdocs != null && userdocs.size() > 0;
    }

    public String createFilingNumber() {
        return fileId.createFilingNumber();
    }
}
