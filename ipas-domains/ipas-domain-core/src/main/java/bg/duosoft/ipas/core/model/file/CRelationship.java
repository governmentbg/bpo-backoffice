package bg.duosoft.ipas.core.model.file;

import lombok.Data;

import java.io.Serializable;


@Data
public class CRelationship
        implements Serializable {
    private static final long serialVersionUID = 1711216392705688906L;
    private String relationshipType;
    private String relationshipRole;
    private CFileId fileId;
}


