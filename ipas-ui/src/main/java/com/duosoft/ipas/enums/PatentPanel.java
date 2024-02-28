package com.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum PatentPanel {
    IdentityData(GeneralPanel.IdentityData.code()),
    SpcIdentityData("spc-" + GeneralPanel.IdentityData.code()),
    MainData("patent-main-data"),
    PlantMainData("plant-main-data"),
    SpcMainData("spc-main-data"),
    PublishedDrawingsData("published-drawings-data"),
    DesignDrawingsData("design-drawings-data"),
    ClaimsData("claims-data"),
    CitationsData("patent-citations-data"),
    RightsData("patent-rights-data"),
    Persons(GeneralPanel.Persons.code()),
    Payments(GeneralPanel.Payments.code()),
    Publication("patent-publication-data"),
    IpcData("patent-ipc-data"),
    CpcData("patent-cpc-data"),
    Process(GeneralPanel.Process.code()),
    Recordals(GeneralPanel.Recordals.code()),
    History(GeneralPanel.History.code()),
    Citations("patent-citations-data");

    PatentPanel(String code) {
        this.code = code;
    }

    private String code;

    public String code() {
        return code;
    }


    public static PatentPanel selectByCode(String code) {
        PatentPanel panel = Arrays.stream(PatentPanel.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(panel))
            throw new RuntimeException("Canno find patent panel with code: " + code);

        return panel;
    }

}
