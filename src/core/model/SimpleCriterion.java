package core.model;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.Objects;

public class SimpleCriterion {

    private String criName;
    private String attrName;
    private String op;
    private String val;
    private static SimpleCriterion isDocument = new SimpleCriterion();
    private static boolean isDocumentCriterion = false;
    private static boolean isNegationCriterion = false;
    private final static ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("js");

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

    public boolean checkIfMeetsCriterion(DataUnit dataUnit, SimpleCriterion criterion) {
        if (dataUnit == null)
            throw new RuntimeException("Value of data unit is null.");
        if (isDocumentCriterion)
            return (dataUnit instanceof Document);
        if (criterion instanceof BinaryCriterion) {
            // Binary criterion
            SimpleCriterion criterion1 = ((BinaryCriterion) criterion).getCriterion1();
            SimpleCriterion criterion2 = ((BinaryCriterion) criterion).getCriterion2();
            String logicOp = ((BinaryCriterion) criterion).getLogicOp();
            if (Objects.equals(logicOp, "&&"))
                return checkIfMeetsCriterion(dataUnit, criterion1) && checkIfMeetsCriterion(dataUnit, criterion2);
            if (Objects.equals(logicOp, "||"))
                return checkIfMeetsCriterion(dataUnit, criterion1) || checkIfMeetsCriterion(dataUnit, criterion2);
        }
        else {
            // Simple criterion
            // 1. name contains "something"
            if (Objects.equals(criterion.getAttrName(), "name")) {
                String name = dataUnit.getName();
                String val = criterion.getVal().replace("\"", "");
                return name.contains(val);
            }
            // 2. type equals "txt/html/css/java"
            else if (dataUnit instanceof Document  && Objects.equals(criterion.getAttrName(), "type")) {
                String docType = String.valueOf(((Document) dataUnit).getType()).toLowerCase();
                String requiredType = criterion.getVal().replace("\"", "");
                return Objects.equals(docType, requiredType);
            }
            // 3. size >/</<=/>=/==/!= [int]
            else if (Objects.equals(criterion.getAttrName(), "size")) {
                int fileSize = dataUnit.getSize();
                int requiredSize = Integer.parseInt(criterion.getVal());
                return switch (criterion.getOp()) {
                    case ">" -> fileSize > requiredSize;
                    case "<" -> fileSize < requiredSize;
                    case "!=" -> fileSize != requiredSize;
                    case ">=" -> fileSize >= requiredSize;
                    case "<=" -> fileSize <= requiredSize;
                    case "==" -> fileSize == requiredSize;
                    default -> throw new RuntimeException("Invalid type detected.");
                };
            }
        }
        return false;
    }
}
