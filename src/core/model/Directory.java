package core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Directory extends DataUnit {

    @Override
    public DocumentType getType() {
        return null;
    }
    
    private static Map<String, DataUnit> contents = new HashMap<>();
    private Directory parentDir;
    public Directory(String name, Directory parentDir){
        super(name);
        setParentDir(parentDir);
    }


    public int getSize(Directory currentDirectory) {
        int size = 0;
        for (DataUnit dataUnit : currentDirectory.getContents().values()) {
            if (dataUnit.getParentDir() == currentDirectory) {
                size += dataUnit.getSize();
//                System.out.println("doc counted: " + dataUnit.getName());
            }
        }
        return 40 + size;
    }

    public Directory getParentDir() {
        return parentDir;
    }

    public void setParentDir(DataUnit newParentDir) {
        parentDir = (Directory) newParentDir;
    }

    public Map<String, DataUnit> getContents() {
        return contents;
    }

    public void setContents(HashMap<String, DataUnit> newContents) {
        contents = newContents;
    }


    public Directory newDir(String name) {
        if (contents.get(name) != null) throw new IllegalArgumentException("A file of this name already exists.");
        Directory dir = new Directory(name, this);
        updateSize(dir.getSize());
        contents.put(name, dir);
        System.out.println("New directory named " + name + " created.");
        CVFS.hasUnsavedChanges = true;
        return dir;
    }

    public void updateSize(int offset) {
        if (getParentDir() != null) {
            getParentDir().updateSize(offset);
            setSize(getSize() + offset);
        }
    }
    public StringBuilder getPath() {
        StringBuilder builtString = getParentDir().getPath();
        builtString.append('/').append(getName());
        return builtString;
    }

    public void createNewDocument(String name, DocumentType type, String content) {
        if (contents.get(name) != null)
            throw new IllegalArgumentException("A file with an identical name already exists. Please enter a different name.");
        Document document = new Document(type, content, name, this);
        updateSize(document.getDocumentSize());
        contents.put(name, document);
        System.out.println("New document named '" + name + "." + type + "' created.");
        System.out.println("Document Content: " + content);
        CVFS.hasUnsavedChanges = true;
    }

    public void deleteDocument(String docName){
        if (contents.get(docName) == null)
            throw new IllegalArgumentException("Document named '" + docName + "' is not found in this directory.");
        updateSize(contents.get(docName).getSize() * -1);
        contents.remove(docName);
        System.out.println("Document named '" + docName + "' successfully deleted from current directory");
    }

    public void renameDocument (String oldName, String newName) {
        if (contents.get(oldName) == null)
            throw new IllegalArgumentException("Document named '" + oldName + "' is not found in this directory.");
        if (contents.get(newName) != null)
            throw new IllegalArgumentException("File with the same (new) name already exists in this directory");
        if (newName.equals(oldName)) {
            System.out.println("New document name cannot be the same as old document name.");
        }

        DataUnit renamedDocument = contents.get(oldName);
        renamedDocument.setName(newName);
        contents.remove(oldName);
        contents.put(newName, renamedDocument);
        System.out.println("Successfully renamed document.\n" + "Old name: " + oldName + "\nNew name: " + newName);
    }

    public void listAllFiles() {
        if (contents.isEmpty())
            System.out.println("Current directory is empty");
        for (DataUnit dataUnit : contents.values()) {
            if (dataUnit instanceof Directory)
                System.out.println("-> (Directory) Name: " + dataUnit + ", Size: " + getSize(this));
            if (dataUnit instanceof Document)
                System.out.println("-> (Document) Name: " + dataUnit + ", Type: " + dataUnit.getType() + ", Size: " + dataUnit.getSize());
        }
    }

    public void recursivelyListAllFiles() {
        if (contents.isEmpty())
            System.out.println("Current directory is empty.");
        recursivelyListAllFiles(this, 0);
    }
    private final ArrayList<DataUnit> reachedFiles = new ArrayList<DataUnit>();

    public void recursivelyListAllFiles(Directory currentDirectory, int fileLevel) {
        for (DataUnit dataUnit : currentDirectory.getContents().values()) {
            if (reachedFiles.contains(dataUnit)) {
                reachedFiles.clear();
                return;
            }
            for (int i = 0; i < fileLevel; i++)
                System.out.print("\t");
            if (dataUnit instanceof Document) {
                reachedFiles.add(dataUnit);
                System.out.println("-> (Document) Name: " + dataUnit.getName() + ", Type: " + dataUnit.getType() + ", Size: " + dataUnit.getSize());
            }
            if (dataUnit instanceof Directory) {
                reachedFiles.add(dataUnit);
                System.out.println("-> (Directory) Name: " + dataUnit.getName() + ", Size: " + getSize(currentDirectory));
                recursivelyListAllFiles((Directory) dataUnit, fileLevel + 1);
            }
        }
    }

}
