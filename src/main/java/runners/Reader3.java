package runners;

import core.Relay;
import utils.Utils;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Reader3 {
    public static void main(String[] args) throws IOException {
        // initialize
        Relay relay = new Relay();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Cleaning up...");
            relay.close();
        }, "Shutdown-thread"));


        FileChannel f = FileChannel.open(Paths.get("file.txt"), StandardOpenOption.READ);

        // writer1
        Thread.currentThread().setName("Reader 3");
        Relay.RelayReader reader = relay.new RelayReader(() -> {
            String data = Utils.getData(f);
            Utils.say("I've read the \'" + data + "\' from file.");
            /*try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }, Integer.parseInt(args[0]));
        reader.read();
    }
}
