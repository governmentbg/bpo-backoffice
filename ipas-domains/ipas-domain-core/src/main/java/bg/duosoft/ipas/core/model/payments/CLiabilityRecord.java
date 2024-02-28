package bg.duosoft.ipas.core.model.payments;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class CLiabilityRecord implements Serializable {
    private int id;
    private String referenceNumber;
    private String amount;
    private String amountOutstanding;
    private String dateGenerated;

}
