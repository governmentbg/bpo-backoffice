package com.duosoft.ipas.util.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserdocTypeConfigurationData {
    private String userdocType;
    private String registerToProcess;
    private String markInheritResponsibleUser;
    private String inheritResponsibleUser;
    private Integer abdocsUserTargetingOnRegistrationId;
    private Boolean abdocsUserTargetingOnResponsibleUserChange;
    private List<String> departmentIds;
    private boolean hasPublicLiabilities;

}
