package bg.duosoft.ipas.persistence.model.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.IntellectualPropertyRepresentativePK;
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
public class IpPatentReprsPK implements Serializable, IntellectualPropertyRepresentativePK {

    @Column(name = "FILE_SEQ")
    private String fileSeq;

    @Column(name = "FILE_TYP")
    private String fileTyp;

    @Column(name = "FILE_SER")
    private Integer fileSer;

    @Column(name = "FILE_NBR")
    private Integer fileNbr;

    @Column(name = "PERSON_NBR")
    private Integer personNbr;

    @Column(name = "ADDR_NBR")
    private Integer addrNbr;

    @Column(name = "REPRESENTATIVE_TYP")
    private String representativeTyp;

}
