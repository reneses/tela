package io.reneses.tela.core.dispatcher;

import io.reneses.tela.TestActions;
import io.reneses.tela.TestUtils;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.core.dispatcher.exceptions.InvalidParameterTypeException;
import io.reneses.tela.core.sessions.SessionManager;
import io.reneses.tela.core.sessions.SessionManagerFactory;
import io.reneses.tela.core.sessions.databases.extensions.SessionOrientDatabaseExtension;
import io.reneses.tela.core.sessions.models.Session;
import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class ActionDispatcherImplTest {

    private static AbstractActionDispatcher dispatcher;
    private static TestActions actions = new TestActions();
    private Session session;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        dispatcher = new ActionDispatcherImpl(Arrays.asList(TestActions.class), new ArrayList<>());
    }

    @Before
    public void setUp() throws Exception {
        SessionManager sessionManager = TestUtils.configureSessionManager();
        session = sessionManager.create();
    }

    @After
    public void tearDown() throws Exception {
        TestUtils.destroyDatabase();
    }

    @Test
    public void dispatchWithoutParams() throws Exception {
        String result = dispatcher.dispatch(session, "test", "hello");
        assertEquals(actions.hello(), result);
    }

    @Test
    public void dispatchWithStringParam() throws Exception {
        String input = " hi ";
        Map<String, String[]> params = new HashMap<>();
        params.put("input", new String[] {input});
        String result = dispatcher.dispatch(session, "test", "trim", params);
        assertEquals(actions.trim(input), result);
    }

    @Test
    public void dispatchWithIntegerParam() throws Exception {
        int input = 1;
        Map<String, String[]> params = new HashMap<>();
        params.put("n", new String[] {String.valueOf(input)});
        int result = dispatcher.dispatch(session, "test", "negate", params);
        assertEquals(actions.negate(input), result);
    }

    @Test
    public void dispatchWithCastedIntegerParam() throws Exception {
        int input = 1;
        Map<String, String[]> params = new HashMap<>();
        params.put("n", new String[] {String.valueOf(String.valueOf(input))});
        int result = dispatcher.dispatch(session, "test", "negate", params);
        assertEquals(actions.negate(input), result);
    }

    @Test(expected = InvalidParameterTypeException.class)
    public void dispatchWithInvalidIntegerParam() throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("n", new String[] { "hello" });
        dispatcher.dispatch(session, "test", "negate", params);
    }

    @Test
    public void dispatchWithArrayParam() throws Exception {
        String[] words = { "hello", "world"};
        Map<String, String[]> params = new HashMap<>();
        params.put("word", words);
        String result = dispatcher.dispatch(session, "test", "implode", params);
        assertEquals(actions.implode(words), result);
    }

    @Test
    public void dispatchWithTwoIntegerParams() throws Exception {
        int n1 = 1, n2 = 2;
        Map<String, String[]> params = new HashMap<>();
        params.put("n1", new String[] { String.valueOf(n1)});
        params.put("n2", new String[] { String.valueOf(n2)});
        int result = dispatcher.dispatch(session, "test", "sum", params);
        assertEquals(actions.sum(n1, n2), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void dispatchThrowingException() throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("n", new String[] { "-1"});
        dispatcher.dispatch(session, "test", "only-positive", params);
    }

}