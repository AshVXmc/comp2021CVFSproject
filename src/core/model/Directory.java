package core.model;

import java.util.HashMap;

public class Directory {
    String name;
    private HashMap<String, Document> contents = new HashMap<>();
    private Directory parentDir;
    public Directory(String name, Directory parentDir){

    }
}
