package bg.duosoft.ipas.rest.model.mark;

import bg.duosoft.ipas.rest.model.document.RDocumentId;
import bg.duosoft.ipas.rest.model.file.RFileId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RInternationalNiceClass implements Serializable {
    private String tagCode;
    private String tagDescription;
    private Long niceClassCode;
    private String niceClassDescription;
    private List<RTerm> terms;

    public List<RTerm> getTerms() {
        if (terms == null) {
            terms = new ArrayList<>();
        }
        return terms;
    }
}
