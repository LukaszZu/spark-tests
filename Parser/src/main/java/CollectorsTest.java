import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collector.*;


/**
 * Created by zulk on 14.01.16.
 */
public class CollectorsTest {
    public static void main(String[] args) {
        List<String[]> s = new ArrayList<>();

        s.add(new String[]{"AAA","BBBB"});
        s.add(new String[]{"AAA","BBB1"});
        s.add(new String[]{"AAA","BBB1"});
        s.add(new String[]{"AAA","BBB2"});
        s.add(new String[]{"AAA","BBB3"});
        s.add(new String[]{"AA1","BBB3"});
        s.add(new String[]{"AA2","BBB3"});
        s.add(new String[]{"AA2",""});
        s.add(new String[]{"AA2",null});

        Collector<String, StringJoiner, String> stringJoinerSupplier = getStringStringJoinerStringCollector(",","[","]");


//        StringJoiner joiner = new StringJoiner("1").

        Map<String, String> collect = s.stream().
                collect(Collectors.groupingBy(a -> a[0], Collectors.mapping(a -> a[1], stringJoinerSupplier)));
        System.out.println(collect);
    }

    private static Collector<String, StringJoiner, String> getStringStringJoinerStringCollector(String separator,String prefix,String suffix) {
        return Collector.of(() -> new StringJoiner(separator,prefix,suffix)
                    , (joiner, s1) -> {
                        if (s1 != null && ! s1.isEmpty())
                         joiner.add(s1);
                    }
                    , StringJoiner::merge
                    , StringJoiner::toString
            );
    }
}
