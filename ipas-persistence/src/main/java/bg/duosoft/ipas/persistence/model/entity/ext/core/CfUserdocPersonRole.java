package bg.duosoft.ipas.persistence.model.entity.ext.core;

import bg.duosoft.ipas.enums.UserdocPersonRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_USERDOC_PERSON_ROLE", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfUserdocPersonRole implements Serializable {
    @Id
    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private UserdocPersonRole role;

    @Column(name = "IND_TAKE_FROM_OWNER")
    private String indTakeFromOwner;

    @Column(name = "IND_TAKE_FROM_REPRESENTATIVE")
    private String indTakeFromRepresentative;

    @Column(name = "IND_ADDITIONAL_OFFIDOC_CORRESPONDENT")
    private String indAdditionalOffidocCorrespondent;

    @Column(name = "NAME")
    private String name;

}
