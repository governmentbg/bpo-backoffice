package bg.duosoft.ipas.rest.custommodel.offidoc;

import bg.duosoft.ipas.rest.custommodel.abdocs.document.RAbdocsDocFile;
import bg.duosoft.ipas.rest.model.offidoc.ROffidocAbdocsDocument;
import bg.duosoft.ipas.rest.model.offidoc.ROffidocType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ROffidocAbdocsDataResponse {

    private ROffidocAbdocsDocument offidocAbdocsDocument;
    private ROffidocType offidocType;
    private List<RAbdocsDocFile> files;
    private String closestMainParentObjectRegistrationNumber;
    private String documentSubject;

}
