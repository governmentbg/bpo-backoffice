package bg.duosoft.ipas.rest.custommodel.abdocs.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RAbdocsSimpleDocumentResponse {
    private Integer docId;
    private String docDirection;
    private Integer docTypeId;
    private String docTypeName;
    private String docSubject;
    private String docStatus;
    private String regUri;
    private Date regDate;
    private List<RAbdocsDocFile> docFiles;
    private String receivedOriginalState;
}
