package bg.duosoft.ipas.persistence.model.entity.ext.core;

import bg.duosoft.ipas.enums.UserdocPersonRole;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CfUserdocTypeToPersonRolePK implements Serializable {

    @Column(name = "USERDOC_TYP")
    private String userdocTyp;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private UserdocPersonRole role;
}
