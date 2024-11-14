package core.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Disk extends Directory{
    private final int maxSize;

    public Disk(int maxSize) {
        super("Disk", null);
        this.maxSize = maxSize;
        setSize(0);
    }

    public int getMaxSize() {
        return maxSize;
    }

    public static Disk loadDisk(String filePath) throws IOException, ClassNotFoundException {

        FileInputStream fileIn = new FileInputStream(filePath);

        ObjectInputStream in = new ObjectInputStream(fileIn);

        Disk disk = (Disk) in.readObject();

        in.close();

        fileIn.close();

        return disk;

    }

    @Override
    public StringBuilder getPath() {
        StringBuilder builtString = new StringBuilder();
        builtString.append("Disk");
        return builtString;
    }

    @Override
    public String toString() {
        return String.format(getName(), getSize(), getMaxSize());
    }
}
