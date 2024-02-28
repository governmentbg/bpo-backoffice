package bg.duosoft.ipas.enums;

public enum RegisterToProcessType {
    NONE("NONE", "Няма зададена конфигурация"),
    TOP_PROCESS("TOP_PROCESS", "Винаги към основен процес"),
    SUB_PROCESS("SUB_PROCESS", "Винаги към под-процес"),
    REGISTERED_OBJECT_SUB_PROCESS("REGISTERED_OBJECT_SUB_PROCESS", "Към под-процес само за регистриран обект");

    private String code;
    private String description;

    RegisterToProcessType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String code() {
        return code;
    }
    public String description() {
        return description;
    }

}
