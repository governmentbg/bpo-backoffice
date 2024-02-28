package bg.duosoft.ipas.core.model.structure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CExtFileType implements Serializable {
    private String fileTyp;
    private String name;
    private Integer order;
}
