package luzu.test;

import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.prefs.Preferences;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by zulk on 12.01.16.
 */
public class IntPartitioner {
    public static void main(String[] args) {

        ArrayList<Integer> mod2 = new ArrayList<>();
        ArrayList<Integer> mod3 = new ArrayList<>();
        ArrayList<Integer> noMod = new ArrayList<>();

        Predicate<Integer> pmod2 = i -> i % 2 == 0;
        Predicate<Integer> pmod3 = i -> i % 3 == 0;

        HashMap<Predicate, Collection> predicateConsumerHashMap = new HashMap<>();
        predicateConsumerHashMap.put(pmod2,mod2);
        predicateConsumerHashMap.put(pmod3,mod3);

//        Stream<Predicate<Integer>> conditions = Stream.of(pmod2, pmod3);i -> i % 2 == 0;

        for (int i=1;i<=10;i++) {
            final int finalI = i;
            Optional<Boolean> any = predicateConsumerHashMap.entrySet()
                    .stream()
                    .filter(s -> s.getKey().test(finalI))
                    .map(s -> s.getValue().add(finalI))
                    .findAny();


            if (!any.isPresent()) {
                noMod.add(finalI);
            }
        }


        System.out.println(mod2);
        System.out.println(mod3);
        System.out.println(noMod);
    }

}
