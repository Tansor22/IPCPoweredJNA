package runners;

public class Utils {
    public static int getRandomInt(int a, int b) {
        return a + (int) (Math.random() * (b - a));
    }

    public static int getRandomInt(int b) {
        return getRandomInt(0, b);
    }
    public static void say(String message) {
        System.out.println(Thread.currentThread().getName() + " says: " + message);
    }
}
