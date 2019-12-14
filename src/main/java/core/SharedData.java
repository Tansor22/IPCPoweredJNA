package core;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public abstract class SharedData<T> {
    private final String dataId;
    protected FileChannel channel;
    protected final Data dataType;
    private File file;
    private static final String tempDir;

    static {
        tempDir = System.getProperty("java.io.tmpdir");
    }

    public SharedData(String dataId, Data dataType) {
        this.dataType = dataType;
        this.dataId = dataId;
        getOrCreate();
    }

    public abstract <T> T get();

    public abstract void set(T data);


    private void getOrCreate() {
        String absolutePathToTmpFile = tempDir + dataId + ".tmp";
        Path path = Paths.get(absolutePathToTmpFile);
        file = path.toFile();
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) throw new IOException("Не удалось создать файл!");
            }
            channel = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE);
            //f.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
