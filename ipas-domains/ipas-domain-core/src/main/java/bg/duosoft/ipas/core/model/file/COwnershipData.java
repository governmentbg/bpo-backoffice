package bg.duosoft.ipas.core.model.file;

import bg.duosoft.ipas.core.model.person.COwner;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class COwnershipData
        implements Serializable {
    private static final long serialVersionUID = -5788397644445277676L;
    private List<COwner> ownerList;
}


