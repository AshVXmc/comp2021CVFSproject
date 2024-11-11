package core.model;

public class CVFS {
    private Disk disk;
    private Directory dir;

    public void createNewDisk(int diskSize) {
        disk = new Disk(diskSize);
        dir = disk;

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
    public Object[] parsePath(String path) {
        // since the revised project.pdf said that we dont use : to separate file names in a path anymore
        String[] paths = path.split(":");

        Directory cur = getDir();
        for (int i = 0; i < paths.length - 1; i++) {
            String s = paths[i];
            if (s.equals("$")) continue;
            cur = (Directory) cur.getCatalog().get(s);
            if (cur == null) throw new IllegalArgumentException("Invalid Path, please use >/<dir>/.../<file> format.");
        }
        Object[] result = new Object[2];
        result[0] = cur;
        result[1] = paths[paths.length - 1];
        return result;
    }

}
