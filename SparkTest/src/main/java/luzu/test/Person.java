package luzu.test;

import java.io.Serializable;

/**
 * Created by zulk on 21.03.16.
 */
public class Person implements Serializable {
    private String  name;
    private Integer n1;

    public Person() {
    }

    public Person(String name, Integer n1) {
        this.name = name;
        this.n1 = n1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getN1() {
        return n1;
    }

    public void setN1(Integer n1) {
        this.n1 = n1;
    }

}
