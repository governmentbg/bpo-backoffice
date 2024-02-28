package bg.duosoft.ipas.core.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CDocumentSeqId implements Serializable {
    private static final long serialVersionUID = -1754807516466295569L;
    private String docSeqType;
    private String docSeqName;
    private Integer docSeqNbr;
    private Integer docSeqSeries;
    public CDocumentSeqId(String docSeqType, Integer docSeqSeries, Integer docSeqNbr) {
        this.docSeqType = docSeqType;
        this.docSeqSeries = docSeqSeries;
        this.docSeqNbr = docSeqNbr;
    }
}


