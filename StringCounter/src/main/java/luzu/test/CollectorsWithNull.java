package luzu.test;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * Created by zulk on 20.01.16.
 */
public class CollectorsWithNull {
    public static void main(String[] args) {

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("AAA");
        arrayList.add("AAB");
        arrayList.add("AAC");
        Map<String, String> collect =
                arrayList.stream()
                .map(CollectorsWithNull::process)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(
                        toMap(a -> a , a -> a));
        System.out.println(collect);
    }

    private static String process2(String s) {
        if (s.equals("AAB")) {
            return null;
        }
        return s;
    }


    private static Optional<String> process(String s) {

        Optional<String> os = Optional.empty();
        if (s.equals("AAB")) try {
            os = Optional.of(throwException());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());;
        }
        else {
            os = Optional.of(s);
        }
        return os;
    }

    private static String throwException() {
        throw new RuntimeException("Aaa");
    }
}
