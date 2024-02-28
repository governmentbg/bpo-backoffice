package bg.duosoft.ipas.core.model.mark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CViennaClassSimple
        implements Serializable {
    private static final long serialVersionUID = 1746185971152494873L;
    private String viennaClass;
    private String viennaDescription;
}


