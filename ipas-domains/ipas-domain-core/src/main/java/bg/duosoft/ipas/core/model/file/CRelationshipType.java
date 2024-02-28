package bg.duosoft.ipas.core.model.file;

import lombok.Data;

import java.io.Serializable;

@Data
public class CRelationshipType implements Serializable {
    private String relationshipType;
    private String relationshipName;
    private String relationshipDirectName;
    private String relationshipInverseName;
}


