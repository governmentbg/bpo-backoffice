package bg.duosoft.ipas.core.model.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CUser implements Serializable {
    private static final long serialVersionUID = 4128027489050691627L;
    private String userName;
    private Integer userId;
    private Boolean indInactive;
}