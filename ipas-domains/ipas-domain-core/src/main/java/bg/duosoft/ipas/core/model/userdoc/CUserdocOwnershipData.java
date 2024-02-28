package bg.duosoft.ipas.core.model.userdoc;

import bg.duosoft.ipas.core.model.person.CUserdocOwner;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CUserdocOwnershipData implements Serializable {
    private List<CUserdocOwner> ownerList;
}


