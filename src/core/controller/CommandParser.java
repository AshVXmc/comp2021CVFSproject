package core.controller;

import core.model.Directory;

import java.util.HashMap;

public class CommandParser {

    public static void parseCommand(String command, CommandsList commandType) {
        Directory dir;
        String name;
        String[] commandElements = command.split(" ");

        switch (commandType) {
            case newDir:
                if (commandElements.length != 2) throw new IllegalArgumentException("Incorrect number of paramaters (Expected 2). Command formula: [newDisk diskSize]");
                try {
                    Integer.parseInt(commandElements[1]);
                } catch (NumberFormatException e){
                    throw new NumberFormatException("Disk Size has to be a number.");
                }


                System.out.println("NEW DIRECTORY WOHOO");
                return;
            case newDoc:
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
