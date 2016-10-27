package io.reneses.tela.core.dispatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ActionDispatcherFactory class.
 */
public class ActionDispatcherFactory {

    private static List<Class<?>> classesToScan = new ArrayList<>();
    private static List<Package> packagesToScan = new ArrayList<>();

    //------------------------------ Scanning methods ------------------------------//

    /**
     * Add all the classes within the supplied packages to the classes that will be scanned
     *
     * @param packages Packages to scan
     */
    public static void addPackagesToScan(Package... packages) {
        Collections.addAll(packagesToScan, packages);
    }

    /**
     * Add all the classes within the supplied packages to the classes that will be scanned
     *
     * @param packages Packages to scan
     */
    public static void addPackagesToScan(Iterable<Package> packages) {
        for (Package packageToScan : packages)
            packagesToScan.add(packageToScan);
    }

    /**
     * Add all the classes to the ones that will be scanned
     *
     * @param classes Classes to scan
     */
    public static void addClassesToScan(Class<?>... classes) {
        Collections.addAll(classesToScan, classes);
    }

    /**
     * Add all the classes to the ones that will be scanned
     *
     * @param classes Classes to scan
     */
    public static void addClassesToScan(Iterable<Class<?>> classes) {
        for (Class<?> clazz : classes)
            classesToScan.add(clazz);
    }

    /**
     * Return a singleton instance
     *
     * @return ActionDispatcherImpl instance
     */
    public static ActionDispatcher create() {
        return new ActionDispatcherImpl(classesToScan, packagesToScan);
    }

}
