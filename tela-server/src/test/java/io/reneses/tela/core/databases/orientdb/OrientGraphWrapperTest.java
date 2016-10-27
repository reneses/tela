package io.reneses.tela.core.databases.orientdb;

import com.orientechnologies.orient.core.metadata.schema.OSchemaProxy;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrientGraphWrapperTest {

    @Before
    public void setUp() throws Exception {
        OrientGraphWrapperFactory.registerExtensions(new TestGraphExtension());
        OrientGraphWrapperFactory.connectMemory("Test");
    }

    @After
    public void tearDown() throws Exception {
       OrientGraphWrapperFactory.dropAndDestroyInstance();
    }

    @Test
    public void testInit() throws Exception {
        OrientGraphNoTx graph =  OrientGraphWrapperFactory.get().getNoTxGraph();
        OSchemaProxy schema = graph.getRawGraph().getMetadata().getSchema();
        assertTrue(schema.existsClass(TestGraphExtension.CLASS));
        assertTrue(schema.getClass(TestGraphExtension.CLASS).existsProperty(TestGraphExtension.NAME));
        assertTrue(schema.getClass(TestGraphExtension.CLASS).existsProperty(TestGraphExtension.ID));
        assertTrue(schema.getClass(TestGraphExtension.CLASS).existsProperty(TestGraphExtension.AGE));
        assertTrue(schema.existsClass(TestGraphExtension.CALLS));
        assertTrue(schema.getClass(TestGraphExtension.CLASS).areIndexed(TestGraphExtension.ID));
    }

}