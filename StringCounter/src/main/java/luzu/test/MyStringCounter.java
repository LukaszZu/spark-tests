package luzu.test;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * Created by zulk on 11.01.16.
 */
public class MyStringCounter  {
    private final Optional<StringTokenizer> text;

    public MyStringCounter(String text) {
        this.text = Optional.ofNullable(text).map(s -> new StringTokenizer(s," "));
    }

    public int count() {
        return text.map(StringTokenizer::countTokens).orElse(0);
    }

    public Map<String,Long> groupAndCount() {
        if(text.isPresent()) {
            return Collections.list(text.get()).stream()
                    .collect(Collectors.groupingBy(s -> (String) s, Collectors.counting()));
        } else {
            return Collections.emptyMap();
        }
   }
}
