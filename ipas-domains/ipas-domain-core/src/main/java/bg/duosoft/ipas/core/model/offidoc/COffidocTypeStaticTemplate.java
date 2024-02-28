package bg.duosoft.ipas.core.model.offidoc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class COffidocTypeStaticTemplate implements Serializable {
    private String offidocType;
    private String staticFileName;
}
