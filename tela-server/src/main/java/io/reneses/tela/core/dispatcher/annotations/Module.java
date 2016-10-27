package io.reneses.tela.core.dispatcher.annotations;

import java.lang.annotation.*;

/**
 * Module annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Module {

    /**
     * @return Module name
     */
    String value();

}
