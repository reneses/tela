package io.reneses.tela.tutorial.actions;

import io.reneses.tela.core.dispatcher.annotations.Action;
import io.reneses.tela.core.dispatcher.annotations.Module;
import io.reneses.tela.core.dispatcher.annotations.Schedulable;

import java.util.Date;

/**
 * Simple actions from the tutorial
 */
@Module("tutorial")
public class TutorialActions {

    @Action(description = "Simple hello", parameters = {})
    public String hello() {
        return "Hello World!";
    }

    @Action(description = "Hello with name", parameters = {"name"})
    public String hello(String name) {
        return "Hello " + name + "!";
    }

    @Action(name = "hello", description = "Hello with name and age", parameters = {"name", "age"})
    public String helloFriends(String name, int age) {
        return "Hello " + name + " of " + age + " years!";
    }

    @Action(name = "hello", description = "Hello to all your friends!", parameters = {"friend"})
    public String helloFriends(String[] name) {
        return "Hello " + String.join(", ", (CharSequence[]) name) + "!";
    }

    @Action(name = "hello-date", description = "Hello with date!", parameters = {})
    @Schedulable(minimumDelay = 1)
    public String helloDate() {
        return "Hello again! (At: " + new Date() + ")";
    }

}
