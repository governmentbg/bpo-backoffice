package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.core.model.miscellaneous.CCpcClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class PatentCpcData {
    private String cpcEdition;
    private String cpcSection;
    private String cpcClass;
    private String cpcSubclass;
    private String cpcGroup;
    private String cpcSubgroup;
    private String cpcName;
    private String cpcQualification;
    private boolean withLatestVersion;
    private String commonSearchTitle;


    private void initBaseData(CCpcClass cCpcClass){
        this.cpcEdition=cCpcClass.getCpcEdition();
        this.cpcSection=cCpcClass.getCpcSection();
        this.cpcClass=cCpcClass.getCpcClass();
        this.cpcSubclass=cCpcClass.getCpcSubclass();
        this.cpcGroup=cCpcClass.getCpcGroup();
        this.cpcSubgroup=cCpcClass.getCpcSubgroup();
        this.cpcName=cCpcClass.getCpcName();
    }

    public PatentCpcData(CCpcClass cCpcClass) {
        initBaseData(cCpcClass);
    }
    public PatentCpcData(CCpcClass cCpcClass,String latestVersion) {
        initBaseData(cCpcClass);
        this.withLatestVersion = Objects.nonNull(cCpcClass.getCpcVersionCalculated()) && latestVersion.equals(cCpcClass.getCpcVersionCalculated());
    }
    public PatentCpcData(String commonSearchTitle) {
        this.commonSearchTitle = commonSearchTitle;
    }
}
