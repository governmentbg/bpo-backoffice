package bg.duosoft.ipas.core.model.journal;

import lombok.Data;

import java.io.Serializable;

@Data
public class CPdfFile implements Serializable {
    private Integer pdfNbr;
    private byte[] pdfContent;
}
