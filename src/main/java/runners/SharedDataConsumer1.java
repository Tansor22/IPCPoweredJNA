package runners;

import core.SharedInt;

public class SharedDataConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        SharedInt shared = new SharedInt("shared");
        Integer val = null;
        while (val == null) {
            val = shared.get();
            System.out.println("SharedDataConsumer1 have got value " + val);
            Thread.sleep(500);
        }
        Thread.sleep(500);
        int v = 30;
        shared.set(v);
        System.out.println("SharedDataConsumer1 have set value " + v);
        Thread.sleep(1000);
        Integer data = shared.get();
        System.out.println("SharedDataConsumer1 have got value " + data);
        shared.close();
    }
}
