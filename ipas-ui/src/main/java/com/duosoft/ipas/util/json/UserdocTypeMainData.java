package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.enums.UserdocGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserdocTypeMainData {
    private String userdocType;
    private String userdocName;
    private UserdocGroup userdocGroup;
    private String generateProcType;
    private Boolean indInactive;
}
