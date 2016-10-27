package io.reneses.tela;

import io.reneses.tela.core.dispatcher.annotations.Action;
import io.reneses.tela.core.dispatcher.annotations.Module;
import io.reneses.tela.core.dispatcher.annotations.Schedulable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Module("test")
public class TestActions {

    static AtomicInteger counter = new AtomicInteger(0);

    public static void resetCount() {
        counter.set(0);
    }

    public static int getCount() {
        return counter.get();
    }

    @Action(parameters = {})
    @Schedulable(minimumDelay = 1)
    public int count() {
        return counter.incrementAndGet();
    }

    @Action(parameters = {})
    @Schedulable(minimumDelay = 60)
    public int slowCount() {
        return counter.incrementAndGet();
    }

    @Action(parameters = {})
    @Schedulable(minimumDelay = 1)
    public String hello() {
        return "world";
    }

    @Action(parameters = {})
    public String bye() {
        return "bye";
    }

    @Action(parameters = { "input" })
    public String trim(String input) {
        return input.trim();
    }

    @Action(parameters = { "n" })
    @Schedulable(minimumDelay = 10)
    public int negate(int n) {
        return -n;
    }

    @Action(parameters = { "word" })
    public String implode(String[] words) {
        return Stream.of(words).reduce("", (out, w) -> out+=w);
    }

    @Action(parameters = { "n1", "n2" })
    public int sum(int n1, int n2) {
        return n1 + n2;
    }

    @Action(parameters = { "n1", "n2", "n3" })
    public int sum(int n1, int n2, int n3) {
        return n1 + n2 + n3;
    }

    @Action(name="only-positive", parameters = { "n" })
    public int onlyPositive(int n) {
        if (n < 0)
            throw new IllegalArgumentException("Only positive");
        return n;
    }

}