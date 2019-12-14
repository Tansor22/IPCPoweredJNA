package runners;

import java.io.File;
import java.nio.file.Paths;

public class Cleaner {
    public static void main(String[] args) {
        String[] toClean = {"numR", "numW", "waitR", "waitW"};
        for (String fileName : toClean) {
            String absolutePathToTmpFile = System.getProperty("java.io.tmpdir") + fileName + ".tmp";
            File file = Paths.get(absolutePathToTmpFile).toFile();
            if (file.exists()) file.delete();
        }
    }
}
