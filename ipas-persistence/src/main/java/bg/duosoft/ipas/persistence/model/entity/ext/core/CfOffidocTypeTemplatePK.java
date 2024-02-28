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
public class CfOffidocTypeTemplatePK implements Serializable {

    @Column(name = "OFFIDOC_TYP")
    private String offidocTyp;

    @Column(name = "NAME_WFILE")
    private String nameWfile;

}
