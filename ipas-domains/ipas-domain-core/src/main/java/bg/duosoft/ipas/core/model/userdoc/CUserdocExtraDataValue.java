package bg.duosoft.ipas.core.model.userdoc;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Builder
public class CUserdocExtraDataValue implements Serializable {
    private String textValue;
    private Integer numberValue;
    private Date dateValue;
    private Boolean booleanValue;
}


