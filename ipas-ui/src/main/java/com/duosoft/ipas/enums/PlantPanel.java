package com.duosoft.ipas.enums;

public enum PlantPanel {
    IdentityData("plant-identity-data"),
    MainData("plant-main-data"),
    Persons("plant-persons-data"),
    Priorities("plant-priorities-data"),
    Publication("plant-publication-data");

    PlantPanel(String code) {
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
}
