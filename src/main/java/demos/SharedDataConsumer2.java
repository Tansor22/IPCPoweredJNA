package demos;

import core.SharedInt;

public class SharedDataConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        SharedInt shared = new SharedInt("shared");

        Integer v = shared.get();
        System.out.println("SharedDataConsumer2 have got value " + v);
        Thread.sleep(100);
        int value = 3;
        shared.set(value);
        System.out.println("SharedDataConsumer2 have set value to " + value);


        Integer data = shared.get();
        while (data != 30) {
            data = shared.get();
            Thread.sleep(200);
            System.out.println("SharedDataConsumer2 have got value " + data);
        }
        System.out.println("SharedDataConsumer2 have got value " + data);
        shared.close();
    }
}
