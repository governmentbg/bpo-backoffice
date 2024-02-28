package bg.duosoft.ipas.core.model.userdoc.grounds;

import lombok.Data;

import java.io.Serializable;

@Data
public class CLegalGroundTypes implements Serializable {
    private Integer id;
    private String title;
    private String description;
}
