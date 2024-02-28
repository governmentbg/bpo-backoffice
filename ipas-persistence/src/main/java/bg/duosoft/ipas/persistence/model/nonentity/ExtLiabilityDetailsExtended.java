package bg.duosoft.ipas.persistence.model.nonentity;

import bg.duosoft.ipas.persistence.model.entity.ext.ExtLiabilityDetailsBase;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@EqualsAndHashCode
public class ExtLiabilityDetailsExtended extends ExtLiabilityDetailsBase {

    @Column(name = "EXTERNAL_SYSTEM_ID")
    private String externalSystemId;

}
