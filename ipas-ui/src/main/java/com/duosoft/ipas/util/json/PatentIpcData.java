package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.core.model.miscellaneous.CIpcClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class PatentIpcData {
    private String ipcEdition;
    private String ipcSection;
    private String ipcClass;
    private String ipcSubclass;
    private String ipcGroup;
    private String ipcSubgroup;
    private String ipcName;
    private String ipcQualification;
    private boolean withLatestVersion;
    private String commonSearchTitle;

    private void initBaseData(CIpcClass cIpcClass) {
        this.ipcEdition = cIpcClass.getIpcEdition();
        this.ipcSection = cIpcClass.getIpcSection();
        this.ipcClass = cIpcClass.getIpcClass();
        this.ipcSubclass = cIpcClass.getIpcSubclass();
        this.ipcGroup = cIpcClass.getIpcGroup();
        this.ipcSubgroup = cIpcClass.getIpcSubgroup();
        this.ipcName = cIpcClass.getIpcSymbolDescription();
    }

    public PatentIpcData(CIpcClass cIpcClass) {
        initBaseData(cIpcClass);
    }

    public PatentIpcData(CIpcClass cIpcClass, String latestVersion) {
        initBaseData(cIpcClass);
        this.withLatestVersion = Objects.nonNull(cIpcClass.getIpcVersionCalculated()) && latestVersion.equals(cIpcClass.getIpcVersionCalculated());
    }

    public PatentIpcData(String commonSearchTitle) {
        this.commonSearchTitle = commonSearchTitle;
    }
}
