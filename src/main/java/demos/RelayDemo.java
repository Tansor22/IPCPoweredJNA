package demos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RelayDemo {
    private static final int READERS = 10;
    private static final int WRITERS = 5;
    private static final int MAX_NUM = 50;
    public static void main(String[] args) throws IOException {
        String dataId = "sharedNumW";
        String tempDir = System.getProperty("java.io.tmpdir");
        System.out.println("Tmp dir : " + tempDir);
        String absolutePathToTmpFile = tempDir + dataId + ".tmp";
        Path path = Paths.get(absolutePathToTmpFile);
        File f = path.toFile();
        boolean wasCreated = f.createNewFile();
        if (wasCreated) {
            System.out.println(absolutePathToTmpFile + " was created!");
        } else {
            System.out.println(absolutePathToTmpFile + " wasn't created...");
            if (f.exists()) {
                System.out.println(absolutePathToTmpFile + " already exists!");
            }
        }
        //f.deleteOnExit();


        /// buffers
        //FileChannel.


       /* Relay relay = new Relay();
        List<Integer> list = new ArrayList<>();
        IntStream
                .generate((() -> Utils.getRandomInt(MAX_NUM)))
                .limit(100).forEach(list::add);

        List<Relay.RelayWriter> writers = IntStream
                .range(0, WRITERS)
                .mapToObj(i -> relay.new RelayWriter(() -> {
                            int valueToAdd = Utils.getRandomInt(MAX_NUM);
                            list.add(valueToAdd);
                            Utils.say("I've added the " + valueToAdd + " value.");
                        }))
                .collect(Collectors.toList());
        for(Relay.RelayWriter w : writers)
            w.write();*/


        // readers


    }
}
