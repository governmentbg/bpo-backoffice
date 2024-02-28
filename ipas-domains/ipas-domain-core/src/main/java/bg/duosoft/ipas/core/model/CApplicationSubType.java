package bg.duosoft.ipas.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class
CApplicationSubType implements Serializable {
    private String applicationType;
    private String applicationSubType;
    private String applicationSubTypeName;
}
