package io.reneses.tela.core.dispatcher.scanner;

import io.reneses.tela.core.dispatcher.models.Action;
import io.reneses.tela.core.dispatcher.models.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The function scanner is the component responsible of scanning the classes, extracting the actions, storing them,
 * and returning them by its name and parameters
 */
class ActionScanner extends AbstractActionScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionScanner.class);

    /**
     * Instantiate the scanner, scanning, parsing and storing the actions within the supplied classes
     *
     * @param classesToScan  Classes to scan
     * @param packagestoScan Packages to scan
     */
    ActionScanner(Collection<Class<?>> classesToScan, Collection<Package> packagestoScan) {
        super(classesToScan, packagestoScan);
    }

    /**
     * Aggregate the classes to scan and the classes within the packages to scan
     *
     * @return All the classes to scan
     */
    private List<Class<?>> collectClassesToScan() {
        List<Class<?>> output = new ArrayList<>();
        if (classesToScan != null)
            output.addAll(classesToScan);
        if (packagestoScan != null)
            for (Package packageToScan : packagestoScan)
                output.addAll(PackageScanner.getClassesForPackage(packageToScan));
        return output;
    }

    /**
     * Process a module, finding and parsing all the actions within it
     *
     * @param c Class
     * @return Module
     */
    private Module processModule(Class<?> c) {

        io.reneses.tela.core.dispatcher.annotations.Module annotation =
                c.getAnnotation(io.reneses.tela.core.dispatcher.annotations.Module.class);

        String name = annotation.value();
        Module module = new Module(name);
        for (Method m : c.getMethods()) {
            if (m.isAnnotationPresent(io.reneses.tela.core.dispatcher.annotations.Action.class)) {
                try {
                    Action action = new Action(m);
                    LOGGER.debug("[Dispatcher] Action loaded: {}/{} {}", module.getName(), action.getName(), action.getParameters().keySet());
                    module.addAction(action);
                } catch (Exception e) {
                    LOGGER.error("[Dispatcher] The method " + m.getDeclaringClass() + "." + m.getName() + " could not be init as an action", e);
                }
            }
        }
        return module;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Module> scan() {
        List<Class<?>> classesToScan = collectClassesToScan();
        Map<String, Module> modules = new HashMap<>();
        LOGGER.debug("[Dispatcher] Scanning classes: {}",
                classesToScan.stream().map(Class::getSimpleName).collect(Collectors.toList()));
        classesToScan
                .stream()
                .filter(c -> c.isAnnotationPresent(io.reneses.tela.core.dispatcher.annotations.Module.class))
                .forEach(c -> {
                    Module module = processModule(c);
                    if (modules.containsKey(module.getName()))
                        modules.get(module.getName()).addActions(module.getAllActions());
                    else
                        modules.put(module.getName(), module);
                });
        LOGGER.debug("[Dispatcher] Classes scanning finished, {} modules found: {}", modules.size(), modules.keySet());
        return modules;
    }

}
