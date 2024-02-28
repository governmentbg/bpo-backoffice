package bg.duosoft.ipas.enums;

public enum ErrorLogPriority {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    VERY_HIGH("VERY HIGH");

    private final String value;

    ErrorLogPriority(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
