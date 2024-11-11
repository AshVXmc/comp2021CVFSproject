package core.controller;
import core.model.CVFS;
import core.model.DataUnit;
import core.model.Directory;
import core.model.DocumentType;
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
            case newDir:
                if (cvfs.getDir() == null)
                    throw new IllegalStateException("No disk detected. Please create a new disk.");
                if (commandElements.length != 2)
                    throw new IllegalArgumentException("Incorrect number of parameters. Command formula: [newDir dirName]");;
                resourceList = cvfs.parsePath(commandElements[1]);
                dir = (Directory) resourceList[0];
                name = (String) resourceList[1];
                if (!DataUnit.isValidName(name))
                    throw new IllegalArgumentException("Illegal directory name: " + name);

                dir.newDir(name);

                break;


            case newDoc:
                if (commandElements.length < 4) {
                    throw new IllegalArgumentException("Incorrect number of parameters. Command formula: [newDoc docName docType docContent]");
                }

                String docName = commandElements[1];
                String docType = commandElements[2];
                StringBuilder docContentBuilder = new StringBuilder();
                if (!DocumentType.isValidType(docType)) {
                    throw new IllegalArgumentException("Invalid document type. Allowed types are: txt, java, html, css.");
                }

                // Join the remaining elements as content
                for (int i = 3; i < commandElements.length; i++) {
                    docContentBuilder.append(commandElements[i]).append(" ");
                }
                String docContent = docContentBuilder.toString().trim();

                try {
                    System.out.println("Document '" + docName + "." + docType + "' created successfully.");
                    System.out.println("Document Content: " + docContent);
                } catch (Exception e) {
                    System.err.println("Error creating document: " + e.getMessage());
                }
                break;

            case newDisk:
                if (commandElements.length != 2) throw new IllegalArgumentException("Incorrect number of paramaters (Expected 2). Command formula: [newDisk diskSize]");
                try {
                    cvfs.createNewDisk(Integer.parseInt(commandElements[1]));
                } catch (NumberFormatException e){
                    throw new NumberFormatException("Disk Size has to be a number.");
                }

                break;
            case delete:
                break;
            case rename:
                break;
            case changeDir:
                break;
            case list:
                break;
            case rList:
                break;
            case newSimpleCri:
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
