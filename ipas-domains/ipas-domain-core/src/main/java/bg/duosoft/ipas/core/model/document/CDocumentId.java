package bg.duosoft.ipas.core.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CDocumentId implements Serializable {

    private static final long serialVersionUID = 7894619428309533742L;
    private String docOrigin;
    private String docLog;
    private Integer docSeries;
    private Integer docNbr;

    public String createFilingNumber() {
        return docOrigin + "/" + docLog + "/" + docSeries + "/" + docNbr;
    }

}


