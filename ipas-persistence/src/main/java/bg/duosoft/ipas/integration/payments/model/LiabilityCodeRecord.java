package bg.duosoft.ipas.integration.payments.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiabilityCodeRecord {

    private String liabilityCode;
    private String description;
    private Integer isAnnuity;
    private String feeTypeCode;
    private Integer annuityYears;
    private Integer requireLiability;

}
