package core.controller;

import core.model.*;

public class CommandTypeGetter {
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

            // helper function
            case "getCurrentDir":
                return CommandsList.getCurrentDir;
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
