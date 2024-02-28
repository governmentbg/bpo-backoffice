package bg.duosoft.ipas.core.model.userdoc.grounds;

import lombok.Data;

import java.io.Serializable;

@Data
public class CUserdocSubGrounds implements Serializable {
    private Integer rootGroundId;
    private Integer legalGroundTypeId;
    private String legalGroundTypeTitle;
    private String legalGroundTypeDescription;
    private String version;
}
