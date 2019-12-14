package runners;

import core.SharedInt;

public class SharedDataDemo {
    public static void main(String[] args) {
        SharedInt shared = new SharedInt("numW");
        shared.set(-2277);

        Integer data = shared.get();
        System.out.println(data);
    }
}
