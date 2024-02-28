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
@Table(name = "PDF_FILE", schema = "EXT_JOURNAL")
@Cacheable(value = false)
public class PdfFile implements Serializable {
    @Id
    @Column(name = "PDF_NBR")
    private Integer pdfNbr;

    @Column(name = "PDF_CONTENT")
    private byte[] pdfContent;
}
