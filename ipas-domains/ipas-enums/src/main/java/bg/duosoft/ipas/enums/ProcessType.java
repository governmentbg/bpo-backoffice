package bg.duosoft.ipas.enums;

public enum ProcessType {
    PATENT(10),
    UTILITY_MODEL(11),
    NATIONAL_SINGLE_DESIGN_PROCESS_TYPE(27),
    INTERNATIONAL_SINGLE_DESIGN_PROCESS_TYPE(28),
    ACP(29);

    ProcessType(Integer code) {
        this.code = code;
    }

    private Integer code;

    public Integer code() {
        return code;
    }
}
