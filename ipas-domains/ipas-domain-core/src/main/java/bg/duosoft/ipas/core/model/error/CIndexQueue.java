package bg.duosoft.ipas.core.model.error;

import bg.duosoft.ipas.enums.ErrorLogAbout;
import bg.duosoft.ipas.enums.ErrorLogPriority;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CIndexQueue implements Serializable {
    private Integer id;
    private String type;
    private Date insertedAt;
    private String operation;
    private Boolean checked;

    public CIndexQueue id(Integer id) {
        this.id = id;

        return this;
    }

    public CIndexQueue type(String type) {
        this.type = type;

        return this;
    }

    public CIndexQueue insertedAt(Date insertedAt) {
        this.insertedAt = insertedAt;

        return this;
    }

    public CIndexQueue operation(String operation) {
        this.operation = operation;

        return this;
    }

    public CIndexQueue checked(Boolean checked) {
        this.checked = checked;

        return this;
    }
}
