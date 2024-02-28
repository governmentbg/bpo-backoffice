package bg.duosoft.ipas.persistence.model.entity.journal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "JOURNAL_ELEMENT", schema = "EXT_JOURNAL")
@Cacheable(value = false)
public class JournalElement implements Serializable {
    @Id
    @Column(name = "ELEMENT_NBR")
    private Integer elementNbr;

    @Column(name = "NAME")
    private String name;

    @Column(name = "JOURNAL_NBR")
    private Integer journalNbr;

    @Column(name = "JOURNAL_STRUCT_NODE_NBR")
    private Integer nodeNbr;

    @Column(name = "PROC_TYP")
    private String procTyp;

    @Column(name = "PROC_NBR")
    private Integer procNbr;

    @Column(name = "ACTION_NBR")
    private Integer actionNbr;

    @OneToOne
    @JoinColumn(name = "PDF_FILE_NBR", referencedColumnName = "PDF_NBR", updatable = false, insertable = false)
    private PdfFile pdfFile;
}
