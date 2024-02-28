package bg.duosoft.ipas.enums;

public enum ErrorLogAbout {
    IPAS("IPAS"),
    EF_USERCHANGE("EF_USERCHANGE"),
    ABDOCS("ABDOCS");

    private final String value;

    ErrorLogAbout(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
