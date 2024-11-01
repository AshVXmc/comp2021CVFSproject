package core.controller;
import core.model.CVFS;
import core.view.CVFSView;

public class CVFSController {
    private final CVFS cvfsModel;
    private final CVFSView cvfsView;

    public CVFSController(CVFS cvfsModel, CVFSView cvfsView) {
        this.cvfsModel = cvfsModel;
        this.cvfsView = cvfsView;
    }
}
