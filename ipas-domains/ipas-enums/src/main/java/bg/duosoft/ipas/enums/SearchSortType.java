package bg.duosoft.ipas.enums;

public enum SearchSortType {

    ASC("asc"),
    DESC("desc"),
    NONE("");

    SearchSortType(String name) {
        this.name = name;
    }

    private String name;

    public String toString() {
        return name;
    }
}
