package bg.duosoft.ipas.enums;

public enum GroundCategoriesTypes {

    ABSOLUTE_GROUNDS("ABS"),
    RELATIVE_GROUNDS("REL");

    GroundCategoriesTypes(String code) { this.code = code;}
    private String code;
    public String code() {
        return code;
    }
}
