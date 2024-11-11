package core.model;

public class CVFS {
    private Disk disk;
    private Directory dir;

    public void createNewDisk(int diskSize) {
        disk = new Disk(diskSize);
        dir = disk;
        System.out.println("\033[32mNew disk created, size: \033[33m" + diskSize + "\033[0m");
    }

    public void setDisk(Disk disk) {
        this.disk = disk;
        dir = disk;
    }

    public Directory getDir() {
        return dir;
    }

    public void setDir(Directory dir) {
        this.dir = dir;
    }


}
