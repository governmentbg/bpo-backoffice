package bg.duosoft.ipas.core.model.process;

import lombok.*;

import java.io.Serializable;

//TODO Object package and name
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CProcessInsertActionResult implements Serializable {
    private boolean inserted;
    private String processType;
    private String statusCode;
}