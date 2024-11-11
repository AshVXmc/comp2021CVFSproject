package core.model;

import java.io.Serializable;
import java.util.HashMap;

public class Directory implements Serializable {
    private String name;
    private HashMap<String, Document> contents = new HashMap<>();
    private Directory parentDir;
    public Directory(String name, Directory parentDir){
        this.name = name;
        this.parentDir = parentDir;
    }

    public Directory getParentDir() {
        return parentDir;
    }

    public void setParentDir(Directory newparentDir) {
        parentDir = newparentDir;
    }
    public static boolean isValidName(String name) {
        return name.matches("^[a-zA-Z0-9]{1,10}$");
    }

    public static boolean isValidNameSize(String name) {
        return name.length() <= 10 && !name.isEmpty();
    }


}
