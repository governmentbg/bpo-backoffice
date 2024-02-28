package bg.duosoft.ipas.enums;

public enum ProcessEventType {
    NOTE("NOTE"),
    ACTION("ACTION"),
    USERDOC("USERDOC"),
    MANUAL("MANUAL");

    ProcessEventType(String name) {
        this.name = name;
    }

    private String name;

    public String code() {
        return name;
    }
}
