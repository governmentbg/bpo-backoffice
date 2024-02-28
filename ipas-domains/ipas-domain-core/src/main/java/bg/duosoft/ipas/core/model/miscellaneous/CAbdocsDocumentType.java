package bg.duosoft.ipas.core.model.miscellaneous;

import bg.duosoft.ipas.enums.IpasObjectType;
import lombok.Data;

import java.io.Serializable;

@Data
public class CAbdocsDocumentType implements Serializable {
    private Integer abdocsDocTypeId;
    private String type;
    private String name;
    private IpasObjectType ipasObject;
    private Integer docRegistrationType;
}
