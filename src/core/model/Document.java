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
    @Override
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

    public static void newDoc (String[] commandElements, CVFS cvfs) {
        if (commandElements.length < 4)
            throw new IllegalArgumentException("Incorrect number of parameters. Command formula: newDoc [docName] [docType] [docContent]");
        if (cvfs.getDir() == null)
            throw new IllegalStateException("No disk detected. Please create a new disk.");
    
        String docName = commandElements[1];
        String docType = commandElements[2];
        StringBuilder docContentBuilder = new StringBuilder();
        if (!DocumentType.isValidType(docType))
            throw new IllegalArgumentException("Invalid document type. Allowed types are: txt, java, html, css.");
        if (docName.length() > 10)
            throw new IllegalArgumentException("Invalid document name length, must be 10 characters or less.");
        if (!DataUnit.isValidName(docName))
            throw new IllegalArgumentException("Invalid document name " + docName + ". Only English letters and digits are allowed.");
        for (int i = 3; i < commandElements.length; i++) {
            docContentBuilder.append(commandElements[i]).append(" ");
        }
        String docContent = docContentBuilder.toString().trim();
    
        try {
            Object[] resourceList = cvfs.parsePath(commandElements[1]);
            Directory dir = (Directory) resourceList[0];
            String name = (String) resourceList[1];
            dir.createNewDocument(name, DocumentType.getDocumentType(docType), docContent);
        } catch (Exception e) {
            System.err.println("Error creating document: " + e.getMessage());
        }
    }
}
