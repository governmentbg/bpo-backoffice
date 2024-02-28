package bg.duosoft.ipas.rest.custommodel.abdocs.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RAbdocsDocFile {
    private Integer Id;
    private Integer docId;
    private Integer dbId;
    private UUID key;
    private String name;
    private String docFileVisibility;
    private String description;
    private boolean isPrimary;
}
