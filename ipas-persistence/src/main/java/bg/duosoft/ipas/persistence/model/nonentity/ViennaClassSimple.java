package bg.duosoft.ipas.persistence.model.nonentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViennaClassSimple
        implements Serializable {
    private static final long serialVersionUID = 1746185971152494873L;
    private String viennaClass;
    private String viennaDescription;
}


