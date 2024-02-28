package com.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum MarkPanel {
    IdentityData(GeneralPanel.IdentityData.code()),
    MainData("mark-main-data"),
    InternationalData("mark-international-data"),
    AcpAffectedObjectsData("acp-affected-objects-data"),
    AcpTakenItemsData("acp-taken-items-data"),
    AcpCheckData("acp-check-data"),
    AcpAdministrativePenaltyData("acp-administrative-penalty-data"),
    AcpViolationPlacesData("acp-violation-places-data"),
    NiceClasses("mark-nice-data"),
    InternationalNiceClassesRestrictions("mark-international-nice-data"),
    Persons(GeneralPanel.Persons.code()),
    Claims("mark-claims-data"),
    Publication("mark-publication-data"),
    Payments(GeneralPanel.Payments.code()),
    Process(GeneralPanel.Process.code()),
    Recordals(GeneralPanel.Recordals.code()),
    History(GeneralPanel.History.code());

    MarkPanel(String code) {
        this.code = code;
    }

    private String code;

    /**
     * Method that returns the code
     *
     * @return the code
     */
    public String code() {
        return code;
    }


    public static MarkPanel selectByCode(String code) {
        MarkPanel panel = Arrays.stream(MarkPanel.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(panel))
            throw new RuntimeException("Canno find mark panel with code: " + code);

        return panel;
    }

}
