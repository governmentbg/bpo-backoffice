package bg.duosoft.ipas.persistence.model.nonentity;

import bg.duosoft.ipas.core.model.file.CFileId;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PatentCertificatePaidFeesResult {
    private CFileId fileId;
    private String liabilityCode;
    private String liabilityCodeName;
    private Date paymentDate;
    private BigDecimal amount;
    private String payer;
    private Integer id;
    private String statusName;
    private Integer regNumber;
}
