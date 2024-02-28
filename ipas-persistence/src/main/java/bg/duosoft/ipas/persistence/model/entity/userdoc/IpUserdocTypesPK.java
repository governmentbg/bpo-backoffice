package bg.duosoft.ipas.persistence.model.entity.userdoc;

import lombok.*;
import org.hibernate.search.annotations.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Embeddable
public class IpUserdocTypesPK implements Serializable {

    @Column(name = "DOC_ORI")
    private String docOri;

    @Column(name = "DOC_LOG")
    private String docLog;

    @Column(name = "DOC_SER")
    private Integer docSer;

    @Column(name = "DOC_NBR")
    private Integer docNbr;

    @Column(name = "USERDOC_TYP")
    @Field(analyze = Analyze.NO, store = Store.YES)
    private String userdocTyp;
}
