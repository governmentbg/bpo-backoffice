package bg.duosoft.ipas.core.model.plant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CPlantTaxonNomenclature implements Serializable {

    private static final long serialVersionUID = -4985662158486518798L;

    private Long id;

    private String taxonCode;

    private String commonClassifyBul;

    private String commonClassifyEng;

    private String latinClassify;

}
