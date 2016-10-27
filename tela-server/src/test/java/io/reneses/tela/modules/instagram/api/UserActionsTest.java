package io.reneses.tela.modules.instagram.api;

import io.reneses.tela.TestUtils;
import io.reneses.tela.core.cache.CacheManagerFactory;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.core.history.History;
import io.reneses.tela.core.history.HistoryFactory;
import io.reneses.tela.modules.instagram.InstagramTestUtils;
import io.reneses.tela.modules.instagram.actions.UserActions;
import io.reneses.tela.modules.instagram.databases.extensions.InstagramOrientDatabaseExtension;
import io.reneses.tela.modules.instagram.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// This class is within this package so that we can access the Twitter API and mock it
public class UserActionsTest {

    private InstagramApi api;
    private UserActions actions;

    @Before
    public void setUp() throws Exception {

        OrientGraphWrapperFactory.registerExtensions(new InstagramOrientDatabaseExtension());
        OrientGraphWrapperFactory.connectMemory("test");
        CacheManagerFactory.setMemoryMode();
        History history = HistoryFactory.create();

        api = new InstagramApiImpl(InstagramTestUtils.mockInstagramHttpClient());
        actions = new UserActions(new InstagramApiWrapper(api, history));
    }

    @After
    public void tearDown() throws Exception {
        CacheManagerFactory.create().clear();
        TestUtils.destroyDatabase();
    }

    @Test
    public void self() throws Exception {
        assertEquals(api.self(""), actions.self(""));
    }

    @Test
    public void search() throws Exception {
        assertEquals(api.search("", "jack"), actions.search("", "jack"));
    }

    @Test
    public void userById() throws Exception {
        assertEquals(api.user("", 1), actions.user("", 1));
    }

    @Test
    public void userByUsername() throws Exception {
        assertEquals(api.user("", 1), actions.user("", "jack"));
    }

    @Test
    public void followersWithoutLimit() throws Exception {
        assertEquals(api.followers(""), actions.followers(""));
    }

    @Test
    public void followersWithLimit() throws Exception {
        assertEquals(api.followers("", 1), actions.followers("", 1));
    }

    @Test
    public void followingWithoutLimit() throws Exception {
        assertEquals(api.following(""), actions.following(""));
    }

    @Test
    public void followingWithLimit() throws Exception {
        assertEquals(api.following("", 1), actions.following("", 1));
    }

    @Test
    public void friends() throws Exception {
        List<User> friends = api.followers("");
        friends.retainAll(api.following(""));
        assertEquals(friends, actions.friends(""));
    }

    @Test
    public void countsById() throws Exception {
        User user = actions.user("", 1);
        assertTrue(actions.counts("", 1).contains(user.getCounts()));
    }

    @Test
    public void countsByUsername() throws Exception {
        User user = actions.user("", 1);
        assertTrue(actions.counts("", "jack").contains(user.getCounts()));
    }

    @Test
    public void relationshipById() throws Exception {
        assertEquals(api.relationship("", 1), actions.relationship("", 1));
    }

    @Test
    public void relationshipByUsername() throws Exception {
        assertEquals(api.relationship("", 1), actions.relationship("", "jack"));
    }

}