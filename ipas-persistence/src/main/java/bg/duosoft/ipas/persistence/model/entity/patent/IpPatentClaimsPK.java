package bg.duosoft.ipas.persistence.model.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.FileSeqTypSerNbrPK;
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
public class IpPatentClaimsPK implements Serializable, FileSeqTypSerNbrPK {

    @Column(name = "FILE_SEQ")
    private String fileSeq;

    @Column(name = "FILE_TYP")
    private String fileTyp;

    @Column(name = "FILE_SER")
    private Integer fileSer;

    @Column(name = "FILE_NBR")
    private Integer fileNbr;

    @Column(name = "LANGUAGE_CODE")
    private String languageCode;

    @Column(name = "CLAIM_NBR")
    private Integer claimNbr;

}
