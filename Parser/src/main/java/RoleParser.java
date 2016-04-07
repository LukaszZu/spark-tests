import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.*;
import javafx.util.Pair;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by zulk on 07.01.16.
 */
class RoleParser {
    private String[] roles;

    private HashMultimap<String,String> map = HashMultimap.create();

    public RoleParser(String[] roles) {
        this.roles = roles;
    }


    public Map<String,String> parse() {

        Pattern pattern = Pattern.compile("/");

        return Arrays.stream(roles)
                .map(r -> Splitter.on("/").trimResults().omitEmptyStrings().splitToList(r))
                .flatMap(this::parseMap)
                .collect(Collectors.groupingBy(Pair::getKey,Collectors.mapping(Pair::getValue,Collectors.joining())));
    }


    private Pair<String,String> assignValues(PeekingIterator<String> iterator) {
        String key = "",value = "";
            key   = iterator.next();

        if (iterator.hasNext()) {
            value = iterator.peek();
        };

        return new Pair<>(key,value);
    }

    private Stream<Pair<String,String>> parseMap(List<String> roles) {

        List<Pair<String,String>> pairs = new ArrayList<>();
        PeekingIterator<String>   stringPeekingIterator = Iterators.peekingIterator(roles.iterator());

        while(stringPeekingIterator.hasNext()) {
            pairs.add(assignValues(stringPeekingIterator));
        }
        return pairs.stream();
    }

}
