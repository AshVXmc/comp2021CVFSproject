package core.controller;
import core.model.CVFS;
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
        CommandsList cmdType = CommandParser.getCommandType(cmd);

        while (cmdType == CommandsList.illegal){

            cmd = getScannerNextLine();
            cmdType = CommandParser.getCommandType(cmd);
        }
        CommandParser.parseCommand(cmd, cmdType);
    }
}
