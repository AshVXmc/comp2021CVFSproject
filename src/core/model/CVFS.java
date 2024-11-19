package core.model;

import core.controller.CVFSController;
import core.controller.CommandsList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CVFS {
    private Disk disk;
    private Directory dir;
    private final Map<String, SimpleCriterion> criterionsList = new HashMap<>();
    public static boolean hasUnsavedChanges;

    public void setDisk(Disk disk) {
        this.disk = disk;
        dir = disk;
    }

    public void createNewDisk(String diskSizeStr) {
        int diskSize;
        try {
            diskSize = Integer.parseInt(diskSizeStr);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Disk Size has to be a number.");
        }
        
        if (diskSize <= 0)
            throw new IllegalArgumentException("Disk size must be greater than 0.");
        
        try {
            disk = new Disk(diskSize);
            dir = disk;
            System.out.println("New disk of size " + diskSize + " bytes successfully created.");
        } catch (Exception e) {
            System.err.println("Error creating new disk: " + e.getMessage());
        }
        hasUnsavedChanges = true;
    }

    public Directory getDir() {
        return dir;
    }

    public void setDir(Directory dir) {
        this.dir = dir;
    }
    public Object[] parsePath(String path) {
        // since the revised project.pdf said that we dont use : to separate file names in a path anymore
        String[] paths = path.split("/");

        Directory cur = getDir();
        for (int i = 0; i < paths.length - 1; i++) {
            String s = paths[i];
            if (s.equals(">")) continue;
            cur = (Directory) cur.getContents().get(s);
            if (cur == null) throw new IllegalArgumentException("Invalid Path, please use >/<dir>/.../<file> format.");
        }
        Object[] result = new Object[2];
        result[0] = cur;
        result[1] = paths[paths.length - 1];
        return result;
    }

    public void changeDirectory(String newDirName) {
        Object[] resourceList = parsePath(newDirName);
        Directory directory = (Directory) resourceList[0];
        String name = (String) resourceList[1];
        if (newDirName.equals("..")) {
            if (dir.getParentDir() == null)
                throw new IllegalArgumentException("Current directory is the root directory.");
            setDir(dir.getParentDir());
            System.out.println("Changed current working directory to '" + getDir() + "'");
            return;
        }
        DataUnit newDir = directory.getContents().get(name);
        if (newDir == null) throw new IllegalArgumentException("No directory named '" + newDirName + "' found.");
        if (!(newDir instanceof Directory))  throw new IllegalArgumentException("Not a directory.");
        setDir((Directory) newDir);
        System.out.println("Changed current working directory to '" + newDirName + "'");
    }

    public void createNewSimpleCriterion(String criName, String attrName, String op, String val) {
        if (criterionsList.containsKey(criName))
            throw new IllegalArgumentException("Simple criterion named '" + criName + "' already exists.");
        criterionsList.put(criName, new SimpleCriterion(criName, attrName, op, val));
        System.out.println("New simple criterion named '" + criName  + "' successfully created." +
                "\nAttribute: " + attrName +
                "\nOperator: " + op +
                "\nValue: " + val
        );
    }

    public void createNewIsDocumentCriterion(){
        criterionsList.put("isDocument", new SimpleCriterion());
        System.out.println("New simple criterion named 'isDocument' successfully created.\n" +
                "Evaluates to 'true' if is an instance of Document."
        );
    }

    public void createNewNegation(String criName1, String criName2) {
        if (criterionsList.containsKey(criName1))
            throw new IllegalArgumentException("Criterion named '" + criName1 + "' already exists.");
        if (!criterionsList.containsKey(criName2))
            throw new IllegalArgumentException("Criterion to be negated, named '" + criName2 + "', doesn't exist.");
        criterionsList.put(criName1, criterionsList.get(criName2).getNegativeCriterion(criName1));
        System.out.println("New Negation named '" + criName1 + "' successfully created." +
                "\nCriterion " + criName2 +" will be referenced by " + criName1);
    }

    public void createNewBinaryCriterion(String criName1, String criName3, String logicOp, String criName4) {
        if (criterionsList.containsKey(criName1))
            throw new IllegalArgumentException("Criterion named '" + criName1 + "' already exists.");
        if (!criterionsList.containsKey(criName3) || criName3 == null)
            throw new IllegalArgumentException("Criterion named '" + criName3 + "' is not found.");
        if (!criterionsList.containsKey(criName4) || criName4 == null)
            throw new IllegalArgumentException("Criterion named '" + criName4 + "' is not found.");
        criterionsList.put(criName1, new BinaryCriterion(criName1, criterionsList.get(criName3), logicOp, criterionsList.get(criName4)));
        System.out.println("New Binary criterion named '" + criName1 + "' successfully created." +
                "\nFirst criterion: " + criName3 +
                "\nOperator: " + logicOp +
                "\nSecond criterion: " + criName4
        );
    }

    public void printAllCriteria() {
        if (criterionsList.values().isEmpty()) {
            System.out.println("No criteria has been created. ");
            return;
        }
        for (SimpleCriterion c : criterionsList.values()) {
            if (c instanceof BinaryCriterion) {
                System.out.println("-> " + c.getCriName() + ": " + ((BinaryCriterion) c).getCriterion1().getCriName() + " " + ((BinaryCriterion) c).getLogicOp() + " " + ((BinaryCriterion) c).getCriterion2().getCriName());
            }
            else {
                if (Objects.equals(c.getCriName(), "isDocument")) {
                    System.out.println("-> " + c.getCriName());
                }
                else {
                    System.out.println("-> " + c.getCriName() + ": " + c.getAttrName() + " " + c.getOp() + " " + c.getVal());
                }
            }
        }
    }
    public void search(String criName) {
        if (!criterionsList.containsKey(criName))
            throw new IllegalArgumentException("Criterion named '" + criName + "' doesn't exist.");
        System.out.println("Search results for criterion '" + criName + "':");
        
        int totalFiles = 0;
        int totalSize = 0;

        for (DataUnit d : dir.getContents().values()) {
            if (criterionsList.get(criName).evaluate(d)) {
                System.out.println("-> " + d.getName());
                totalFiles++;
                totalSize += d.getSize();
            }
        }
        System.out.println("Total files: " + totalFiles + ", Total size: " + totalSize + " bytes.");
    }

    private final ArrayList<DataUnit> reachedFiles = new ArrayList<>();


    public void rSearch(String criName) {
        if (!criterionsList.containsKey(criName))
            throw new IllegalArgumentException("Criterion named '" + criName + "' doesn't exist.");
        System.out.println("Recursive search results for criterion '" + criName + "':");
        recursiveSearch(getDir(), criName);
    }

    private void recursiveSearch(Directory directory, String criName) {

        for (DataUnit d : directory.getContents().values()) {
            if (reachedFiles.contains(d)) {
                int totalFiles = reachedFiles.size();
                int totalSize = 0;
                for (DataUnit dataUnit : reachedFiles) {
                    totalSize += dataUnit.getSize();
                }
                reachedFiles.clear();
                System.out.println("Total files: " + totalFiles + ", Total size: " + totalSize + " bytes.");
                return;
            }
            if (d instanceof Directory) {
                System.out.println("-> " + d.getName());
                reachedFiles.add(d);
                recursiveSearch((Directory) d, criName);
            } else {
                if (criterionsList.get(criName).evaluate(d)) {
                    System.out.println("-> " + d.getName());
                    reachedFiles.add(d);
                }
            }
        }

    }

    public void save(String filePath) {
        try {
            File file = new File(filePath);
            if (file.isDirectory()) {
                throw new IllegalArgumentException("The provided path is a directory. Please provide a file path.");
            }
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(disk);
            out.close();
            fileOut.close();
            System.out.println("Disk saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving disk: " + e.getMessage());
        }
    }

    public void load(String filePath) {
        try {
            File file = new File(filePath);
            if (file.isDirectory()) {
                throw new IllegalArgumentException("The provided path is a directory. Please provide a file path.");
            }
            disk = Disk.loadDisk(filePath);
            dir = disk;
            System.out.println("Disk loaded from " + filePath);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading disk: " + e.getMessage());
        }
    }
}
