package bg.duosoft.ipas.core.model.journal;

import lombok.Data;

import java.io.Serializable;

@Data
public class CJournalElement implements Serializable {
    private Integer elementNbr;
    private String name;
    private Integer journalNbr;
    private Integer nodeNbr;
    private String procTyp;
    private Integer procNbr;
    private Integer actionNbr;
    private CPdfFile pdfFile;
}
