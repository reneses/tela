package io.reneses.tela.core.dispatcher.annotations;

import java.lang.annotation.*;

/**
 * Schedulable annotation
 * Indicates that an action can be scheduled
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Schedulable {

    /**
     * @return Minimum delay assignable to an action (seconds)
     */
    int minimumDelay() default 60;

}
