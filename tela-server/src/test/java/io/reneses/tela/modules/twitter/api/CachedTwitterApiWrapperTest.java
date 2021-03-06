package io.reneses.tela.modules.twitter.api;

import io.reneses.tela.TestUtils;
import io.reneses.tela.core.cache.CacheManagerFactory;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.core.history.History;
import io.reneses.tela.core.history.HistoryFactory;
import io.reneses.tela.modules.twitter.database.extensions.TwitterOrientDatabaseExtension;
import io.reneses.tela.modules.twitter.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CachedTwitterApiWrapperTest {

    private CachedTwitterApiWrapper apiWrapper;
    private TwitterApi api;


    @Before
    public void setUp() throws Exception {

        OrientGraphWrapperFactory.registerExtensions(new TwitterOrientDatabaseExtension());
        OrientGraphWrapperFactory.connectMemory("test");
        CacheManagerFactory.setMemoryMode();
        History history = HistoryFactory.create();

        api = TwitterTestUtils.mockTwitterApi();
        apiWrapper = new CachedTwitterApiWrapper(api, history);

    }

    @After
    public void tearDown() throws Exception {
        CacheManagerFactory.create().clear();
        TestUtils.destroyDatabase();
    }

    @Test
    public void self() throws Exception {

        User self = api.self("1", "2", "3", "4");
        verify(api, times(1)).self("1", "2", "3", "4");

        // Cached
        User result = apiWrapper.self("1", "2", "3", "4");
        assertNotNull(result);
        assertEquals(self, result);
        verify(api, times(2)).self("1", "2", "3", "4");

        // Cached
        result = apiWrapper.self("1", "2", "3", "4");
        assertNotNull(result);
        assertEquals(self, result);
        verify(api, times(2)).self("1", "2", "3", "4");

    }

    @Test
    public void user() throws Exception {

        User self = api.user("1", "2", "3", "4", "uniovi");
        verify(api, times(1)).user("1", "2", "3", "4", "uniovi");

        // Cached
        User result = apiWrapper.user("1", "2", "3", "4", "uniovi");
        assertNotNull(result);
        assertEquals(self, result);
        verify(api, times(2)).user("1", "2", "3", "4", "uniovi");

        // Cached
        result = apiWrapper.user("1", "2", "3", "4", "uniovi");
        assertNotNull(result);
        assertEquals(self, result);
        verify(api, times(2)).user("1", "2", "3", "4", "uniovi");

    }

    @Test
    public void followersWithLimit() throws Exception {

        User self = api.self("1", "2", "3", "4");
        List<User> followers = api.followers("1", "2", "3", "4", self.getScreenName(), 5);
        verify(api, times(1)).followers("1", "2", "3", "4", self.getScreenName(), 5);

        // Non cached
        List<User> result = apiWrapper.followers("1", "2", "3", "4", self.getScreenName(), 5);
        assertEquals(result.size(), followers.size());
        assertTrue(result.containsAll(followers));
        verify(api, times(2)).followers("1", "2", "3", "4", self.getScreenName(), 5);

        // Again, non cached, as we are specifying a limit
        result = apiWrapper.followers("1", "2", "3", "4", self.getScreenName(), 5);
        assertEquals(result.size(), followers.size());
        assertTrue(result.containsAll(followers));
        verify(api, times(2)).followers("1", "2", "3", "4", self.getScreenName(), 5);

    }
    @Test
    public void followersWithoutLimit() throws Exception {

        User self = api.self("1", "2", "3", "4");
        List<User> followers = api.followers("1", "2", "3", "4", self.getScreenName());
        verify(api, times(1)).followers("1", "2", "3", "4", self.getScreenName(), -1);

        // Non cached
        List<User> result = apiWrapper.followers("1", "2", "3", "4", self.getScreenName());
        assertEquals(result.size(), followers.size());
        assertTrue(result.containsAll(followers));
        verify(api, times(2)).followers("1", "2", "3", "4", self.getScreenName(), -1);

        // Again, non cached, as we are specifying a limit
        result = apiWrapper.followers("1", "2", "3", "4", self.getScreenName());
        assertNotNull(result);
        assertEquals(result.size(), followers.size());
        assertTrue(result.containsAll(followers));
        verify(api, times(2)).followers("1", "2", "3", "4", self.getScreenName(), -1);

    }
    @Test
    public void followingWithLimit() throws Exception {

        User self = api.self("1", "2", "3", "4");
        List<User> following = api.following("1", "2", "3", "4", self.getScreenName(), 5);
        verify(api, times(1)).following("1", "2", "3", "4", self.getScreenName(), 5);

        // Non cached
        List<User> result = apiWrapper.following("1", "2", "3", "4", self.getScreenName(), 5);
        assertEquals(result.size(), following.size());
        assertTrue(result.containsAll(following));
        verify(api, times(2)).following("1", "2", "3", "4", self.getScreenName(), 5);

        // Again, non cached, as we are specifying a limit
        result = apiWrapper.following("1", "2", "3", "4", self.getScreenName(), 5);
        assertEquals(result.size(), following.size());
        assertTrue(result.containsAll(following));
        verify(api, times(2)).following("1", "2", "3", "4", self.getScreenName(), 5);

    }

    @Test
    public void followingWithoutLimit() throws Exception {

        User self = api.self("1", "2", "3", "4");
        List<User> following = api.following("1", "2", "3", "4", self.getScreenName());
        verify(api, times(1)).following("1", "2", "3", "4", self.getScreenName(), -1);

        // Non cached
        List<User> result = apiWrapper.following("1", "2", "3", "4", self.getScreenName());
        assertEquals(following.size(), result.size());
        assertTrue(following.containsAll(result));
        verify(api, times(2)).following("1", "2", "3", "4", self.getScreenName(), -1);

        // Again, non cached, as we are specifying a limit
        result = apiWrapper.following("1", "2", "3", "4", self.getScreenName());
        assertNotNull(result);
        assertEquals(following.size(), result.size());
        assertTrue(following.containsAll(result));
        verify(api, times(2)).following("1", "2", "3", "4", self.getScreenName(), -1);

    }

    @Test
    public void friends() throws Exception {

        User self = api.self("1", "2", "3", "4");
        List<User> friends = api.followers("1", "2", "3", "4", self.getScreenName());
        friends.retainAll(api.following("1", "2", "3", "4", self.getScreenName()));
        verify(api, times(1)).followers("1", "2", "3", "4", self.getScreenName(), -1);
        verify(api, times(1)).following("1", "2", "3", "4", self.getScreenName(), -1);

        // Non cached
        List<User> result = apiWrapper.friends("1", "2", "3", "4", self.getScreenName());
        assertEquals(friends.size(), result.size());
        assertTrue(friends.containsAll(result));
        verify(api, times(2)).followers("1", "2", "3", "4", self.getScreenName(), -1);
        verify(api, times(2)).following("1", "2", "3", "4", self.getScreenName(), -1);

        // Again, non cached, as we are specifying a limit
        result = apiWrapper.friends("1", "2", "3", "4", self.getScreenName());
        assertNotNull(result);
        assertEquals(friends.size(), result.size());
        assertTrue(friends.containsAll(result));
        verify(api, times(2)).followers("1", "2", "3", "4", self.getScreenName(), -1);
        verify(api, times(2)).following("1", "2", "3", "4", self.getScreenName(), -1);

    }



}