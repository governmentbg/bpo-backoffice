package bg.duosoft.ipas.core.model.userdoc;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CUserdocPersonData implements Serializable {
    private List<CUserdocPerson> personList;
}
