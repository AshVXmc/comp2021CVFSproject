package core.model;

public class CVFS {
    private Disk disk;
    private Directory dir;

    public void createNewDisk(int diskSize) {
        disk = new Disk(diskSize);


    }
}
