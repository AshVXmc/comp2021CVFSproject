package core.model;

import java.io.Serializable;

public class DataUnit implements Serializable {
    private String name;
    private int size;

    public DataUnit(String name) {
        this.name = name;
    }
    public static boolean isValidName(String name) {
        return name.matches("^[a-zA-Z0-9]{1,10}$");
    }

    public static boolean isValidNameSize(String name) {
        return name.length() <= 10 && !name.isEmpty();
    }
    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
