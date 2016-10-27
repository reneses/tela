package io.reneses.tela.core.dispatcher.scanner;

import io.reneses.tela.core.dispatcher.models.Module;

import java.util.Collection;
import java.util.Map;

/**
 * The function scanner is the component responsible of scanning the classes, extracting the actions, storing them,
 * and returning them by its name and parameters
 */
public abstract class AbstractActionScanner {

    protected Collection<Class<?>> classesToScan;
    protected Collection<Package> packagestoScan;

    /**
     * Instantiate the scanner with the classes and packages to scan
     *
     * @param classesToScan  Classes to scan
     * @param packagestoScan Packages to scan
     */
    AbstractActionScanner(Collection<Class<?>> classesToScan, Collection<Package> packagestoScan) {
        this.classesToScan = classesToScan;
        this.packagestoScan = packagestoScan;
    }

    /**
     * Scan the classes and packages
     *
     * @return The modules found within them
     */
    public abstract Map<String, Module> scan();

}
