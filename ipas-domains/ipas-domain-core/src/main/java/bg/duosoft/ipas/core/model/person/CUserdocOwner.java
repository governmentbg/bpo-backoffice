package bg.duosoft.ipas.core.model.person;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class CUserdocOwner extends COwner implements Serializable {

    private Boolean indService;

}


