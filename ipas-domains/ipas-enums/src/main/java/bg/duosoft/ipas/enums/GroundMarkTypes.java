package bg.duosoft.ipas.enums;

public enum GroundMarkTypes {

    NATIONAL_MARK(1),
    INTERNATIONAL_MARK(2),
    PUBLIC_MARK(3);


    GroundMarkTypes(Integer code) { this.code = code;}
    private Integer code;
    public Integer code() {
        return code;
    }
}
