package com.duosoft.ipas.util.json;


import bg.duosoft.ipas.core.model.design.CLocarnoClasses;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DesignLocarnoData {

    private String locarnoClassCode;
    private String locarnoEditionCode;
    private String locarnoName;

    public DesignLocarnoData(CLocarnoClasses cLocarnoClasses) {
        this.locarnoClassCode = cLocarnoClasses.getLocarnoClassCode();
        this.locarnoEditionCode=cLocarnoClasses.getLocarnoEditionCode();
        this.locarnoName = cLocarnoClasses.getLocarnoName();
    }
}
