package runners;

import core.Relay;
import utils.Utils;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static utils.Utils.getRandomInt;

public class Writer3 {
    public static void main(String[] args) throws IOException {
        // initialize
        Relay relay = new Relay();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Cleaning up...");
            relay.close();
        }, "Shutdown-thread"));


        FileChannel f = FileChannel.open(Paths.get("file.txt"), StandardOpenOption.WRITE);

        // writer1
        Thread.currentThread().setName("Writer 3");
        Relay.RelayWriter writer = relay.new RelayWriter(() -> {
            try {
                int position = getRandomInt((int)f.size());
                String data = Utils.setData(f, position);
                Utils.say("I've written the \'" + data + "\' to file.");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 10);
        writer.write();
    }
    }
