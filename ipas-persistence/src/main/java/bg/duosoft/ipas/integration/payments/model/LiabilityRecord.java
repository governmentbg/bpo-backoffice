package bg.duosoft.ipas.integration.payments.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiabilityRecord {
    private int id;
    private String referenceNumber;
    private String amount;
    private String amountOutstanding;
    private String dateGenerated;

}
