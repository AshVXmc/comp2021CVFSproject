package core.model;

public class BinaryCriterion extends SimpleCriterion{
    private SimpleCriterion criterion1, criterion2;
    private String logicOp;

    public SimpleCriterion getCriterion1() {
        return criterion1;
    }
    public SimpleCriterion getCriterion2() {
        return criterion2;
    }
    public String getLogicOp() {
        return logicOp;
    }
    BinaryCriterion (String criName, SimpleCriterion criterion1, String logicOp, SimpleCriterion criterion2) {
        super(criName);
        this.criterion1 = criterion1;
        this.criterion2 = criterion2;
        this.logicOp = logicOp;
    }

    public static boolean isValidOp(String op) {
        return op.matches("^&&|\\|\\|$");
    }
}
