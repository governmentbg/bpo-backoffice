package bg.duosoft.ipas.enums;

public enum AdministrativePenaltyPaymentStatus {
    PAID(1),
    PARTIALLY_PAID(2),
    UNPAID(3);

    AdministrativePenaltyPaymentStatus(Integer code) {
        this.code = code;
    }

    private Integer code;

    public Integer code() {
        return code;
    }

}
