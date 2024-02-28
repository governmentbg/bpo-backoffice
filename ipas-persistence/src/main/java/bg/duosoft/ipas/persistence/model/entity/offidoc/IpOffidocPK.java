package bg.duosoft.ipas.persistence.model.entity.offidoc;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@ToString
public class IpOffidocPK implements Serializable {

    @Column(name = "OFFIDOC_ORI")
    private String offidocOri;

    @Column(name = "OFFIDOC_SER")
    private Integer offidocSer;

    @Column(name = "OFFIDOC_NBR")
    private Integer offidocNbr;
}
