package core.model;

import java.io.Serializable;

public class Disk extends Directory implements Serializable {
    private final int maxSize;

    public Disk(int maxSize) {
        super("Disk", null);
        this.maxSize = maxSize;
        setSize(0);
    }

    public int getMaxSize() {
        return maxSize;
    }


    @Override
    public StringBuilder getPath() {
        StringBuilder builtString = new StringBuilder();
        builtString.append("Disk");
        return builtString;
    }

    @Override
    public String toString() {
        return String.format(getName(), getSize(), getMaxSize());
    }
}
