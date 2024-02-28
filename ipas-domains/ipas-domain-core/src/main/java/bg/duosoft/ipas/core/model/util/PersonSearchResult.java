package bg.duosoft.ipas.core.model.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class PersonSearchResult implements Serializable {
    private Integer personNbr;
    private String personName;


    public PersonSearchResult personNbr(Integer personNbr) {
        this.personNbr = personNbr;
        return this;
    }

    public PersonSearchResult personName(String personName) {
        this.personName = personName;
        return this;
    }
}
