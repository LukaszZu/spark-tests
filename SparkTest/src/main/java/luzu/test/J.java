package luzu.test;

import java.io.Serializable;

/**
 * Created by zulk on 16.03.16.
 */
public    class J implements Serializable {
    String name1;
    String name2;
    int n1;

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public int getN1() {
        return n1;
    }

    public void setN1(int n1) {
        this.n1 = n1;
    }
}

