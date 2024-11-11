package core.model;

import java.io.Serializable;
import java.util.HashMap;

public class Directory extends DataUnit {

    private HashMap<String, DataUnit> contents = new HashMap<>();
    private Directory parentDir;
    public Directory(String name, Directory parentDir){
        super(name);
        this.parentDir = parentDir;
    }

    public Directory getParentDir() {
        return parentDir;
    }

    public void setParentDir(DataUnit newParentDir) {
        parentDir = (Directory) newParentDir;
    }

    public HashMap<String, DataUnit> getCatalog() {
        return contents;
    }


    public Directory newDir(String name) {
        if (contents.get(name) != null) throw new IllegalArgumentException("A file of this name already exists.");
        Directory temp = new Directory(name, this);
        updateSizeBy(temp.getSize());
        contents.put(name, temp);
        return temp;
    }

    public void updateSizeBy(int offset) {
        getParentDir().updateSizeBy(offset);
        setSize(getSize() + offset);
    }


}
