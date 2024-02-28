package bg.duosoft.ipas.persistence.model.entity.userdoc;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Embeddable
public class IpUserdocProcsPK implements Serializable {

    @Column(name = "DOC_ORI")
    private String docOri;

    @Column(name = "DOC_LOG")
    private String docLog;

    @Column(name = "DOC_SER")
    private Integer docSer;

    @Column(name = "DOC_NBR")
    private Integer docNbr;

    @Column(name = "USERDOC_TYP")
    private String userdocTyp;

    @Column(name = "USERDOC_FILE_SEQ")
    private String userdocFileSeq;

    @Column(name = "USERDOC_FILE_TYP")
    private String userdocFileTyp;

    @Column(name = "USERDOC_FILE_SER")
    private Integer userdocFileSer;

    @Column(name = "USERDOC_FILE_NBR")
    private Integer userdocFileNbr;
}
