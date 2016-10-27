package io.reneses.tela.modules.instagram.api;

import io.reneses.tela.TestUtils;
import io.reneses.tela.core.cache.CacheManagerFactory;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.core.history.History;
import io.reneses.tela.core.history.HistoryFactory;
import io.reneses.tela.modules.instagram.InstagramTestUtils;
import io.reneses.tela.modules.instagram.actions.MediaActions;
import io.reneses.tela.modules.instagram.databases.extensions.InstagramOrientDatabaseExtension;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// This class is within this package so that we can access the Twitter API and mock it
public class MediaActionsTest {

    private InstagramApi api;
    private MediaActions actions;

    @Before
    public void setUp() throws Exception {

        OrientGraphWrapperFactory.registerExtensions(new InstagramOrientDatabaseExtension());
        OrientGraphWrapperFactory.connectMemory("test");
        CacheManagerFactory.setMemoryMode();
        History history = HistoryFactory.create();

        api = new InstagramApiImpl(InstagramTestUtils.mockInstagramHttpClient());
        actions = new MediaActions(new InstagramApiWrapper(api, history));
    }

    @After
    public void tearDown() throws Exception {
        CacheManagerFactory.create().clear();
        TestUtils.destroyDatabase();
    }

    @Test
    public void selfMediaWithLimit() throws Exception {
        assertEquals(api.selfMedia("", 1), actions.selfMedia("", 1));
    }

    @Test
    public void selfMediaWithoutLimit() throws Exception {
        assertEquals(api.selfMedia(""), actions.selfMedia(""));
    }

    @Test
    public void likes() throws Exception {
        api.selfMedia("");
        assertEquals(api.likes("", "1"), actions.likes("", "1"));
    }

    @Test
    public void comments() throws Exception {
        api.selfMedia("");
        assertEquals(api.comments("", "1"), actions.comments("", "1"));
    }


}