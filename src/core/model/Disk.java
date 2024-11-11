package core.model;

public class Disk extends Directory{
    private final int maxSize;

    public Disk(int maxSize) {
        super("Disk", null);
        this.maxSize = maxSize;

    }


}
