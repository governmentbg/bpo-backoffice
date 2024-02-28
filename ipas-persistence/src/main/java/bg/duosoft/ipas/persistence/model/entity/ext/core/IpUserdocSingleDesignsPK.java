package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class IpUserdocSingleDesignsPK implements Serializable {

    @Column(name = "DOC_ORI")
    private String docOri;

    @Column(name = "DOC_LOG")
    private String docLog;

    @Column(name = "DOC_SER")
    private Integer docSer;

    @Column(name = "DOC_NBR")
    private Integer docNbr;

    @Column(name = "FILE_SEQ")
    private String fileSeq;

    @Column(name = "FILE_TYP")
    private String fileTyp;

    @Column(name = "FILE_SER")
    private Integer fileSer;

    @Column(name = "FILE_NBR")
    private Integer fileNbr;

}
