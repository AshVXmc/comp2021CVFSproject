package core.model;

import java.io.Serializable;
import java.util.HashMap;

public class Directory extends DataUnit {

    private HashMap<String, DataUnit> contents = new HashMap<>();
    private Directory parentDir;
    public Directory(String name, Directory parentDir){
        super(name);
        setParentDir(parentDir);
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
        Directory dir = new Directory(name, this);
        updateSizeBy(dir.getSize());
        contents.put(name, dir);
        System.out.println("New directory named " + name + " created.");
        return dir;
    }

    public void updateSizeBy(int offset) {
        if (getParentDir() != null) {
            getParentDir().updateSizeBy(offset);
            setSize(getSize() + offset);
        }
    }
    public StringBuilder getPath() {
        StringBuilder builtString = getParentDir().getPath();
        builtString.append('/').append(getName());
        return builtString;
    }


}
