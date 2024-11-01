package core.model;

public class Document {
    private final String name;
    private final String type;
    private final String content;

    public Document(String type, String content, String name) {
        this.name = name;
        this.type = type;
        this.content = content;
    }
}
