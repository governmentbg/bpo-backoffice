package bg.duosoft.ipas.core.model.plant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CPlant implements Serializable {

    private static final long serialVersionUID = -4985662158486518798L;

    private String taxonCode;

    private String proposedDenomination;

    private String proposedDenominationEng;

    private String publDenomination;

    private String publDenominationEng;

    private String apprDenomination;

    private String apprDenominationEng;

    private String rejDenomination;

    private String rejDenominationEng;

    private String features;

    private String stability;

    private String testing;

    private CPlantTaxonNomenclature plantNumenclature;

}
