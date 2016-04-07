package luzu.test;

import com.google.common.base.Function;
import com.google.common.collect.*;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by zulk on 04.02.16.
 */
public class MultipleCounters {
    static class Data {
        String name;
        Integer val1,val2;
        Integer i1;

        public Data(String name, Integer val1, Integer val2, Integer i1) {
            this.name = name;
            this.val1 = val1;
            this.val2 = val2;
            this.i1 = i1;
        }
    }

    public static void main(String[] args) {
//        List<Data> date = new ArrayList<>();
//
//        date.add(new Data("ala",20,10,1));
//        date.add(new Data("ala",20,10,1));
//        date.add(new Data("ala",20,10,1));
//        date.add(new Data("ala",20,10,1));
//        date.add(new Data("ala2",20,10,1));
//
//
//
//        date.stream().flatMap(d -> mapData(d));

        parallelStream();
    }

    private static Stream<String[]> mapData(Data d) {
        IntSummaryStatistics intSummaryStatistics = new IntSummaryStatistics();
        String[] strings = {d.name, "field1",String.valueOf(d.val1)};
        return null;
    }


    static class Pair {
        String a;
        String b;

        public Pair(String a, String b) {
            this.a = a;
            this.b = b;
        }

        public String getA() {
            return a;
        }

        public String getB() {
            return b;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "a='" + a + '\'' +
                    ", b='" + b + '\'' +
                    '}';
        }
    }

    public static void parallelStream() {
        ImmutableListMultimap<String, String> multimap = ImmutableListMultimap.of("A", "A", "B", "B", "C", "C", "D", "D", "A", "X");

        ImmutableList<Pair> list = ImmutableList.of(new Pair("A", "B"),
                                                    new Pair("A", "C"),
                                                    new Pair("A", "C"),
                                                    new Pair("A", "C"),
                                                    new Pair("A", "C"),
                                                    new Pair("A", "C"),
                                                    new Pair("C", "C"),
                                                    new Pair("C", "C"),
                                                    new Pair("C", "C"),
                                                    new Pair("C", "C"),
                                                    new Pair("B", "C"));


        ImmutableListMultimap<String, Pair> index = Multimaps.index(list, Pair::getA);

        System.out.println(index);

//        multimap.parallelStream().forEach(e -> System.out.println("dd"+e.getKey()+e.getValue()+Thread.currentThread().getName()));


//        multimap.

           index.keySet().parallelStream()
                .flatMap(k -> index.get(k).stream())
                   .map(p -> p  .getA()+" => "+p.getB()+"/"+Thread.currentThread().getName())
                .collect(Collectors.toList())
                    .stream().forEach(System.out::println);

    }
}
