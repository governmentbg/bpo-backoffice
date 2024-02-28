package bg.duosoft.ipas.persistence.model.entity.mark;

import lombok.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.IntegerBridge;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Embeddable
public class EnotifMarkPK implements Serializable {

    @Column(name = "ID")
    private Integer id;

    @Column(name = "FILE_SEQ")
    private String fileSeq;

    @Column(name = "FILE_TYP")
    private String fileTyp;

    @Column(name = "FILE_SER")
    private Integer fileSer;

    @Column(name = "FILE_NBR")
    private Integer fileNbr;


}
