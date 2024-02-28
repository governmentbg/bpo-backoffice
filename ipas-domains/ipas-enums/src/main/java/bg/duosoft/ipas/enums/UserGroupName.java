package bg.duosoft.ipas.enums;

public enum UserGroupName {
    ROLE_IPAS_MANAGEMENT("ROLE_IPAS_MANAGEMENT"),
    ROLE_TMEFILING_APPLICATION("ROLE_TMEFILING_APPLICATION");

    private final String value;

    UserGroupName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
