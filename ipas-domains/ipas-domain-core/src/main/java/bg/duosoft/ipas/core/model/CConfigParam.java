package bg.duosoft.ipas.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class CConfigParam implements Serializable {

    private String configCode;
    private String value;

}
