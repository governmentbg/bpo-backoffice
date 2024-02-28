package bg.duosoft.ipas.core.model.acp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CAcpTakenItem implements Serializable {
    private Integer id;
    private CAcpTakenItemType type;
    private String typeDescription;
    private Integer count;
    private CAcpTakenItemStorage storage;
    private String storageDescription;
    private Boolean forDestruction;
    private Boolean returned;
    private Boolean inStock;
}
