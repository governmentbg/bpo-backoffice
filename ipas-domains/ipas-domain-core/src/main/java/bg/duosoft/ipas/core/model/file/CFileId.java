package bg.duosoft.ipas.core.model.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CFileId implements Serializable {
    private static final long serialVersionUID = 2145894921001877060L;
    private String fileSeq;
    private String fileType;
    private Integer fileSeries;
    private Integer fileNbr;

    public String createFilingNumber() {
        return fileSeq + "/" + fileType + "/" + fileSeries + "/" + fileNbr;
    }

}


