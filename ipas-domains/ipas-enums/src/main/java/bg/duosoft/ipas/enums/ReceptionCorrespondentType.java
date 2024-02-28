package bg.duosoft.ipas.enums;

public enum ReceptionCorrespondentType {
    APPLICANT(2),
    REPRESENTATIVE(3);

    ReceptionCorrespondentType(Integer code) {
        this.code = code;
    }

    private Integer code;

    public Integer code() {
        return code;
    }
}
