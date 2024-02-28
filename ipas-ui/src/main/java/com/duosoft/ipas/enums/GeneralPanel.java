package com.duosoft.ipas.enums;

public enum GeneralPanel {
    IdentityData("object-identity-data"),
    Persons("object-person-data"),
    Payments("object-payments-data"),
    Process("object-process-data"),
    Recordals("object-recordal-data"),
    History("object-history-data");

    GeneralPanel(String code) {
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
