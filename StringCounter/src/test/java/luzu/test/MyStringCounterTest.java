package luzu.test;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class MyStringCounterTest {

    @Test
    public void shouldCountWords() {
        Map<String,Integer> result = generateTestCases().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, s -> {
                    MyStringCounter stringCounter = new MyStringCounterBuilder().setText(s.getKey())
                            .createMyStringCounter();
                    return stringCounter.count();
                }));

        assertThat(result).isEqualTo(generateTestCases());
    }

    @Test
    public void shouldGroupWordsAndCount() {
        MyStringCounter stringCounter = new MyStringCounterBuilder().setText("TEST TEST ALA").createMyStringCounter();
        Map<String,Long> result = stringCounter.groupAndCount();
        assertThat(result).contains(entry("TEST",2L),entry("ALA",1L));
    }

    @Test
    public void shouldReturnEmptyMapWhenNull() {
        MyStringCounter stringCounter = new MyStringCounterBuilder().setText(null).createMyStringCounter();
        Map<String,Long> result = stringCounter.groupAndCount();
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnEmptyMapWhenEmptyString() {
        MyStringCounter stringCounter = new MyStringCounterBuilder().setText("").createMyStringCounter();
        Map<String,Long> result = stringCounter.groupAndCount();
        assertThat(result).isEmpty();
    }

    private Map<String,Integer> generateTestCases() {
        Map<String, Integer> testCase = new HashMap<>();
        testCase.put("TEST TEST",2);
        testCase.put("TEST",1);
        testCase.put("TEST TEST ALA",3);
        testCase.put("",0);
        testCase.put(null,0);
        return testCase;
    }

}
