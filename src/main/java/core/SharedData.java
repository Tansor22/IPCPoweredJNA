package core;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public abstract class SharedData<T> {
    private final String dataId;
    protected FileChannel channel;
    protected final Data dataType;
    private File file;
    private static final String TEMP_DIR;
    private static final Map<String,Integer> CONSUMERS;

    static {
        TEMP_DIR = System.getProperty("java.io.tmpdir");
        CONSUMERS = new HashMap<>();
    }

    public SharedData(String dataId, Data dataType) {
        this.dataType = dataType;
        CONSUMERS.merge(dataId,1, Integer::sum);
        this.dataId = dataId;
        getOrCreate();
    }

    public abstract <T> T get();

    public abstract void set(T data);


    private void getOrCreate() {
        String absolutePathToTmpFile = TEMP_DIR + dataId + ".tmp";
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
            Integer consumers = CONSUMERS.get(dataId);
            System.out.println("Consumer before dec " + consumers);
            if (file.exists() && consumers <= 1) {
                if (!file.delete()) throw new IOException("Не удалось удалить файл!");
            }
            CONSUMERS.replace(dataId, --consumers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }
}
