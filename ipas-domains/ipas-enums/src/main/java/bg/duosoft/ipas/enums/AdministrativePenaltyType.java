package bg.duosoft.ipas.enums;

public enum AdministrativePenaltyType {
    FINE(1),
    PROPERTY_SANCTION(2),
    MINOR_CASE(3),
    OTHERS(4);


    AdministrativePenaltyType(Integer code) {
        this.code = code;
    }

    private Integer code;

    public Integer code() {
        return code;
    }

}
