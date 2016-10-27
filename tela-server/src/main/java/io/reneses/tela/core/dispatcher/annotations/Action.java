package io.reneses.tela.core.dispatcher.annotations;

import java.lang.annotation.*;

/**
 * Action annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Action {

    /**
     * @return Action name
     */
    String name() default "";

    /**
     * @return Action description, which will be used to generate the help
     */
    String description() default "";


    /**
     * @return List of names of the parameters
     */
    String[] parameters();

}
