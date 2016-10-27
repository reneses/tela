package io.reneses.tela.modules.twitter.api;

import io.reneses.tela.TestUtils;
import io.reneses.tela.core.cache.CacheManagerFactory;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.core.history.History;
import io.reneses.tela.core.history.HistoryFactory;
import io.reneses.tela.modules.twitter.TwitterTestUtils;
import io.reneses.tela.modules.twitter.database.extensions.TwitterOrientDatabaseExtension;
import io.reneses.tela.modules.twitter.models.User;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TwitterApiWrapperTest {

    private AbstractTwitterApiWrapper apiWrapper;
    private TwitterApi api;


    @Before
    public void setUp() throws Exception {

        OrientGraphWrapperFactory.registerExtensions(new TwitterOrientDatabaseExtension());
        OrientGraphWrapperFactory.connectMemory("test");
        CacheManagerFactory.setMemoryMode();
        History history = HistoryFactory.create();

        api = new TwitterApiImpl(TwitterTestUtils.mockTwitterHttpClient());
        apiWrapper = new TwitterApiWrapper(api, history);

    }

    @After
    public void tearDown() throws Exception {
        CacheManagerFactory.create().clear();
        TestUtils.destroyDatabase();
    }

    @Test
    public void self() throws Exception {
        assertEquals(api.self(""), apiWrapper.self(""));
    }

    @Test
    public void followersWithoutLimit() throws Exception {
        assertEquals(api.followers("",""), apiWrapper.followers("", ""));
    }

    @Test
    public void followersWithLimit() throws Exception {
        assertEquals(api.followers("","",3), apiWrapper.followers("", "",3));
    }

    @Test
    public void followingWithoutLimit() throws Exception {
        assertEquals(api.following("",""), apiWrapper.following("", ""));
    }

    @Test
    public void followingWithLimit() throws Exception {
        assertEquals(api.following("","",3), apiWrapper.following("", "",3));
    }

    @Test
    public void friends() throws Exception {
        List<User> friends = api.followers("","");
        friends.retainAll(api.following("", ""));
        List<User> retrieved =  apiWrapper.friends("",api.self("").getScreenName());
        assertEquals(friends.size(), retrieved.size());
        assertTrue(friends.containsAll(retrieved));
    }

}