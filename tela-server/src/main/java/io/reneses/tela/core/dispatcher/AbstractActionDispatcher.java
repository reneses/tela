package io.reneses.tela.core.dispatcher;

import java.util.*;

/**
 * Dispatcher for the actions
 */
abstract class AbstractActionDispatcher implements ActionDispatcher {

    Collection<Class<?>> actionClasses;
    Collection<Package> actionPackages;

    /**
     * Create an action dispatcher, scanning classes within the package where it is created
     *
     * @param actionClasses  Classes to be scanned
     * @param actionPackages Packages to be scanned
     */
    AbstractActionDispatcher(Collection<Class<?>> actionClasses, Collection<Package> actionPackages) {
        this.actionClasses = actionClasses;
        this.actionPackages = actionPackages;
    }

}
