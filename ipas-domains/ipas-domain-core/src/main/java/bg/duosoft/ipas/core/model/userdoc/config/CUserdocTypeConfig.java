package bg.duosoft.ipas.core.model.userdoc.config;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CUserdocTypeConfig implements Serializable {
    private String userdocTyp;
    private String registerToProcess;
    private String markInheritResponsibleUser;
    private String inheritResponsibleUser;
    private String abdocsUserTargetingOnRegistration;
    private Boolean abdocsUserTargetingOnResponsibleUserChange;
    private List<CUserdocTypeDepartment> departments;
    private Boolean hasPublicLiabilities;
}
