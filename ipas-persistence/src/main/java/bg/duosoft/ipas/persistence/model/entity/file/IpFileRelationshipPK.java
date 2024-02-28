package bg.duosoft.ipas.persistence.model.entity.file;

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
public class IpFileRelationshipPK implements Serializable {

    @Column(name = "FILE_SEQ1")
    private String fileSeq1;

    @Column(name = "FILE_TYP1")
    private String fileTyp1;

    @Column(name = "FILE_SER1")
    private Integer fileSer1;

    @Column(name = "FILE_NBR1")
    private Integer fileNbr1;

    @Column(name = "FILE_SEQ2")
    private String fileSeq2;

    @Column(name = "FILE_TYP2")
    private String fileTyp2;

    @Column(name = "FILE_SER2")
    private Integer fileSer2;

    @Column(name = "FILE_NBR2")
    private Integer fileNbr2;

    @Column(name = "RELATIONSHIP_TYP")
    private String relationshipTyp;

}
