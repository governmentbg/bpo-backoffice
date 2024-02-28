package bg.duosoft.ipas.core.model.userdoc.config;

import bg.duosoft.ipas.core.model.structure.OfficeDepartment;
import lombok.Data;

import java.io.Serializable;

@Data
public class CUserdocTypeDepartment implements Serializable {
    private String userdocTyp;
    private OfficeDepartment department;
}

