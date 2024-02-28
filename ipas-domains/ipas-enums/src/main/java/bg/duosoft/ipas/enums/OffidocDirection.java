package bg.duosoft.ipas.enums;

public enum OffidocDirection {
    INTERNAL_DOCUMENT("I"),
    EXTERNAL_DOCUMENT("O");

    private final String value;

    OffidocDirection(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
