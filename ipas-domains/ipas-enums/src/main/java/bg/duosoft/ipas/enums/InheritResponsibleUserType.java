package bg.duosoft.ipas.enums;

public enum InheritResponsibleUserType {

    NONE("NONE", "Няма зададена конфигурация"),
    ALL("ALL", "Всички"),
    REGISTERED_IPOBJECTS("REGISTERED_IPOBJECTS", "Регистрирани обекти на ИС"),
    UNREGISTERED_IPOBJECTS("UNREGISTERED_IPOBJECTS", "Заявки");



    private String code;
    private String description;

    InheritResponsibleUserType(String code, String description) {
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
