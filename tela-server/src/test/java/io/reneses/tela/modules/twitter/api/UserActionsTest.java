package io.reneses.tela.modules.twitter.api;

import io.reneses.tela.TestUtils;
import io.reneses.tela.core.cache.CacheManagerFactory;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.core.history.History;
import io.reneses.tela.core.history.HistoryFactory;
import io.reneses.tela.modules.twitter.actions.UserActions;
import io.reneses.tela.modules.twitter.database.extensions.TwitterOrientDatabaseExtension;
import io.reneses.tela.modules.twitter.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// This class is within this package so that we can access the Twitter API and mock it
public class UserActionsTest {

    private TwitterApi api;
    private UserActions actions;

    @Before
    public void setUp() throws Exception {

        OrientGraphWrapperFactory.registerExtensions(new TwitterOrientDatabaseExtension());
        OrientGraphWrapperFactory.connectMemory("test");
        CacheManagerFactory.setMemoryMode();
        History history = HistoryFactory.create();

        api = TwitterTestUtils.mockTwitterApi();
        actions = new UserActions(new TwitterApiWrapper(api, history));

    }

    @After
    public void tearDown() throws Exception {
        CacheManagerFactory.create().clear();
        TestUtils.destroyDatabase();
    }

    @Test
    public void self() throws Exception {
        assertEquals(api.self("1", "2", "3","4"), actions.self("1:2:3:4"));
    }

    @Test
    public void user() throws Exception {
        assertEquals(api.user("1", "2", "3","4", "uniovi"), actions.user("1:2:3:4", "uniovi"));
    }

    @Test
    public void followersWithoutLimit() throws Exception {
        assertEquals(api.followers("1", "2", "3","4",""), actions.followers("1:2:3:4", ""));
    }

    @Test
    public void followersWithLimit() throws Exception {
        assertEquals(api.followers("1", "2", "3","4","",3), actions.followers("1:2:3:4", "",3));
    }

    @Test
    public void followingWithoutLimit() throws Exception {
        assertEquals(api.following("1", "2", "3","4",""), actions.following("1:2:3:4", ""));
    }

    @Test
    public void followingWithLimit() throws Exception {
        assertEquals(api.following("1", "2", "3","4","",3), actions.following("1:2:3:4", "",3));
    }

    @Test
    public void friends() throws Exception {
        List<User> friends = api.followers("1", "2", "3","4","");
        friends.retainAll(api.following("1", "2", "3","4", ""));
        List<User> retrieved =  actions.friends("1:2:3:4",api.self("1", "2", "3","4").getScreenName());
        assertEquals(friends.size(), retrieved.size());
        assertTrue(friends.containsAll(retrieved));
    }

}