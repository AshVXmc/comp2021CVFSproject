package core.controller;
import core.model.CVFS;
import core.model.Directory;
import core.model.DocumentType;
import core.view.CVFSView;

import java.util.Scanner;

public class CVFSController {
    private final CVFS cvfsModel;
    private final CVFSView cvfsView;
    private final Scanner scanner = new Scanner(System.in);

    private String getScannerNextLine() {
        return scanner.nextLine();
    }

    public CVFSController(CVFS cvfsModel, CVFSView cvfsView) {
        this.cvfsModel = cvfsModel;
        this.cvfsView = cvfsView;
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
        Directory dir;
        String name;
        String[] commandElements = command.split(" ");

        switch (commandType) {
            case newDir:
                return;
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
                return;
            case newDisk:
                if (commandElements.length != 2) throw new IllegalArgumentException("Incorrect number of paramaters (Expected 2). Command formula: [newDisk diskSize]");
                try {
                    Integer.parseInt(commandElements[1]);
                } catch (NumberFormatException e){
                    throw new NumberFormatException("Disk Size has to be a number.");
                }
                //cvfs.createNewDisk

                System.out.println("NEW DIRECTORY WOHOO");
                return;
            case delete:
                return;
            case rename:
                return;
            case changeDir:
                return;
            case list:
                return;
            case rList:
                return;
            case newSimpleCri:
                return;
            case isDocument:
                return;
            case newNegation:
                return;
            case newBinaryCri:
                return;
            case printAllCriteria:
                return;
            case search:
                return;
            case rsearch:
                return;
            case save:
                return;
            case load:
                return;
            case quit:
                System.exit(0);
        }
    }
}
