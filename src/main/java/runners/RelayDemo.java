package runners;

import core.Relay;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RelayDemo {
    private static final int READERS = 10;
    private static final int WRITERS = 5;
    private static final int MAX_NUM = 50;
    public static void main(String[] args) {
        Relay relay = new Relay();
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
            w.write();


        // readers


    }
}
