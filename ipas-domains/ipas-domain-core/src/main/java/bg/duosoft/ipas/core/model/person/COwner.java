package bg.duosoft.ipas.core.model.person;

import lombok.Data;

import java.io.Serializable;

@Data
public class COwner
        implements Serializable {
    private static final long serialVersionUID = 557510688297572367L;
    private String ownershipNotes;
    private Integer orderNbr;
    private CPerson person;

}


