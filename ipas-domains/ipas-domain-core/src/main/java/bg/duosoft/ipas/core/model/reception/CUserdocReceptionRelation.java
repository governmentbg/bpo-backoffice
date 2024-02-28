package bg.duosoft.ipas.core.model.reception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class CUserdocReceptionRelation implements Serializable {
    private String mainType;
    private String userdocType;
    private String userdocName;
    private Boolean isVisible;
}
