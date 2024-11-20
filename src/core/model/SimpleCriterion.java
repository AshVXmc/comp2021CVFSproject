package core.model;

import java.io.Serializable;

public class SimpleCriterion implements Serializable {
    private String criName;
    private String attrName;
    private String op;
    private String val;
    private static SimpleCriterion isDocument = new SimpleCriterion();
    private static boolean isDocumentCriterion = false;
    private static boolean isNegationCriterion = false;

    public String getCriName() {
        return criName;
    }

    public String getAttrName() {
        return attrName;
    }

    public String getOp() {
        return op;
    }

    public String getVal() {
        return val;
    }

    public void setCriName(String criName) {
        this.criName = criName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public SimpleCriterion(String criName, String attrName, String op, String val){
        this.criName = criName;
        this.attrName = attrName;
        this.op = op;
        this.val = val;
    }
    public SimpleCriterion(String criName) {
        this.criName = criName;
    }

    public SimpleCriterion() {
        this.criName = "isDocument";
        isDocumentCriterion = true;
    }
    public SimpleCriterion(SimpleCriterion x) {
        criName = x.getCriName();
        attrName = x.getAttrName();
        op = x.getOp();
        val = x.getVal();
        isNegationCriterion = x.isNegationSimpleCriterion();
        isDocumentCriterion = x.isDocumentSimpleCriterion();
    }


    public static boolean isValidCriterion(String criName, String attrName, String op, String val)  {
        if (criName == null || attrName == null || op == null || val == null) throw new IllegalArgumentException("None of the attributes can be null.");
        return isValidCriterionName(criName) && isValidCriterionContent(attrName, op, val);
    }
    public static SimpleCriterion getIsDocument() {
        return isDocument;
    }
    public boolean isDocumentSimpleCriterion() {
        return isDocumentCriterion;
    }

    public boolean isNegationSimpleCriterion() {
        return isNegationCriterion;
    }
    public static boolean isValidCriterionContent(String attrName, String op, String val) {
        /* three types of simple criterions
         1. name, contains, "text"
         2. type, equals, "type"
         3. size, > or < or >= or <= or == or !=, (int)
        */
        switch (attrName) {
            case "name":
                return op.equals("contains") && val.matches("^\"\\S+\"$");
            case "type":
                if (op.equals("equals") && val.matches("^\"\\S+\"$")) {
                    if (val.matches("^\"(txt|java|html|css)\"$")) {
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
                boolean isValidVal = false;
                isValidOp = op.matches("^(>|<|<=|>=|==|!=)$");
                try {
                    Integer.parseInt(val);
                    isValidVal = true;
                } catch (NumberFormatException ignored) {}
                return isValidOp && isValidVal;
        }
        return false;
    }

    public static boolean isValidCriterionName(String name) {
        if (name == null) return false;
        return (name.matches("^[a-zA-Z]{2}$") || name.equals("isDocument"));
    }

    public SimpleCriterion getNegativeCriterion(String criName) {
        SimpleCriterion negativeCriterion = new SimpleCriterion(this);
        negativeCriterion.toggleNegation();
        negativeCriterion.setCriName(criName);
        return negativeCriterion;
    }

    public void toggleNegation() {
        isNegationCriterion = !isNegationCriterion;
    }

    public boolean evaluate(DataUnit d) {
        if (this.getCriName().equals("isDocument"))
            return d instanceof Document;
        switch (this.getAttrName()) {
            case "name":
                if (this.getOp().equals("contains")) {
                    return d.getName().contains(this.getVal().replace("\"", ""));
                }
                break;
            case "type":
                if (this.getOp().equals("equals") && d instanceof Document) {
                    return d.getType().toString().toLowerCase().equals(this.getVal().replace("\"", ""));
                }
                break;
            case "size":
                int dataSize = d.getSize();
                int criterionSize = Integer.parseInt(this.getVal());
                switch (this.getOp()) {
                    case ">":
                        return dataSize > criterionSize;
                    case "<":
                        return dataSize < criterionSize;
                    case ">=":
                        return dataSize >= criterionSize;
                    case "<=":
                        return dataSize <= criterionSize;
                    case "==":
                        return dataSize == criterionSize;
                    case "!=":
                        return dataSize != criterionSize;
                }
                break;
        }
        return false;
    }
}
