package bg.duosoft.ipas.core.model.util;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class TempID implements Serializable {

    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer tempID) {
        this.value = tempID;
    }
}
