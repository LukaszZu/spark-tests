package luzu.test;

public class MyStringCounterBuilder {
    private String text;

    public MyStringCounterBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public MyStringCounter createMyStringCounter() {
        return new MyStringCounter(text);
    }
}