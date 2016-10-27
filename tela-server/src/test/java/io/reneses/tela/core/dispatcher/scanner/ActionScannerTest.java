package io.reneses.tela.core.dispatcher.scanner;

import io.reneses.tela.TestActions;
import io.reneses.tela.core.dispatcher.models.Module;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

public class ActionScannerTest {

    private AbstractActionScanner scanner;

    @Test
    public void scanClass() throws Exception {
        scanner = ActionScannerFactory.create(Arrays.asList(TestActions.class),null);
        Map<String, Module> modules = scanner.scan();
        assertNotNull(modules);
        assertTrue(modules.containsKey("test"));
        Module testModule = modules.get("test");
        assertNotNull(testModule.getAction("hello"));
    }

    @Test
    public void scanPackage() throws Exception {
        scanner = ActionScannerFactory.create(null, Arrays.asList(TestActions.class.getPackage()));
        Map<String, Module> modules = scanner.scan();
        assertNotNull(modules);
        assertTrue(modules.containsKey("test"));
        assertTrue(modules.containsKey("test2"));
    }

    @Test
    public void scanNull() throws Exception {
        scanner = ActionScannerFactory.create(null, null);
        Map<String, Module> modules = scanner.scan();
        assertNotNull(modules);
        assertTrue(modules.isEmpty());
    }

}