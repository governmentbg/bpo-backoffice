package bg.duosoft.ipas.core.model.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CProcessId implements Serializable {
    private static final long serialVersionUID = 9146289069889306507L;
    private String processType;
    private Integer processNbr;

}

