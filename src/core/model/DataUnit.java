package core.model;

import java.io.Serializable;

public abstract class DataUnit implements Serializable {
    private String name;

    private int size;

    public DataUnit(String name) {
        this.name = name;
    }
    public static boolean isValidName(String name) {
        return name.matches("^[a-zA-Z0-9]{1,10}$");
    }

    public String getName() {
        return name;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public abstract DocumentType getType();

    public abstract DataUnit getParentDir();
    public String toString() {
        return String.format(getName(), getSize());
    }

    public abstract void setParentDir(DataUnit newParentDir);

    protected void setName(String newName) {
        name = newName;
    }
}
