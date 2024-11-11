package core.model;

public enum DocumentType {
    TXT("txt"),
    JAVA("java"),
    HTML("html"),
    CSS("css");

    private final String type;

    DocumentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    // Static method to validate if a string is a valid document type
    public static boolean isValidType(String type) {
        for (DocumentType docType : DocumentType.values()) {
            if (docType.getType().equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }
}