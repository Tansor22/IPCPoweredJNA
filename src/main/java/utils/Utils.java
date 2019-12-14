package utils;

import com.sun.org.apache.bcel.internal.generic.LOR;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Utils {
    private final static String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
            "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis " +
            "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure " +
            "dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur " +
            "sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    public static int getRandomInt(int a, int b) {
        return a + (int) (Math.random() * (b - a));
    }

    public static int getRandomInt(int b) {
        return getRandomInt(0, b);
    }
    public static void say(String message) {
        System.out.println(Thread.currentThread().getName() + " says: " + message);
    }
    public static String getData(FileChannel f) {
        try {
            int begin = getRandomInt((int)f.size());
            int end = getRandomInt((int)f.size());
            int length = Math.abs(begin - end);
            ByteBuffer buffer = ByteBuffer.allocate(length);
            f.read(buffer, Math.min(begin, end));
            return new String(buffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String setData(FileChannel f, int position) {
        try {
            int begin = getRandomInt(LOREM_IPSUM.length());
            int end = getRandomInt(LOREM_IPSUM.length());
            String data = LOREM_IPSUM
                    .substring(Math.min(begin, end), Math.max(begin, end));
            ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());
            f.write(buffer, position);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
