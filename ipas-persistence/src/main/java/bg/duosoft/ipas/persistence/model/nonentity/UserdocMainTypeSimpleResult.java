package bg.duosoft.ipas.persistence.model.nonentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserdocMainTypeSimpleResult {
    private String mainType;
    private String mainTypeName;
    private Boolean isVisible;
}
