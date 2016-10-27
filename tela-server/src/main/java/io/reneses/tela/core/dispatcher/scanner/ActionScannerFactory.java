package io.reneses.tela.core.dispatcher.scanner;

import java.util.Collection;

/**
 * ActionScannerFactory class.
 */
public class ActionScannerFactory {

    /**
     * Create an action scanner
     *
     * @return ActionDispatcher instance
     * @param classesToScan Classes to be scanned
     * @param packagestoScan Packages to be scanned
     */
    public static AbstractActionScanner create(Collection<Class<?>> classesToScan, Collection<Package> packagestoScan) {
        return new ActionScanner(classesToScan, packagestoScan);
    }

}
