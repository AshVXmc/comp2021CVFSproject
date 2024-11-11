package core.model;

public class CVFS {
    private Disk disk;
    private Directory dir;

    public void createNewDisk(int diskSize) {
        disk = new Disk(diskSize);


    }

    public Directory getDir() {
        return dir;
    }

    public void setDir(Directory dir) {
        this.dir = dir;
    }


}
