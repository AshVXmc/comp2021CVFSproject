package core.model;

public class SimpleCriterion {

    private String criName;
    private String attrName;
    private String op;
    private String val;
    private static SimpleCriterion isDocument = new SimpleCriterion();
    private boolean isDocumentCriterion = false;

    public SimpleCriterion(String criName, String attrName, String op, String val){
        this.criName = criName;
        this.attrName = attrName;
        this.op = op;
        this.val = val;
    }

    private SimpleCriterion() {
        this.criName = "isDocument";
        this.isDocumentCriterion = true;
    }

    public static boolean isValidCriterion(String criName, String attrName, String op, String val)  {
        if (criName == null || attrName == null || op == null || val == null) throw new IllegalArgumentException("Null criterion checked by isValidCri() checker.");
        return isValidCriterionName(criName) && isValidCriterionContent(attrName, op, val);
    }

    public static boolean isValidCriterionContent(String attrName, String op, String val) {
        switch (attrName) {
            case "name":
                return op.equals("contains") && val.matches("^\"\\S+\"$");
            case "type":
                if (op.equals("equals") && val.matches("^\"\\S+\"$")) {
                    if (val.matches("^\"(txt|html|css|java)\"$")) {
                        return true;
                    }
                    else {
                        System.err.println("Invalid file type. File must be of type txt, html, css or java.");
                        return false;
                    }
                }
                return false;

            case "size":
                boolean isValidOp;
                boolean isValidVal;
                isValidOp = op.matches("^(>|<|<=|>=|==|!=)$");
                try {
                    Integer.parseInt(val);
                    isValidVal = true;
                } catch (NumberFormatException e) {
                    isValidVal = false;
                }
                return isValidOp && isValidVal;
        }
        return false;
    }

    public static boolean isValidCriterionName(String name) {
        if (name == null) return false;
        return (name.matches("^[a-zA-Z]{2}$") || name.equals("isDocument"));
    }
}
