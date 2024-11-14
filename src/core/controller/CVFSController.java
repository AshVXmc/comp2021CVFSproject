package core.controller;
import core.model.*;
import core.view.CVFSView;

import java.util.Scanner;

public class CVFSController {
    private static CVFS cvfs = null;
    private static CVFSView cvfsView = null;
    private final Scanner scanner = new Scanner(System.in);


    private String getScannerNextLine() {
        return scanner.nextLine();
    }

    public CVFSController(CVFS cvfs, CVFSView cvfsView) {
        CVFSController.cvfs = cvfs;
        CVFSController.cvfsView = cvfsView;
    }

    public void startCLITerminal(){
        String cmd = getScannerNextLine();
        CommandsList cmdType = CommandTypeGetter.getCommandType(cmd);

        while (cmdType == CommandsList.illegal){

            cmd = getScannerNextLine();
            cmdType = CommandTypeGetter.getCommandType(cmd);
        }
        parseCommand(cmd, cmdType);
    }
    public static void parseCommand(String command, CommandsList commandType) {
        Object[] resourceList;
        String name;
        Directory dir;

        String[] commandElements = command.split(" ");

        switch (commandType) {
            case illegal:
                System.out.println("Command not found.");
                break;
            case newDir:
                if (cvfs.getDir() == null)
                    throw new IllegalStateException("No disk detected. Please create a new disk.");
                if (commandElements.length != 2)
                    throw new IllegalArgumentException("Incorrect number of parameters. Command formula: newDir [dirName]");
                resourceList = cvfs.parsePath(commandElements[1]);
                dir = (Directory) resourceList[0];
                name = (String) resourceList[1];
                if (!DataUnit.isValidName(name))
                    throw new IllegalArgumentException("Illegal directory name: " + name);
                dir.newDir(name);
                break;

            case newDoc:
                if (commandElements.length < 4)
                    throw new IllegalArgumentException("Incorrect number of parameters. Command formula: newDoc [docName] [docType] [docContent]");
                if (cvfs.getDir() == null)
                    throw new IllegalStateException("No disk detected. Please create a new disk.");

                String docName = commandElements[1];
                String docType = commandElements[2];
                StringBuilder docContentBuilder = new StringBuilder();
                if (!DocumentType.isValidType(docType))
                    throw new IllegalArgumentException("Invalid document type. Allowed types are: txt, java, html, css.");
                if (!DataUnit.isValidName(docName))
                    throw new IllegalArgumentException("Invalid document name " + docName + ". Only English letters and digits are allowed.");
                if (docName.length() > 10)
                    throw new IllegalArgumentException("Invalid document name length, must be 10 characters or less.");
                // Join the remaining elements as content
                for (int i = 3; i < commandElements.length; i++) {
                    docContentBuilder.append(commandElements[i]).append(" ");
                }
                String docContent = docContentBuilder.toString().trim();

                try {
                    resourceList = cvfs.parsePath(commandElements[1]);
                    dir = (Directory) resourceList[0];
                    name = (String) resourceList[1];
                    dir.createNewDocument(name, DocumentType.getDocumentType(docType), docContent);
                } catch (Exception e) {
                    System.err.println("Error creating document: " + e.getMessage());
                }
                break;
            case newDisk:
                if (commandElements.length != 2)
                    throw new IllegalArgumentException("Incorrect number of parameters (Expected 2). Command formula: newDisk [diskSize]");
                int diskSize;
                try {diskSize = Integer.parseInt(commandElements[1]);
                } catch (NumberFormatException e){
                    throw new NumberFormatException("Disk Size has to be a number.");
                }
                if (diskSize <= 0)
                    throw new IllegalArgumentException("Disk size must be greater than 0.");
                try {
                    cvfs.createNewDisk(diskSize);
                } catch (Exception e) {
                    System.err.println("Error creating new disk: " + e.getMessage());
                }

                break;
            case delete:
                if (commandElements.length != 2)
                    throw new IllegalArgumentException("Incorrect number of parameters (Expected 2). Command formula: delete [fileName]");
                if (cvfs.getDir() == null)
                    throw new IllegalStateException("No disk detected. Please create a new disk.");
                try {
                    resourceList = cvfs.parsePath(commandElements[1]);
                    dir = (Directory) resourceList[0];
                    name = (String) resourceList[1];
                    if (!DataUnit.isValidName(name))
                        throw new IllegalArgumentException("Invalid file name: " + name);
                    dir.deleteDocument(name);
                } catch (Exception e) {
                    System.err.println("Error deleting document: " + e.getMessage());
                }
                break;
            case rename:
                if (commandElements.length != 3)
                    throw new IllegalArgumentException("Incorrect number of parameters (Expected 3). Command formula: rename [oldFileName] [newFileName]");
                if (cvfs.getDir() == null)
                    throw new IllegalStateException("Please first create a disk.");
                String newName = commandElements[2];
                if (!DataUnit.isValidName(newName))
                    throw new IllegalArgumentException("New name is invalid: " + newName);
                try {
                    resourceList = cvfs.parsePath(commandElements[1]);
                    dir = (Directory) resourceList[0];
                    name = (String) resourceList[1];
                    if (!DataUnit.isValidName(name))
                        throw new IllegalArgumentException("Old name is invalid: " + name);
                    dir.renameDocument(name, newName);
                } catch (Exception e) {
                    System.err.println("Error renaming document: " + e.getMessage());
                }
                break;
            case changeDir:
                if (cvfs.getDir() == null)
                    throw new IllegalStateException("No disk detected. Please create a new disk.");
                if (commandElements.length != 2)
                    throw new IllegalArgumentException("Incorrect number of parameters (Expected 2). Command formula: changeDir [dirName]");
                String newDirName = commandElements[1];
                cvfs.changeDirectory(newDirName);
                break;
            // helper function, is not part of the requirement
            case getCurrentDir:
                if (cvfs.getDir() == null)
                    throw new IllegalStateException("No disk detected. Please create a new disk.");
                System.out.println("Current directory:" + cvfs.getDir().getName());
                break;
            /////
            case list:
                if (cvfs.getDir() == null)
                    throw new IllegalStateException("No disk detected. Please create a new disk.");
                if (commandElements.length != 1)
                    throw new IllegalArgumentException("Incorrect number of parameters (Expected 1). Command formula: list");
                cvfs.getDir().listAllFiles();
                break;
            case rList:
                if (cvfs.getDir() == null)
                    throw new IllegalStateException("No disk detected. Please create a new disk.");
                if (commandElements.length != 1)
                    throw new IllegalArgumentException("Incorrect number of parameters (Expected 1). Command formula: rList");
                cvfs.getDir().recursivelyListAllFIles();
                break;
            case newSimpleCri:
                if (commandElements.length != 5)
                    throw new IllegalArgumentException("Incorrect number of parameters (Expected 5). Command formula: newSimpleCri [criName] [attrName] [op] [val]");
                if (!SimpleCriterion.isValidCriterionName(commandElements[1]))
                    throw new IllegalArgumentException("Invalid Criterion name '" + commandElements[1] + "'");
                if (!SimpleCriterion.isValidCriterion(commandElements[1], commandElements[2], commandElements[3], commandElements[4]))
                    throw new IllegalArgumentException("Invalid Criterion argument " +
                            "'" + commandElements[2] + " " + commandElements[3] + " " + commandElements[4] + "'");

                cvfs.createNewCriterion(commandElements[1], commandElements[2], commandElements[3], commandElements[4]);

                break;
            case isDocument:
                break;
            case newNegation:
                break;
            case newBinaryCri:
                break;
            case printAllCriteria:
                break;
            case search:
                break;
            case rsearch:
                break;
            case save:
                break;
            case load:
                break;
            case quit:
                System.exit(0);
        }
    }
}
