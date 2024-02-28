package com.duosoft.ipas.webmodel.patent_certificates;

import bg.duosoft.ipas.core.model.CExtLiabilityDetail;
import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PatentCertificatePaidFeesProgressResult {
    private boolean generatedSuccessfully;
    private CExtLiabilityDetail extLiabilityDetail;
    private Integer documentId;
    private String errorMessage;
}
