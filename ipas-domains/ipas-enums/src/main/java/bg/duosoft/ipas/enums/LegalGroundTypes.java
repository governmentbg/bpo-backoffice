package bg.duosoft.ipas.enums;

public enum LegalGroundTypes {
    EARLIER_MARK(109),
    EARLIER_CO_MARK(111),
    NOT_NEW_DESIGN(89),
    NOT_ORIGINAL_DESIGN_13(91),
    NOT_ORIGINAL_DESIGN_13A(93),
    PUBLIC_BEFORE_FILING_DATE(101),
    DECLARED_BEFORE_REGISTRATION(103);

    LegalGroundTypes(Integer code) { this.code = code;}
    private Integer code;
    public Integer code() {
        return code;
    }

}
