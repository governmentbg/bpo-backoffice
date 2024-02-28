package bg.duosoft.ipas.rest.model.offidoc;

import bg.duosoft.ipas.enums.OffidocTypeTemplateConfig;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ROffidocTypeTemplate {
    private String nameWFile;
    private OffidocTypeTemplateConfig nameFileConfig;
}
