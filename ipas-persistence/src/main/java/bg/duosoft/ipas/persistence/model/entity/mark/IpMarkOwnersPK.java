package bg.duosoft.ipas.persistence.model.entity.mark;

import bg.duosoft.ipas.persistence.model.entity.IntellectualPropertyOwnerPK;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class IpMarkOwnersPK implements Serializable, IntellectualPropertyOwnerPK {

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

}
