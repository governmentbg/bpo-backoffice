package com.duosoft.ipas.webmodel.structure;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttorneyDataWebModel {
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date attorneyPowerTerm;
    private Boolean attorneyPowerTermIndefinite;
    private Boolean reauthorizationRight;
    private Boolean priorReprsRevocation;
    private String authorizationCondition;
    private Integer personKind;
    private Integer personNbr;
    private Integer addressNbr;
    private Boolean onlyActive;
}
