package bg.duosoft.ipas.model;

public class JavaFile {
    private String content;
    private String name;

    public JavaFile(String content, String name) {
        this.content = content;
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }
}
