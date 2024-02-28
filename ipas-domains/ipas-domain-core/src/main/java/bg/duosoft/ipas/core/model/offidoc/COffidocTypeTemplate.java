package bg.duosoft.ipas.core.model.offidoc;

import bg.duosoft.ipas.enums.OffidocTypeTemplateConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class COffidocTypeTemplate implements Serializable {
    private String offidocType;
    private String nameWFile;
    private OffidocTypeTemplateConfig nameFileConfig;
}
