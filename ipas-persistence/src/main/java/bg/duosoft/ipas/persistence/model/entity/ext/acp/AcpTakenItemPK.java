package bg.duosoft.ipas.persistence.model.entity.ext.acp;

import lombok.*;
import org.hibernate.search.annotations.NumericField;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Embeddable
public class AcpTakenItemPK  implements Serializable {

    @Column(name = "FILE_SEQ")
    private String fileSeq;

    @Column(name = "FILE_TYP")
    private String fileTyp;

    @Column(name = "FILE_SER")
    @NumericField
    private Integer fileSer;

    @Column(name = "FILE_NBR")
    private Integer fileNbr;

    @Column(name = "TAKEN_ITEM_ID")
    private Integer id;
}
