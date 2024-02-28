package bg.duosoft.ipas.enums;

public enum FilePublicationTyp {
    ELECTRONIC("1","Електронно"),
    MIXED("2","Смесено"),
    PAPER("3","Хартиено");

    private String code;
    private String description;

    FilePublicationTyp(String code, String description) {
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
