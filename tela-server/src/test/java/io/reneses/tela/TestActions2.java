package io.reneses.tela;

import io.reneses.tela.core.dispatcher.annotations.Action;
import io.reneses.tela.core.dispatcher.annotations.Module;
import io.reneses.tela.core.dispatcher.annotations.Schedulable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Module("test2")
public class TestActions2 {

    @Action(parameters = {})
    @Schedulable(minimumDelay = 1)
    public String hello() {
        return "world";
    }

}