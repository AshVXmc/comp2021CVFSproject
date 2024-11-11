package core.controller;

import core.model.*;

public class CommandTypeGetter {

    public static void parseCommand(String command, CommandsList commandType) {
        Directory dir;
        String name;
        String[] commandElements = command.split(" ");
        switch (commandType) {
            case newDir:
                System.out.println("NEW DIRECTORY WOHOO");
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

    public static CommandsList getCommandType(String command){
        String[] commandElements = command.split(" ");
        switch (commandElements[0]) {
            case "newDir":
                return CommandsList.newDir;

            case "newDoc":
                return CommandsList.newDoc;

            case "newDisk":
                return CommandsList.newDisk;

            case "delete":
                return CommandsList.delete;

            case "rename":
                return CommandsList.rename;

            case "changeDir":
                return CommandsList.changeDir;

            case "list":
                return CommandsList.list;

            case "rList":
                return CommandsList.rList;

            case "newSimpleCri":
                return CommandsList.newSimpleCri;

            case "isDocument":
                return CommandsList.isDocument;
            case "newNegation":
                return CommandsList.newNegation;

            case "newBinaryCri":
                return CommandsList.newBinaryCri;

            case "printAllCriteria":
                return CommandsList.printAllCriteria;

            case "search":
                return CommandsList.search;

            case "rSearch":
                return CommandsList.rsearch;

            case "save":
                return CommandsList.save;
            case "load":
                return CommandsList.load;

            case "quit":
                return CommandsList.quit;
        }
        if (command == "") {
            System.out.println("Command line is empty. Please enter a command.");
        }
        else {
            System.out.println("Invalid Command.");
        }
        return CommandsList.illegal;
    }
}
