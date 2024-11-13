package core.model;

public class Document extends DataUnit {
    private final String name;
    private final DocumentType type;
    private final String content;
    private Directory parentDir;
    public Document(DocumentType type, String content, String name, DataUnit parentDir) {
        super(name);
        setParentDir(parentDir);
        this.name = name;
        this.type = type;
        this.content = content;
        setSize(40 + content.length() * 2);
    }

    public int getDocumentSize() {
        return 40 + content.length() * 2;
    }
    public DocumentType getType() {
        return type;
    }

    @Override
    public DataUnit getParentDir() {
        return parentDir;
    }

    @Override
    public void setParentDir(DataUnit newParentDir) {
        parentDir = (Directory) newParentDir;
    }
}
