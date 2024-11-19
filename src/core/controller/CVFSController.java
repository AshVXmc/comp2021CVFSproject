package core.controller;
import core.model.*;
import java.util.Objects;
import java.util.Scanner;

public class CVFSController {
    private static CVFS cvfs = null;

    private final Scanner scanner = new Scanner(System.in);


    private String getScannerNextLine() {
        return scanner.nextLine();
    }

    public CVFSController(CVFS cvfs) {
        CVFSController.cvfs = cvfs;
    }

    public void startCLITerminal() {
        String cmd = getScannerNextLine();
        CommandsList cmdType = CommandTypeGetter.getCommandType(cmd);

        while (cmdType == CommandsList.illegal) {

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
                if (commandElements.length != 4)
                    throw new IllegalArgumentException("Incorrect number of parameters (Expected 3). Command formula: newDoc [docName] [docType] [docContent]");
                Document.newDoc(commandElements, cvfs);
                break;

            case newDisk:
                if (commandElements.length != 2)
                    throw new IllegalArgumentException("Incorrect number of parameters (Expected 2). Command formula: newDisk [diskSize]");
                cvfs.createNewDisk(commandElements[1]);
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

            case getCurrentDir:
                if (cvfs.getDir() == null)
                    throw new IllegalStateException("No disk detected. Please create a new disk.");
                System.out.println("Current directory:" + cvfs.getDir().getName());
                break;

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
                cvfs.getDir().recursivelyListAllFiles();
                break;

            case newSimpleCri:
                if (commandElements.length == 2 && Objects.equals(commandElements[1], "isDocument")) {
                    cvfs.createNewIsDocumentCriterion();
                    break;
                }
                if (commandElements.length != 5)
                    throw new IllegalArgumentException("Incorrect number of parameters (Expected 5). Command formula: newSimpleCri [criName] [attrName] [op] [val]");
                if (!SimpleCriterion.isValidCriterionName(commandElements[1]))
                    throw new IllegalArgumentException("Invalid Simple Criterion name: '" + commandElements[1] + "'");
                if (!SimpleCriterion.isValidCriterion(commandElements[1], commandElements[2], commandElements[3], commandElements[4]))
                    throw new IllegalArgumentException("Invalid Simple Criterion argument(s): " + "'" + commandElements[2] + " " + commandElements[3] + " " + commandElements[4] + "'");
                cvfs.createNewSimpleCriterion(commandElements[1], commandElements[2], commandElements[3], commandElements[4]);
                break;

            case newNegation:
                if (commandElements.length != 3)
                    throw new IllegalArgumentException("Incorrect number of parameters (Expected 3). Command formula: newNegation [criName1] [criName2]");
                if (!SimpleCriterion.isValidCriterionName(commandElements[1]))
                    throw new IllegalArgumentException("Invalid Simple Criterion name: '" + commandElements[1] + "'");
                if (!SimpleCriterion.isValidCriterionName(commandElements[2]))
                    throw new IllegalArgumentException("Invalid Simple Criterion name: '" + commandElements[2] + "'");
                cvfs.createNewNegation(commandElements[1], commandElements[2]);
                break;

            case newBinaryCri:
                if (commandElements.length != 5)
                    throw new IllegalArgumentException("Incorrect number of parameters (Expected 5). Command formula: newBinaryCri [criName1] [criName3] [logicOp] [criName4]");
                if (!SimpleCriterion.isValidCriterionName(commandElements[1]))
                    throw new IllegalArgumentException("Invalid Criterion name: '" + commandElements[1] + "'");
                if (!SimpleCriterion.isValidCriterionName(commandElements[2]))
                    throw new IllegalArgumentException("Invalid Criterion name: '" + commandElements[2] + "'");
                if (!BinaryCriterion.isValidOp(commandElements[3]))
                    throw new IllegalArgumentException("Invalid Binary Criterion operator: '" + commandElements[3] + "'");
                if (!SimpleCriterion.isValidCriterionName(commandElements[4]))
                    throw new IllegalArgumentException("Invalid Criterion name: '" + commandElements[4] + "'");
                cvfs.createNewBinaryCriterion(commandElements[1], commandElements[2], commandElements[3], commandElements[4]);
                break;

            case printAllCriteria:
                if (commandElements.length != 1)
                    throw new IllegalArgumentException("Incorrect number of parameters (Expected 1). Command formula: printAllCriteria");
                cvfs.printAllCriteria();
                break;

            case search:
                if (commandElements.length != 2)
                    throw new IllegalArgumentException("Incorrect number of parameters (Expected 2). Command formula: search [criName]");
                cvfs.search(commandElements[1]);
                break;

            case rSearch:
                if (commandElements.length != 2)
                    throw new IllegalArgumentException("Incorrect number of parameters (Expected 2). Command formula: rsearch [criName]");
                cvfs.rSearch(commandElements[1]);
                break;

            case save:
                if (cvfs.getDir() == null)
                    throw new IllegalStateException("No disk detected. Please create a new disk.");
                if (commandElements.length != 2)
                    throw new IllegalArgumentException("Incorrect number of parameters (Expected 2). Command formula: save [filePath]");
                cvfs.save(commandElements[1]);
                break;

            case load:
                if (commandElements.length != 2)
                    throw new IllegalArgumentException("Incorrect number of parameters (Expected 2). Command formula: load [filePath]");
                cvfs.load(commandElements[1]);
                break;

            case quit:
                if (CVFS.hasUnsavedChanges) {
                    System.out.println("You have unsaved changes. Would you like to save them before quitting? (yes/no)");
                    Scanner scanner = new Scanner(System.in);
                    String response = scanner.nextLine().trim().toLowerCase();

                    while (!response.equals("yes") && !response.equals("no")) {
                        System.out.println("Invalid input. Please type 'yes' or 'no'.");
                        response = scanner.nextLine().trim().toLowerCase();
                    }
                    if (response.equals("yes")) {
                        // Call save method
                        System.out.println("Please input a [filePath] to save.");
                        String filePath = scanner.nextLine().trim();
                        cvfs.save(filePath);
                    }
                }
                    System.out.println("Exiting CVFS...");
                    System.exit(0);

        }
    }
}
