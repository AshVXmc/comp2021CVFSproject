import core.controller.CVFSController;
import core.model.CVFS;
import core.view.CVFSView;

public class Main {
    public static void main(String[] args) {
        CVFS cvfsModel = new CVFS();
        CVFSView cvfsView = new CVFSView();
        CVFSController cvfsController = new CVFSController(cvfsModel, cvfsView);
        cvfsView.initialize();
        while (true) cvfsController.startCLITerminal();
    }
}