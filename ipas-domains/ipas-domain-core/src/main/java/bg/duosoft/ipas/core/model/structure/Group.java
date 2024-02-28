package bg.duosoft.ipas.core.model.structure;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 25.7.2019 г.
 * Time: 16:19
 */
@Data
public class Group implements Serializable {
    private Integer groupId;
    @NotEmpty
    private String groupName;
    private String description;
    private List<String> roleNames;
}
